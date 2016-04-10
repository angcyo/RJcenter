package com.angcyo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RsenAccessibilityService extends AccessibilityService {
    public static final boolean DEBUG = true;
    public static final String TAG = "AccessibilityService";
    public static final String TEXT_WEIXIN = "微信";
    public static final String TEXT_FJDR = "附近的人";
    public static final String TEXT_DETAIL = "详细资料";
    public static final String TEXT_TXL = "通讯录";
    public static final String TEXT_FX = "发现";
    public static final String TEXT_ME = "我";
    public static final String TEXT_LIST_ITEM = "以内";
    public static final String TEXT_DZH = "打招呼";
    public static final String TEXT_SAY_HI = "加为朋友";
    public static final String TEXT_SAY_HI2 = "添加朋友请求已发送";
    private AlertDialog alertDialog;
    private long index = 0;
    private boolean needBack = false;//添加好友之后,请求返回.
    private int memberNumIndex = 0;//一屏需要添加的好友数量,用于控制滚动ListView
    private long addMemberNum = 0;//执行了多少次添加朋友操作
    private List<AccessibilityNodeInfo> lastItemList;//保存最后一次附近人的列表信息,用于判断是否全部添加了好友.
    private boolean isOver = false;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // setServiceInfo();//这个方法同样可以实现xml中的配置信息
        e((new Exception()).getStackTrace()[1].getMethodName());

        createTipDialog();
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
//            createTipDialog();

            if (isWeiXinHomePage(event)) {
//                if (!needBack) {
////                    主页
//                    jumpToFaXianPage(event);
//                jumpToFJDRPage(event);
//                }
                needBack = false;
                hideTipDialog();

                if (isOver) {
                    showOverDialog();
                    isOver = false;
                }

                addMemberNum = 0;
            } else if (isWeiXinFJDRPage(event)) {
                //附近的人
                e("已经进入\"附近的人\"界面");
                if (addMemberNum < 1) {
                    showTipMsg("接下来,就交给我吧...");
                }

                AccessibilityNodeInfo listNode = source.getChild(0).getChild(1);
                if (listNode.getChildCount() > 0) {
                    needBack = false;
                    List<AccessibilityNodeInfo> itemList = listNode.findAccessibilityNodeInfosByText(TEXT_LIST_ITEM);
                    if (memberNumIndex == 0) {
                        //showItemListInfo(itemList);
                    }

                    if (itemList.size() > 0) {
                        if (memberNumIndex >= itemList.size()) {
                            //需要滚动屏幕了
                            if (isNodeListFoot(itemList)) {
                                //所有联系人添加完毕
                                needBack = true;
                                isOver = true;
                                e("列表已全部添加完毕,共:" + addMemberNum);
                            } else {
                                lastItemList = itemList;
                                listNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                e("请求滚动(前进)...");
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                }
                                memberNumIndex = 0;
                                List<AccessibilityNodeInfo> itemList2 = listNode.findAccessibilityNodeInfosByText(TEXT_LIST_ITEM);
                                //showItemListInfo(itemList2);
                                itemList2.get(memberNumIndex++).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        } else {
                            itemList.get(memberNumIndex++).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            } else if (isWeiXinDetailPage(event)) {
                //详细资料
                e("详细资料");
                if (!needBack) {
                    if (!clickButton(source, TEXT_DZH)) {
                        needBack = true;
                    }
                }
            } else if (isWeiXinSayHiPage(event)) {
                //打招呼,聊天界面
                e("聊天界面");
                if (!needBack) {
                    clickButton(source, TEXT_SAY_HI);
                    addMemberNum++;
                    showToast();
                }
                e("请求返回...隐藏键盘");
                sendBackKey();
                needBack = true;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
//            else {
//                needBack = true;
//            }

            if (needBack) {
                sendBackKey();
                e("请求返回Back");
            }
        }

//        alertDialog.setMessage(event.getText() + " -- " + String.valueOf(event.getEventType()) + " -- " + index++);
    }

    private void showOverDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("完美结束")
                .setMessage("上一次的战果是:" + addMemberNum + "次")
                .setPositiveButton("恭喜发财", null)
                .create();

        Window window = alertDialog.getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_TOAST);
        window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        alertDialog.show();
    }

    private void showItemListInfo(List<AccessibilityNodeInfo> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            AccessibilityNodeInfo info = itemList.get(i);
            e(info.getParent().getChild(0).getText() + " -- " + info.getText().toString());
        }
    }

    /**
     * 返回值表示,是否调用单击事件
     */
    private boolean clickButton(AccessibilityNodeInfo source, String buttonText) {
        List<AccessibilityNodeInfo> nodeInfos = source.findAccessibilityNodeInfosByText(buttonText);
        for (AccessibilityNodeInfo info : nodeInfos) {
            if (info.getClassName().equals(Button.class.getName())) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }
        return false;
    }

    private void showTipMsg(String msg) {
        alertDialog.setMessage(msg);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void showToast() {
//        Toast toast = T.initToast(this, "");
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
//        T.show(this, "已为申请好友:" + addMemberNum + "次");
        alertDialog.setMessage("已为您申请好友:" + addMemberNum + "次,\n如果发现程序打瞌睡了,熄屏亮屏一次,就可以激活啦!");
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    /**
     * 判断是否已经滚动到了附近的人的底部
     */
    private boolean isNodeListFoot(List<AccessibilityNodeInfo> list) {
        if (lastItemList == null || list == null) {
            return false;
        }

        return lastItemList.containsAll(list);
    }

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
                if (nodeText.equals(text) && TextView.class.getName().equals(info.getClassName())) {
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

    private void sendBackKey() {
        performGlobalAction(GLOBAL_ACTION_BACK);
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
        attributes.gravity = Gravity.CENTER;
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
}
