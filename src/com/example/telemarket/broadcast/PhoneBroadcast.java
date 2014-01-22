package com.example.telemarket.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.example.telemarket.AppConfig;
import com.example.telemarket.service.PhoneListenService;
import com.example.tool.SqliteUtil;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-14
 * Time: 下午12:15
 * 监听电话状态
 */
public class PhoneBroadcast extends BroadcastReceiver {
    /**
     * 手机没有通话，在一般的状态值
     */
    public static final int CALL_TYPE_IDEL = 0;
    /**
     * 手机通话状态值
     */
    public static final int CALL_TYPE_CALLING = 1;
    /**
     * 手机响铃状态值
     */
    public static final int CALL_TYPE_RING = 2;

    /**
     * 当前手机通话状态值
     */
    private int currentState = CALL_TYPE_IDEL;
    /**
     * 手机原来的通话状态值
     */
    private int oldState = CALL_TYPE_IDEL;

    /**
     * 数据库Helper类，只是帮助我们存取状态值，可以改成用文件流实现
     */
//    private SqliteUtil sqlUtil = null;
    private MyPhoneListener listener;
    private Handler mHandler = new Handler();
    private SharedPreferences sharedPreferences;
    private ContentObserver callObserver;

    @Override
    public void onReceive(Context context, Intent intent) {
        //intent.getAction()可以得到当前的事件，这里我们监听的是android.permission.READ_PHONE_STATE
        //这时候intent.getAction().equals("android.permission.READ_PHONE_STATE");
        //这是为了一个监听器可以监听多个事件，只要我们进行一次equals()判断进行不同的处理即可

        //进行细节上的监控，我们需要操作TelephonyManager，为它设置监听器，他就给我们反馈
        //拿到系统的TelephonyManager
        TelephonyManager tpManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        sharedPreferences = context.getSharedPreferences(AppConfig.PHONE_CONFIG, Context.MODE_WORLD_WRITEABLE);

        listener = new MyPhoneListener(context);//创建监听器
        tpManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//设置监听器

        if (!AppConfig.isServiceRunning(context,"PhoneListenService")){
            context.startService(new Intent(context, PhoneListenService.class));
            Toast.makeText(context, "启动PhoneListenService服务", Toast.LENGTH_SHORT).show();
        }
        // 注册监听
//        callObserver = new ContactsContentObserver(context);
//        context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callObserver);
    }

    private class MyPhoneListener extends PhoneStateListener {
        Context context;

        private MyPhoneListener(Context context) {
            this.context = context;
        }

        @Override//当电话状态发生改变的时候，系统会调用这个方法
        public void onCallStateChanged(int state, String incomingNumber) {
            //首先取得当前的状态值
            oldState = sharedPreferences.getInt(AppConfig.PHONE_STATE, TelephonyManager.CALL_STATE_IDLE);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    currentState = CALL_TYPE_IDEL;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    currentState = CALL_TYPE_CALLING;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    currentState = CALL_TYPE_RING;
                    break;
            }

            //当通话状态发生改变
            if (oldState == CALL_TYPE_RING && currentState == CALL_TYPE_CALLING) {
                Toast.makeText(context, "接听" + incomingNumber + "的来电", Toast.LENGTH_LONG).show();
                System.out.println("接听");
            } else if (oldState == CALL_TYPE_CALLING && currentState == CALL_TYPE_IDEL) {
                Toast.makeText(context, "挂断" + incomingNumber + "的电话", Toast.LENGTH_LONG).show();
                System.out.println("挂断");
            }
            if (oldState == CALL_TYPE_IDEL && currentState == CALL_TYPE_CALLING) {
                Toast.makeText(context, "拨号给" + incomingNumber, Toast.LENGTH_LONG).show();
                System.out.println("拨号");
            }

//            context.getContentResolver().unregisterContentObserver(callObserver);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(AppConfig.PHONE_STATE, currentState);
            editor.commit();
        }
    }

    //    捕捉特定Uri引起的数据库的变化
    private class ContactsContentObserver extends ContentObserver {
        Context context;

        public ContactsContentObserver(Context context) {
            super(mHandler);
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
            cur = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date desc limit 1");
            if (null != cur) {
                if (cur.moveToFirst()) {
                    number = cur.getString(cur.getColumnIndex("number"));
                    name = cur.getString(cur.getColumnIndex("name"));

                    type = cur.getString(cur.getColumnIndex("type"));
                    duration = cur.getString(cur.getColumnIndex("duration"));

                    switch (Integer.parseInt(type)) {
                        case 1:
                            // 来电
                            Toast.makeText(context, number + "来电话", Toast.LENGTH_SHORT).show();
                            System.out.println(number + "*******来电话*******");
                            if (Integer.parseInt(duration) == 0) {
                                Toast.makeText(context, "已拒接电话：" + number, Toast.LENGTH_SHORT).show();
                                System.out.println(number + "*******这个是拒接电话*******");
                                type = "4";
                            }
                            break;
                        case 2:
                            // 已拨电话
                            Toast.makeText(context, "已拨电话：" + number, Toast.LENGTH_SHORT).show();
                            System.out.println(number + "*******这个是已拨电话*******");
                            break;
                        case 3:
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
