package com.angcyo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;

import com.angcyo.sample.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;
import com.rsen.drawable.CircleAnimDrawable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDemoActivity extends RBaseActivity implements BluetoothDiscover.IBluetoothDiscoverListener {

    public static Logger log = LoggerFactory.getLogger("bluetooth.ui");
    BluetoothAdapter defaultAdapter;
    String msg;
    long scanMode = 0;
    BluetoothDeviceAdapter leftAdapter;
    BluetoothDeviceAdapter rightAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_bluetooth_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        leftAdapter = new BluetoothDeviceAdapter(this);
        rightAdapter = new BluetoothDeviceAdapter(this);
        mViewHolder.r(R.id.leftRecyclerView).setAdapter(leftAdapter);
        mViewHolder.r(R.id.rightRecyclerView).setAdapter(rightAdapter);

        fixRecyclerViewWidth(mViewHolder.r(R.id.leftRecyclerView));
        fixRecyclerViewWidth(mViewHolder.r(R.id.rightRecyclerView));

        //test drawable
//        mViewHolder.v(R.id.supportBluetoothView).setBackground(new CircleAnimDrawable());
        mViewHolder.v(R.id.leftButtonView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackground(new CircleAnimDrawable(BluetoothDemoActivity.this).setPosition(CircleAnimDrawable.POS_LEFT));

                textTextClock();
            }
        });
//        mViewHolder.v(R.id.centerButtonView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setBackground(new CircleAnimDrawable().setPosition(CircleAnimDrawable.POS_CENTER));
//            }
//        });
        mViewHolder.v(R.id.centerButtonView).setBackground(new CircleAnimDrawable(this).setPosition(CircleAnimDrawable.POS_CENTER));
        mViewHolder.v(R.id.rightButtonView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackground(new CircleAnimDrawable(BluetoothDemoActivity.this).setPosition(CircleAnimDrawable.POS_RIGHT));
            }
        });

        mViewHolder.v(R.id.leftRadioView).setBackground(createRadioBackground(CircleAnimDrawable.POS_LEFT));
        mViewHolder.v(R.id.centerRadioView).setBackground(createRadioBackground(CircleAnimDrawable.POS_CENTER));
        mViewHolder.v(R.id.rightRadioView).setBackground(createRadioBackground(CircleAnimDrawable.POS_RIGHT));

        YoYo.with(Techniques.Landing).delay(300).playOn(mViewHolder.v(R.id.centerButtonView));

        //TextClock测试
//        textTextClock();
    }

    private void textTextClock() {
        final TextClock textClock = (TextClock) mViewHolder.v(R.id.textClockView);
//        try {
//            final Method setShowCurrentUserTime = TextClock.class.getMethod("setShowCurrentUserTime", boolean.class);
//            setShowCurrentUserTime.invoke(textClock, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        textClock.setFormat24Hour("yyyy-MM-dd");
    }

    private Drawable createRadioBackground(int position) {
        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{android.R.attr.state_checked}, new CircleAnimDrawable(this).setPosition(position));
        return listDrawable;
    }

    private void fixRecyclerViewWidth(RecyclerView recyclerView) {
        final ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.width = getScreenWidth() / 2;
        recyclerView.setLayoutParams(layoutParams);
    }

    @Override
    protected void initViewData() {
        defaultAdapter = BluetoothHelper.getDefaultAdapter(this);
        if (defaultAdapter != null) {
            //已经匹配过的设备
            final Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();
            for (BluetoothDevice device : bondedDevices) {
                rightAdapter.addLastItem(new BluetoothDeviceBean(device));
            }

        }
        log.info("initViewData");

        BluetoothDiscover.instance().addDiscoverListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothDiscover.instance().removeDiscoverListener(this);
    }

    public void supportBluetooth(View view) {
        if (defaultAdapter != null && !TextUtils.isEmpty(defaultAdapter.getAddress())) {
            msg = "支持蓝牙";
        } else {
            msg = "不支持蓝牙";
        }
        showMsg(view);
    }

    public void enableBluetooth(View view) {
        if (defaultAdapter.isEnabled()) {
            msg = "蓝牙已打开";
        } else {
            msg = "蓝牙已关闭";
        }
        showMsg(view);
    }

    public void openBluetooth(View view) {
        if (defaultAdapter.isEnabled()) {
            msg = "断开蓝牙";
            defaultAdapter.disable();
        } else {
            msg = "打开蓝牙";
            defaultAdapter.enable();

            //使用系统交互的方式打开蓝牙
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivity(enableBtIntent);
        }
        showMsg(view);
    }

    public void addressBluetooth(View view) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(defaultAdapter.getName());//蓝牙名称
        stringBuilder.append(" ");
        stringBuilder.append(defaultAdapter.getAddress());//蓝牙地址
        stringBuilder.append("\nMode:");
        stringBuilder.append(defaultAdapter.getScanMode());//扫描模式
        stringBuilder.append(" State:");
        stringBuilder.append(defaultAdapter.getState());//当前状态
        stringBuilder.append(" Scan:");
        stringBuilder.append(defaultAdapter.isDiscovering());//是否正在扫描
        msg = stringBuilder.toString();
        showMsg(view);
    }

    public void startDiscovery(View view) {
        if (defaultAdapter.isDiscovering()) {
            defaultAdapter.cancelDiscovery();
            msg = "取消扫描...";
        } else {
            leftAdapter.resetData(new ArrayList<>());
            defaultAdapter.startDiscovery();
            msg = "开始扫描...";
        }
        showMsg(view);
    }

    public void openBluetoothSettingActivity(View view) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);
    }

    public void setScanMode(View view) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);//可被发现的持续时间
        startActivity(intent);

        scanMode++;
    }

    /**
     * 获取联系人的方法测试
     */
    public void queryContacts(View view) {
        method1();
//        method2();
        //method3();
//        new Thread() {
//            @Override
//            public void run() {
//                test();
//            }
//        }.start();
    }

    private void method1() {
        new Thread() {
            @Override
            public void run() {
                final ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, ContactsContract.Contacts._ID + " desc limit 100 offset " + 0);

                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);

                if (cursor != null) {
                    log.info("查询到联系人的数量:{}", cursor.getCount());
                    int count = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                            String contactId = cursor.getString(contactIdIndex);//联系人id

                            /*联系人电话信息*/
                            Cursor contactsCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                                    null, null);
                            if (contactsCursor != null) {
                                int phoneIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);//获取联系人 号码的索引
                                int nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);//获取 名字 所在列的索引
                                if (contactsCursor.moveToFirst()) {
                                    do {
                                        final String phoneNumber = contactsCursor.getString(phoneIndex);//联系人的号码
                                        final String name = contactsCursor.getString(nameIndex);//联系人名字
                                        log.info("id:{} 名字:{} 号码:{}",
                                                contactId, name, phoneNumber);
                                    } while (contactsCursor.moveToNext());
                                }
                                contactsCursor.close();
                            }

                            /*联系人邮箱信息*/
                            Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                                    null, null);

                            if (emailCursor != null) {
                                int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                                if (emailCursor.moveToFirst()) {
                                    do {
                                        final String email = emailCursor.getString(emailIndex);//联系人名字
                                        log.info("id:{} 邮箱:{} ", contactId, email);
                                    } while (emailCursor.moveToNext());
                                }
                                emailCursor.close();
                            }

                            count++;
                        } while (cursor.moveToNext());
                        cursor.close();
                        log.info("查询结束.{} 条", count);
                    }
                }
            }
        }.start();
    }

    private void method2() {
        new Thread() {
            @Override
            public void run() {
                final ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);

                if (cursor != null) {
                    log.info("查询到联系人的数量:{}", cursor.getCount());
                    int count = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                            String contactId = cursor.getString(contactIdIndex);//联系人id

                            Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                                    new String[]{ContactsContract.Data._ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.LABEL,
                                            ContactsContract.Contacts.DISPLAY_NAME,
                                            ContactsContract.CommonDataKinds.Organization.COMPANY
                                    },
                                    ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                                    new String[]{String.valueOf(contactId)}, null);

                            if (dataCursor != null && dataCursor.moveToFirst()) {
                                int index1 = dataCursor.getColumnIndex(ContactsContract.Data._ID);
                                int index2 = dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int index3 = dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                                int index4 = dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
                                int index5 = dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                                int index6 = dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY);
                                do {
                                    String str1 = dataCursor.getString(index1);
                                    String str2 = dataCursor.getString(index2);
                                    String str3 = dataCursor.getString(index3);
                                    String str4 = dataCursor.getString(index4);
                                    String str5 = dataCursor.getString(index5);
                                    String str6 = dataCursor.getString(index6);
                                    log.info("{} {} {} {} {} {}", str1, str2, str3, str4, str5, str6);
                                } while (dataCursor.moveToNext());

                                dataCursor.close();
                            }

                            count++;
                        } while (cursor.moveToNext());
                        cursor.close();
                        log.info("查询结束.{} 条", count);
                    }
                }
            }
        }.start();
    }

    private void method3() {
        new Thread() {
            @Override
            public void run() {
                final ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);

                if (cursor != null) {
                    log.info("查询到联系人的数量:{}", cursor.getCount());
                    int count = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                            String contactId = cursor.getString(contactIdIndex);//联系人id

                            /*获取联系人名字*/
                            Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Nickname.NAME},
                                    ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
                                    new String[]{String.valueOf(contactId)}, null);

                            if (dataCursor != null) {
                                if (dataCursor.moveToFirst()) {
                                    int index = dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME);
                                    do {
                                        String str = dataCursor.getString(index);
                                        log.info("{}", str);
                                    } while (dataCursor.moveToNext());
                                }
                                dataCursor.close();
                            }

                            count++;
                        } while (cursor.moveToNext());
                        cursor.close();
                        log.info("查询结束.{} 条", count);
                    }
                }
            }
        }.start();
    }

    private void method4() {
//        ContactsContract.DataColumns.Dta
//        ContactsContract.DataCol
//

        final ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                    String contactId = cursor.getString(contactIdIndex);//联系人id

                    //联系人的名字

                } while (cursor.moveToNext());
            }
        }
    }

    /**
     * 根据MIMETYPE类型, 返回对应联系人的data1字段的数据
     */
    private String getData1(final ContentResolver contentResolver, String contactId, final String mimeType) {
        StringBuilder stringBuilder = new StringBuilder();

        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.DATA1},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "='" + mimeType + "'",
                new String[]{String.valueOf(contactId)}, null);
        if (dataCursor != null) {
            if (dataCursor.moveToFirst()) {
                do {
                    stringBuilder.append(dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1)));
                    stringBuilder.append("_");
                } while (dataCursor.moveToNext());
            }
            dataCursor.close();
        }

        return stringBuilder.toString();
    }

    private void test() {
        String[] MIMETYPES = new String[]{
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,//联系人名称
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,//联系人电话(可能包含多个)
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,//邮箱(多个)
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,//公司
                ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,//备注
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,//地址
                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,//网站
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE
        };

        final ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                String contactId = cursor.getString(contactIdIndex);//联系人id
                log.error("{} {}", contactId, contactIdIndex);

                for (String MIME : MIMETYPES) {
//                    Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
//                            null,
//                            ContactsContract.Data.CONTACT_ID + "=?" + " AND "
//                                    + ContactsContract.Data.MIMETYPE + "='" + MIME + "'",
//                            new String[]{String.valueOf(contactId)}, null);
//                    log.info("{} {} {}", MIME, dataCursor.getCount(), dataCursor.getColumnCount());
//                    dataCursor.close();

                    log.info("{} {} {}", contactId, MIME, getData1(contentResolver, contactId, MIME));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public void showMsg(View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceDiscover(BluetoothDevice device) {
        leftAdapter.addLastItem(new BluetoothDeviceBean(device));
    }

    static class BluetoothDeviceBean {
        public BluetoothDevice device;
        public String name;
        public String address;
        public int cls;

        public BluetoothDeviceBean(BluetoothDevice device) {
            this.device = device;
            this.name = device.getName();
            this.address = device.getAddress();
            this.cls = device.getBluetoothClass().getDeviceClass();
        }
    }

    class BluetoothDeviceAdapter extends RBaseAdapter<BluetoothDeviceBean> {

        public BluetoothDeviceAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.activity_bluetooth_adapter_item;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, BluetoothDeviceBean bean) {
            if (position % 2 == 0) {
                holder.v(R.id.rootLayout).setBackgroundResource(R.drawable.v_btn_default_white_selector);
            } else {
                holder.v(R.id.rootLayout).setBackgroundResource(R.drawable.v_btn_default_blue_selector);
            }

            if (bean.device.getBondState() == BluetoothDevice.BOND_BONDED) {
                holder.v(R.id.rootLayout).setBackgroundResource(R.drawable.v_btn_default_red_selector);
            }

            holder.tV(R.id.nameView).setText(bean.name);
            holder.tV(R.id.addressView).setText(bean.address);
            holder.tV(R.id.clsView).setText(String.valueOf(bean.cls));

            holder.v(R.id.rootLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.device.createBond()) {
                        log.info("连接至蓝牙:{} 开始.", bean.name);
                    } else {
                        log.info("连接至蓝牙:{} 失败.", bean.name);
                    }
                }
            });
        }
    }
}
