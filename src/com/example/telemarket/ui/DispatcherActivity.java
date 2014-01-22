package com.example.telemarket.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-16
 * Time: 下午8:01
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> activityClass;

        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(prefs.getString("lastActivity", MainActivity.class.getName()));
        } catch(ClassNotFoundException ex) {
            activityClass = MainActivity.class;
        }

        startActivity(new Intent(this, activityClass));
    }
}
