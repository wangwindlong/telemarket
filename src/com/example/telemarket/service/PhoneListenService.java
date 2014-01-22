package com.example.telemarket.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-15
 * Time: 上午9:32
 * To change this template use File | Settings | File Templates.
 */
public class PhoneListenService extends Service {
    private TelephonyManager tManager;
    private ContentObserver callObserver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        tManager = (TelephonyManager) getSystemService
                (Context.TELEPHONY_SERVICE);
        // 注册监听
        callObserver = new ContactsContentObserver(this);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callObserver);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(callObserver);
        super.onDestroy();
    }

    //    捕捉特定Uri引起的数据库的变化
    private class ContactsContentObserver extends ContentObserver {
        Context context;

        public ContactsContentObserver(Context context) {
            super(new Handler());
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            // 大数据操作 在线程中 进行
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
            getCalllogs(context);
//                    mHandler.sendEmptyMessage(0);
//                }
//            });
            super.onChange(selfChange);
        }
    }

    public void getCalllogs(Context context) {
        Cursor cur = null;
        String msg = null;
        String number;
        String name;
        String type;
        String duration;
        try {
            cur = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER + " limit 1");
            if (null != cur) {
                if (cur.moveToFirst()) {
                    number = cur.getString(cur.getColumnIndex("number"));
                    name = cur.getString(cur.getColumnIndex("name"));

                    type = cur.getString(cur.getColumnIndex("type"));
                    duration = cur.getString(cur.getColumnIndex("duration"));

                    switch (Integer.parseInt(type)) {
                        case CallLog.Calls.INCOMING_TYPE:
                            // 来电
                            Toast.makeText(context, number + "来电话", Toast.LENGTH_SHORT).show();
                            System.out.println(number + "*******来电话*******");
                            if (Integer.parseInt(duration) == 0) {
                                Toast.makeText(context, "已拒接电话：" + number, Toast.LENGTH_SHORT).show();
                                System.out.println(number + "*******这个是拒接电话*******");
                                type = "4";
                            }
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            // 已拨电话
                            Toast.makeText(context, "已拨电话：" + number, Toast.LENGTH_SHORT).show();
                            System.out.println(number + "*******这个是已拨电话*******");
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            // 未接
                            Toast.makeText(context, "未接电话：" + number, Toast.LENGTH_SHORT).show();
                            System.out.println(number + "*******这个是未接电话*******");
                            break;

                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }
}
