package com.example.telemarket.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.telemarket.service.PhoneListenService;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-14
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class LaunchReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent tIntent = new Intent(context , PhoneListenService.class);
//        启动指定Service
        context.startService(tIntent);
    }
}
