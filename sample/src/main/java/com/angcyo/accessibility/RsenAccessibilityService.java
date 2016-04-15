package com.angcyo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rsen.util.T;

import java.util.List;

public class RsenAccessibilityService extends AccessibilityService {
    public static final boolean DEBUG = true;
    public static final String TAG = "AccessibilityService";
    public static final String TEXT_WEIXIN = "微信";
    public static final String TEXT_FJDR = "附近的人";
    public static final String TEXT_START_FJDR = "开始查看";
    public static final String TEXT_START2_FJDR = "你可以在此看到附近的人";
    public static final String TEXT_DETAIL = "详细资料";
    public static final String TEXT_TXL = "通讯录";
    public static final String TEXT_FX = "发现";
    public static final String TEXT_ME = "我";
    public static final String TEXT_LIST_ITEM = "以内";
    public static final String TEXT_DZH = "打招呼";
    public static final String TEXT_SAY_HI = "加为朋友";
    public static final String TEXT_SAY_SEND = "发送";
    public static final String TEXT_SAY_HI2 = "添加朋友请求已发送";
    public static final String TEXT_HELLO = "打个招呼";
    public static final int SLEEP_TIME = 300;//两次发送返回按键事件间隔,建议大于300毫秒,否则可能无法响应
    public static final int PAGE_HOME = 1;//表示当前在主页
    public static final int PAGE_FJDR = 2;//表示当前在附近的人
    public static final int PAGE_DETAIL = 3;//表示当前在详细信息
    public static final int PAGE_SAY_HI = 4;//表示当前在加好友
    private static AlertDialog alertDialog;
    private long index = 0;
    private boolean needBack = false;//添加好友之后,请求返回.
    private int memberNumIndex = 0;//一屏需要添加的好友数量, 用于控制滚动ListView
    private long addMemberNum = -1;//执行了多少次添加朋友操作
    //    private List<AccessibilityNodeInfo> lastItemList;//保存最后一次附近人的列表信息,用于判断是否全部添加了好友.
    private boolean isOver = false;
    private boolean requestScroll = false;//自动滚屏的标识符,用于标识该检查滚动事件
    private ScrollHandler scrollHandler;
    private int curPage = -1;
    private int pageIndex = -1;//页面计数器
    private String lastItemText = "";//保存滚动开始前,最后一个的文本信息
    private boolean canStart;//如果不是从附近的人界面进入的,直接不处理,返回;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // setServiceInfo();//这个方法同样可以实现xml中的配置信息
        e((new Exception()).getStackTrace()[1].getMethodName());

        createTipDialog();
        scrollHandler = new ScrollHandler();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //关闭服务时,调用
        e((new Exception()).getStackTrace()[1].getMethodName());
        hideTipDialog();
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return;
        }

        CharSequence packageName = rootInActiveWindow.getPackageName();
        if (TextUtils.isEmpty(packageName) || !packageName.toString().contains("com.tencent.mm")) {
            return;
        }

        //当窗口发生的事件是我们配置监听的事件时,会回调此方法.会被调用多次
//        if (BuildConfig.DEBUG) {
//            try {
//                e((new Exception()).getStackTrace()[1].getMethodName() + getDebugInfo(event));
//            } catch (Exception e) {
//            }
//        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (!RAccessibilityActivity.isDeviceRegister()) {
                //未注册直接返回
                return;
            }

            if (isWeiXinHomePage(event)) {
                e("已经进入\"主页\"界面");
                curPage = PAGE_HOME;
                needBack = false;
                canStart = false;
                incrementPageIndex();

                if (alertDialog != null) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        alertDialog = null;
                        if (addMemberNum > 0) {
                            RAccessibilityActivity.saveLastClick(addMemberNum);
                        }
                    }
                }
                addMemberNum = -1;
                //主页
//                if (!needBack) {
//                    jumpToFaXianPage(event);
//                jumpToFJDRPage(event);
//                }
//                long num = addMemberNum;
//                ready();
//                if (isOver) {
//                    showOverDialog(num);
//                    isOver = false;
//                    if (RAccessibilityActivity.isDebugKey()) {
//                        RAccessibilityActivity.cleanCodeInfo();
//                    }
//                }

            } else if (isWeiXinFJDRPage(event)) {
                curPage = PAGE_FJDR;
                incrementPageIndex();
                //附近的人
                e("已经进入\"附近的人\"界面");
                needBack = false;
                canStart = true;

                List<AccessibilityNodeInfo> startFJDR = source.findAccessibilityNodeInfosByText(TEXT_START_FJDR);
                List<AccessibilityNodeInfo> startFJDR2 = source.findAccessibilityNodeInfosByText(TEXT_START2_FJDR);
                if (startFJDR.size() > 0 && startFJDR2.size() > 0) {
                    //开始查看界面
                    T.show(getApplicationContext(), "亲,还差一步哦!");
                } else {
                    try {
                /*获取到ListView, 第一次打开附近的人, 很有可能出现 开始查看 界面*/
                        if (addMemberNum == -1) {
                            T.show(getApplicationContext(), "loading...");
                        }

                        AccessibilityNodeInfo listNode = getListNode(source);
                        if (listNode != null && listNode.getChildCount() > 0) {
                            if (addMemberNum == -1) {
                                T.show(getApplicationContext(), "正在接管操作,请稍等...");
                                addMemberNum = 0;
                            }
                            if (addMemberNum < 1) {
                                showTipMsg("接下来,就交给我吧...");
                            }
                            //查找所有Items
                            List<AccessibilityNodeInfo> itemList = listNode.findAccessibilityNodeInfosByText(TEXT_LIST_ITEM);
                            if (itemList.size() > 0) {
                                //查找到非空列表

                                if (memberNumIndex >= itemList.size()) {
                                    //请求滚动屏幕
                                    e("请求滚动(前进)...");
                                    memberNumIndex = 0;
                                    requestListScroll(listNode);
                                } else {
                                    clickListItem(itemList, memberNumIndex++);
                                }
                            }
                        } else {
//                            T.show(getApplicationContext(), "Sorry, 请告诉我:你的是哪款手机?");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (isWeiXinDetailPage(event)) {
                if (!canStart) {
                    return;
                }
                curPage = PAGE_DETAIL;
                incrementPageIndex();
                //详细资料
                e("详细资料");
                showToast("进入'详细资料'");
                if (needBack) {
                    sendBackKey();
                } else {
                    if (!clickButton(source, TEXT_DZH)) {
                        needBack = true;
                        sendBackKey();
                    }
                }
                checkBack(PAGE_DETAIL, pageIndex);
            } else if (isWeiXinSayHiPage(event)) {
                if (!canStart) {
                    return;
                }
                curPage = PAGE_SAY_HI;
                incrementPageIndex();
                //打招呼,聊天界面
                e("聊天界面 needBack:" + needBack);
                showToast("进入'聊天界面'");
//                scrollHandler.sendMessage(scrollHandler.obtainMessage(ScrollHandler.MSG_BACK, curPage, 0));
//                scrollHandler.sendMessageDelayed(scrollHandler.obtainMessage(ScrollHandler.MSG_BACK, curPage, 0), 200);
                if (isKeyboardShow(source)) {
                    e("键盘弹出...请求隐藏键盘...");
                    sendBackKey();
                }

                if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_SAY_HI2), TEXT_SAY_HI2, false)) {
                    //如果已经添加过了
                    e("已经发送过好友请求...");
                    showToast("已经发送过请求了~~");
                } else {
                    //发送打招呼文本到文本输入框
                    sendComment(source);
                    clickSendButton(source);
//                    clickButton(source, TEXT_SAY_HI);
                    addMemberNum++;
                }
//                e("请求返回...隐藏键盘");
//                performGlobalAction(GLOBAL_ACTION_BACK);
                needBack = true;
                sendBackKey();
                checkBack(PAGE_SAY_HI, pageIndex);
            } else {
                curPage = -1;
            }
//            else {
//                needBack = true;
//            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (requestScroll) {
                requestScroll = false;
//                e("附近的人页面滚动事件...");
                if (source.getClassName().equals(ListView.class.getName())) {
                    List<AccessibilityNodeInfo> itemList = source.findAccessibilityNodeInfosByText(TEXT_LIST_ITEM);
//                    showItemListInfo(itemList);
                    int offset = 0;
                    try {
                        String itemText = itemList.get(0).getParent().getChild(0).getText().toString();
                        if (lastItemText.equals(itemText)) {
                            offset = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    e("附近的人页面滚动事件...end");
//                    requestListScroll(source);
                    clickListItem(itemList, memberNumIndex++ + offset);
                }
            }
//            if (isWeiXinFJDRPage(event)) {
//                AccessibilityNodeInfo listNode = source.getChild(0).getChild(1);
//                List<AccessibilityNodeInfo> itemList = listNode.findAccessibilityNodeInfosByText(TEXT_LIST_ITEM);
//                showItemListInfo(itemList);
////                listNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                e("附近的人页面滚动事件...");
//            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
//            //这个事件调用很频繁
//            if (isWeiXinSayHiPage(event)) {
//                if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_SAY_HI2), TEXT_SAY_HI2, false)) {
//                    //强制退出此界面
//                    e("强制退出 加好友界面");
//                    sendBackKey();
//                }
//            }
        }

        source.recycle();
        e(event.getEventType() + " 事件ID");

//        alertDialog.setMessage(event.getText() + " -- " + String.valueOf(event.getEventType()) + " -- " + index++);
    }


    private AccessibilityNodeInfo getListNode(AccessibilityNodeInfo source) {
//        int childCount = source.getChildCount();
//        if (childCount == 1) {
//            //小米手机 测试通过
//            listNode = source.getChild(0).getChild(1);
//        } else if (childCount == 2) {
//            //魅族手机测试通过
//            listNode = source.getChild(1);
//        }
        AccessibilityNodeInfo listNode = null;
        for (int i = 0; i < source.getChildCount(); i++) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (child.getClassName().toString().equals(ListView.class.getName())) {
                return child;
            }
            AccessibilityNodeInfo node = getListNode(child);
            if (node != null) {
                return node;
            }
        }
        return listNode;
    }

    private void requestListScroll(AccessibilityNodeInfo listNode) {
        requestScroll = true;
        listNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        showToast("正在滚动'附近的人'列表");
        scrollHandler.sendMessageDelayed(scrollHandler.obtainMessage(ScrollHandler.MSG_SCROLL), 1000);
    }

    private void ready() {
        needBack = false;
        memberNumIndex = 0;
        addMemberNum = 0;
        hideTipDialog();
    }

    private void showOverDialog(long num) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("完美结束")
                .setMessage("上一次的战果是:" + num + "次")
                .setPositiveButton("恭喜发财", null)
                .create();

        Window window = alertDialog.getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_TOAST);
        window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        alertDialog.show();
    }

    /**
     * 附近的人ListView Item点击事件
     */
    private void clickListItem(List<AccessibilityNodeInfo> itemList, int index) {
        AccessibilityNodeInfo info = itemList.get(index);
        try {
            lastItemText = info.getParent().getChild(0).getText().toString();
            showToast("查看:" + lastItemText);
            BmobUtil.saveUserInfo(this, lastItemText);
        } catch (Exception e) {
            e.printStackTrace();
            lastItemText = "--";
        }
        info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    /**
     * 请在打招呼界面调用此方法
     */
    private boolean isKeyboardShow(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo editText = null;
        try {
            editText = source.getChild(0).getChild(0).getChild(4).getChild(0).getChild(0);
            if (editText != null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void sendComment(AccessibilityNodeInfo source) {
        String sayHiString = RAccessibilityActivity.getSayHiString();
        if (TextUtils.isEmpty(sayHiString)) {
            return;
        }

        String[] split = sayHiString.split("\\|");
        sayHiString = split[(int) (addMemberNum % split.length)];

        e("打招呼语:" + sayHiString);

        try {
            AccessibilityNodeInfo editText = source.getChild(0).getChild(0).getChild(4).getChild(0).getChild(0);//键盘未弹出,使用此方法
            if (editText == null) {
                editText = source.getChild(0).getChild(0).getChild(5).getChild(0).getChild(0);//键盘弹出,使用此方法
            }
            if ("android.widget.EditText".equals(editText.getClassName())) {
                showToast("发送消息:" + sayHiString);
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, sayHiString);
                editText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }

            //发送文本
            List<AccessibilityNodeInfo> sendButton = source.findAccessibilityNodeInfosByText(TEXT_SAY_SEND);
            if (sendButton != null && sendButton.size() > 0) {
                AccessibilityNodeInfo info = sendButton.get(sendButton.size() - 1);
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Thread.sleep(2 * SLEEP_TIME);
            }

        } catch (Exception e) {
            // Not support
        }
    }

    private void showItemListInfo(List<AccessibilityNodeInfo> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            AccessibilityNodeInfo info = itemList.get(i);
            e("Item:" + info.getParent().getChild(0).getText() + " -- " + info.getText().toString());
        }
    }

    /**
     * 返回值表示,是否调用单击事件
     */
    private boolean clickButton(AccessibilityNodeInfo source, String buttonText) {
        try {
            List<AccessibilityNodeInfo> nodeInfos = source.findAccessibilityNodeInfosByText(buttonText);
            for (AccessibilityNodeInfo info : nodeInfos) {
                if (info.getClassName().equals(Button.class.getName())) {
                    if (info.isVisibleToUser()) {
                        showToast("点击按钮:" + info.getText());
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 返回值表示,打招呼界面的添加好友按钮,,不同的版本,会有文本上的差异
     */
    private boolean clickSendButton(AccessibilityNodeInfo source) {
        List<AccessibilityNodeInfo> nodeInfos = source.findAccessibilityNodeInfosByText(TEXT_SAY_HI);
        for (AccessibilityNodeInfo info : nodeInfos) {
            if (info.getClassName().equals(Button.class.getName())) {
                if (info.isVisibleToUser()) {
                    showToast("点击按钮:" + info.getText());
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                } else {
                    return false;
                }
            }
        }
        nodeInfos = source.findAccessibilityNodeInfosByText(TEXT_SAY_SEND);
        for (AccessibilityNodeInfo info : nodeInfos) {
            if (info.getClassName().equals(Button.class.getName())) {
                if (info.isVisibleToUser()) {
                    showToast("点击按钮:" + info.getText());
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void showTipMsg(String msg) {
        if (alertDialog == null) {
            createTipDialog();
        }
        alertDialog.setMessage(msg);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void showToast(String request) {
        showTipMsg("-->" + request + "\n已为您申请好友:" + addMemberNum + "次,\n如果发现程序打瞌睡了,熄屏亮屏一次,就可以激活啦!");
    }

//    /**
//     * 判断是否已经滚动到了附近的人的底部
//     */
//    private boolean isNodeListFoot(List<AccessibilityNodeInfo> list) {
//        if (lastItemList == null || list == null) {
//            return false;
//        }
//
//        return lastItemList.containsAll(list);
//    }

    /**
     * 跳转到 发现 界面
     */

    private void jumpToFaXianPage(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        List<AccessibilityNodeInfo> nodeInfos = source.findAccessibilityNodeInfosByText(TEXT_FX);
        for (AccessibilityNodeInfo info : nodeInfos) {
            CharSequence text = info.getText();
            if (TextUtils.isEmpty(text) || TextUtils.isEmpty(info.getClassName())) {
                continue;
            }
            /*文本是nodeText,并且是TextView类型*/
            if (TEXT_FX.equals(text) && TextView.class.getName().equals(info.getClassName())) {
                info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//发送点击事件
            }
        }
        e("跳转到\"发现\"界面");
    }

    /**
     * 跳转到 附近的人 界面
     */
    private void jumpToFJDRPage(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        List<AccessibilityNodeInfo> nodeInfos = source.findAccessibilityNodeInfosByText(TEXT_FJDR);
        for (AccessibilityNodeInfo info : nodeInfos) {
            CharSequence text = info.getText();
            if (TextUtils.isEmpty(text) || TextUtils.isEmpty(info.getClassName())) {
                continue;
            }
            /*文本是nodeText,并且是TextView类型*/
            if (TEXT_FJDR.equals(text) && TextView.class.getName().equals(info.getClassName())) {
                info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//发送点击事件
            }
        }

        e("跳转到\"附近的人\"界面");
    }

    /**
     * 判断是否是微信首页
     */
    private boolean isWeiXinHomePage(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_WEIXIN), TEXT_WEIXIN) &&
                haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_TXL), TEXT_TXL) &&
                haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_FX), TEXT_FX) &&
                haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_ME), TEXT_ME)) {
            //如果这个窗口包含,微信,通讯录,发现,我.就判断是微信主页面
            return true;
        }

        return false;
    }

    /**
     * 判断是否是附近的人界面
     */
    private boolean isWeiXinFJDRPage(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_FJDR), TEXT_FJDR, false)) {
            //如果不是主页,又包含 附近的人 TextView,那么就返回 true

            if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_WEIXIN), TEXT_WEIXIN) &&
                    haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_TXL), TEXT_TXL) &&
                    haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_FX), TEXT_FX) &&
                    haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_ME), TEXT_ME)) {
                //如果这个时候又发现了有主页的特征...则...
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * 判断是否是详细资料界面
     */
    private boolean isWeiXinDetailPage(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_DETAIL), TEXT_DETAIL, false)) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否是打招呼界面
     */
    private boolean isWeiXinSayHiPage(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_SAY_HI), TEXT_SAY_HI, false)) {
            return true;
        } else if (haveNodeInfo(source.findAccessibilityNodeInfosByText(TEXT_SAY_HI2), TEXT_SAY_HI2, false)) {
            return true;
        }

        return false;
    }

    private boolean haveNodeInfo(List<AccessibilityNodeInfo> nodeInfos, String nodeText) {
        return haveNodeInfo(nodeInfos, nodeText, true);
    }

    private boolean haveNodeInfo(List<AccessibilityNodeInfo> nodeInfos, String nodeText, boolean strictMode) {
        if (nodeInfos == null || nodeInfos.size() == 0 || TextUtils.isEmpty(nodeText)) {
            return false;
        }

        for (AccessibilityNodeInfo info : nodeInfos) {
            CharSequence text = info.getText();
            if (TextUtils.isEmpty(text)) {
                continue;
            }
            /*文本是nodeText,并且是TextView类型*/
            if (strictMode) {
                if (nodeText.equals(text.toString()) && TextView.class.getName().equals(info.getClassName().toString())) {
                    return true;
                }
            } else {
                if (text.toString().contains(nodeText)) {
                    return true;
                }
            }

        }

        return false;
    }

    private void checkBack(int curPage, int pageIndex) {
        scrollHandler.removeMessages(ScrollHandler.MSG_BACK);
        scrollHandler.sendMessageDelayed(scrollHandler.obtainMessage(ScrollHandler.MSG_BACK, curPage, pageIndex), 3 * SLEEP_TIME);
    }

    private void sendBackKey() {
        String page = "";
        switch (curPage) {
            case PAGE_DETAIL:
                page = "详细资料";
                break;
            case PAGE_FJDR:
                page = "附近的人";
                break;
            case PAGE_HOME:
                page = "首页";
                break;
            case PAGE_SAY_HI:
                page = "聊天";
                break;
            default:
                break;
        }
        showToast("'" + page + "' 此界面,正在返回...");
        performGlobalAction(GLOBAL_ACTION_BACK);
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
        }
    }

    private void createTipDialog() {
        if (alertDialog != null) {
            return;
        }

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("在此对话框消失之前,请不要进行其他操作.")
                .setMessage("对话框内容^_^")
//                .setPositiveButton("确定", (dialog, which) -> {
//                    sendBackKey();
//                })
                .setCancelable(false)
                .create();
        Window window = alertDialog.getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_TOAST);
        window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.y = 200;
//        attributes.dimAmount = 0f;
        window.setAttributes(attributes);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
    }

    private void hideTipDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void scrollEnd() {
        isOver = true;
        requestScroll = false;
        canStart = false;
        pageIndex = 0;
        hideTipDialog();
        sendBackKey();

        long num = addMemberNum;
        ready();
        if (isOver) {
            showOverDialog(num);
            RAccessibilityActivity.saveLastClick(num);
            isOver = false;
            if (RAccessibilityActivity.isDebugKey()) {
                RAccessibilityActivity.cleanCodeInfo();
            }
        }
    }

    @Override
    public void onInterrupt() {
        //当服务要被中断时调用.会被调用多次
        e((new Exception()).getStackTrace()[1].getMethodName());

        hideTipDialog();
    }

    private void e(String msg) {
        if (DEBUG) {
            Log.e(TAG + " " + Thread.currentThread().getId(), msg);
        }
    }

    private String getDebugInfo(AccessibilityEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("getEventType:");
        stringBuilder.append(event.getEventType());
//        stringBuilder.append("\n");
//        stringBuilder.append("getAction:");
//        stringBuilder.append(event.getAction());
        stringBuilder.append("\n");
        stringBuilder.append("getEventTime:");
        stringBuilder.append(event.getEventTime());
        stringBuilder.append("\n");
        stringBuilder.append("getText:");
        stringBuilder.append(event.getText());
        stringBuilder.append("\n");
        stringBuilder.append("getContentDescription:");
        stringBuilder.append(event.getContentDescription());
        stringBuilder.append("\n");
        stringBuilder.append("getItemCount:");
        stringBuilder.append(event.getItemCount());
        stringBuilder.append("\n");
        stringBuilder.append("getWindowId:");
        stringBuilder.append(event.getWindowId());
        stringBuilder.append("\n");
        stringBuilder.append("getPackageName:");
        stringBuilder.append(event.getPackageName());
        stringBuilder.append("\n");
        stringBuilder.append("getClassName:");
        stringBuilder.append(event.getClassName());
//        stringBuilder.append("\n");
//        stringBuilder.append("getRootInActiveWindow getPackageName:");
//        stringBuilder.append(getRootInActiveWindow().getPackageName());
//        stringBuilder.append("\n");
//        stringBuilder.append("getRootInActiveWindow getClassName:");
//        stringBuilder.append(getRootInActiveWindow().getClassName());
        stringBuilder.append("\n");
        stringBuilder.append("getRootInActiveWindow getText:");
        stringBuilder.append(getRootInActiveWindow().getText());

        AccessibilityNodeInfo source = event.getSource();
        stringBuilder.append("\n");
        stringBuilder.append("getSource getChildCount:");
        stringBuilder.append(source.getChildCount());

        return stringBuilder.toString();
    }

    private synchronized void incrementPageIndex() {
        pageIndex++;
    }

    class ScrollHandler extends Handler {
        //        public static final String MSG_SCROLL = "scroll";
        public static final int MSG_SCROLL = 900;
        public static final int MSG_BACK = 901;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_SCROLL) {
                //收到滚动消息
                if (requestScroll) {
                    //收到消息后,requestScroll如果还是为true,表示并没有滚动,即滚动结束了
                    scrollEnd();
                }
            } else if (msg.what == MSG_BACK) {
//                e(curPage + " ...太长... " + msg.arg1);
                if (msg.arg1 == curPage && pageIndex == msg.arg2) {//如果在一定时间之内,还停留在需要返回的界面,并且此事没有切换过其他界面,那么就发送Back事件
                    e(curPage + " 此界面停留太长...自动返回 " + pageIndex);
                    String page = "";
                    switch (curPage) {
                        case PAGE_DETAIL:
                            page = "详细资料";
                            break;
                        case PAGE_FJDR:
                            page = "附近的人";
                            break;
                        case PAGE_HOME:
                            page = "首页";
                            break;
                        case PAGE_SAY_HI:
                            page = "聊天";
                            break;
                        default:
                            break;
                    }
                    showToast("'" + page + "' 此界面停留过长,自动返回.");
                    performGlobalAction(GLOBAL_ACTION_BACK);
                }
//                performGlobalAction(GLOBAL_ACTION_BACK);
//                try {
//                    Thread.sleep(SLEEP_TIME);
//                    e("返回1次");
//                } catch (InterruptedException e) {
//                }
//                performGlobalAction(GLOBAL_ACTION_BACK);
//                try {
//                    Thread.sleep(SLEEP_TIME);
//                    e("返回2次");
//                } catch (InterruptedException e) {
//                }
//                performGlobalAction(GLOBAL_ACTION_BACK);
//                e("返回3次");
            }
        }
    }
}
