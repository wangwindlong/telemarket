package com.example.telemarket.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.telemarket.R;
import com.example.telemarket.adapter.SendMsgGvadapter;
import com.example.telemarket.bean.CustomInfo;
import com.example.tool.Threads;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public class SendMessageActivity extends BaseActivity {
    private ArrayList<CustomInfo> customList = new ArrayList<CustomInfo>();
    private String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private EditText messageEt;
    private GridView customGv;
    private String body, currNo;//短信内容,当前号码
    private int sentCount = 0;//记录每次群发时的已发短信条数
    private int deliveredCount = 0;//记录每次群发时的已接收短信条数
    private LinkedHashSet<String> addr = new LinkedHashSet<String>();   //存放电话号码的set，用于去除相同号码
    private long threadId;            // 发送短信的threadid

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_message);
        if (getIntent().getSerializableExtra("customList") != null){
           customList = (ArrayList<CustomInfo>) getIntent().getSerializableExtra("customList");
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initView() {
        customGv = (GridView) findViewById(R.id.contactGridView);
        customGv.setAdapter(new SendMsgGvadapter(this,customList));
        customGv.setOnItemClickListener(new GridViewItemClickListener());
        messageEt = (EditText) findViewById(R.id.editMessageEt);
        Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        Button sendBtn = (Button) findViewById(R.id.send_message_btn);
        cancelBtn.setOnClickListener(new BottomOnClickLstener('c'));
        sendBtn.setOnClickListener(new BottomOnClickLstener('s'));

    }

    class BottomOnClickLstener implements View.OnClickListener {
        char type;

        BottomOnClickLstener(char type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case 'c': {
                    finish();
                    break;
                }
                case 's': {
                    sentCount = 0;
                    body = messageEt.getText().toString().trim();
                    for (int i = 0; i < customList.size(); i++) {
                        addr.add(customList.get(i).getTelephone());
                    }
                    if (body == null || "".equals(body)) {
                        Toast.makeText(SendMessageActivity.this, "请填写短信内容！", Toast.LENGTH_SHORT).show();
                        messageEt.setHint("请在此处添加短信内容");
                        return;
                    }
                    threadId = Threads.getOrCreateThreadId(SendMessageActivity.this, addr);//通过这个方法往SMs中插入数据时 threads表也会更新
                    sendSMS(addr, body, threadId);
                    break;
                }
            }
        }
    }

    private void sendSMS(LinkedHashSet<String> phone, final String body, long threadId) {
        SmsManager msg = SmsManager.getDefault();
//        将数据插入数据库
        ContentValues cv = new ContentValues();
        for (final String pno : phone) {
            //已成功发送后的接收器
            Intent sentIntent = new Intent(SENT_SMS_ACTION + pno);
            sentIntent.putExtra("phoneNo", pno);
            PendingIntent sendPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);
            registerReceiver(new SentBroadcastReceiver(), new IntentFilter(SENT_SMS_ACTION + pno));
            // 对方已接受后的接收器
            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION + pno);
            sentIntent.putExtra("phoneNo", pno);
            PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);
            registerReceiver(new DeliverdBroadcastReceiver(), new IntentFilter(DELIVERED_SMS_ACTION + pno));

            Toast.makeText(SendMessageActivity.this, "正在发送短信，请稍侯！", Toast.LENGTH_SHORT).show();
            msg.sendTextMessage(pno, null, body, sendPI, deliverPI);
        }
    }

    class DeliverdBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            // 判断短信是否接收成功
            String currNo = intent.getStringExtra("phoneNo");
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(SendMessageActivity.this, "短信已被" + (currNo == null ? "" : currNo) + "接收！", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(SendMessageActivity.this, "短信对方未接收到！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(SendMessageActivity.this, "短信对方未接收到！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SentBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            // 判断短信是否发送成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    String currNo = intent.getStringExtra("phoneNo");
                    Toast.makeText(SendMessageActivity.this, "第" + sentCount++ + "条短信：" + body + " to " + currNo + "发送成功！", Toast.LENGTH_LONG).show();
                    ContentValues values = new ContentValues();
//                    values.put("thread_id", id);
                    values.put("address", currNo);
                    values.put("body", body);
                    getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getBaseContext(), "发送失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class GridViewItemClickListener implements GridView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            if (position < customList.size()) {
                new AlertDialog.Builder(SendMessageActivity.this).setTitle("确认删除吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                customList.remove(position);
                                customGv.setAdapter(new SendMsgGvadapter(SendMessageActivity.this,customList));
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            } else {
                Toast.makeText(getBaseContext(), "增加其他联系人！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}