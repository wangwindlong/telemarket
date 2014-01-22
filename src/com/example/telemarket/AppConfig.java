package com.example.telemarket;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import com.example.telemarket.bean.user.User;
import com.example.telemarket.fragment.MainFragment;
import com.example.telemarket.fragment.OtherFragment;
import com.example.telemarket.ui.CustomListActivity;
import com.example.telemarket.ui.NewApplyActivity;
import com.example.telemarket.ui.ProductListActivity;
import com.example.tool.StringUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 上午8:08
 * 保存app的配置信息
 */
public class AppConfig {
    public static final String ARG_FRAGMENT_NUMBER = "fragment_number";
    public static final String PHONE_CONFIG = "phone_config";
    public static final String PHONE_STATE = "phone_state";

    public static final String PROJECT_NAME = "telemarket";
    public static final String SHJI_PATH = android.os.Environment.getDataDirectory().getAbsolutePath() + "/" + PROJECT_NAME;
    public static final String EXTERNAL_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PROJECT_NAME;//路径
    public static final String EXTERNAL_IMAGE_PATH = EXTERNAL_PATH + "/Image";//路径
    public static final String SHJI_IMAGE_PATH = SHJI_PATH + "/Image";

    public static final String DATA_FILE_PATH = existSDcard() ? EXTERNAL_PATH : SHJI_PATH;
    public static final String DATA_IMAGE_FILE = existSDcard() ? EXTERNAL_IMAGE_PATH : SHJI_IMAGE_PATH;
    public static final String DATA_VIODE_FILE = DATA_FILE_PATH + "/Video";
    public static final String DATA_XC_IMAGE_FILE = DATA_FILE_PATH + "/Temp";
    public static final String DATA_CACHE_IMAGE_FILE = DATA_FILE_PATH + "/Cache";
    public static final String DATABASE_FILENAME = "telemarket.db";//数据库名

    public static final String SYS_CONFIG = "sys_config"; //用于保存系统配置信息
    public static final String MODEL_USER = "model_user"; //用于保存用户相关信息
    public static final String NETWORKURL = "network_url";
    public static final String NetWorkUrl = "http://veryhoo.com/SCenter/";

    public static final String strKey = "35E91564CC53146C0BA59471CDE66D452A5E044C";
    public static final String BAI_DU_CONVERT_URL = "http://api.map.baidu.com/geocoder/v2/?ak=1ae92539e558a89eb0128c0179f8867a";
    public static final String TOOL_URL = "http://www.veryhoo.com/Portal/mobile/calculation.aspx";


    public static Fragment getFragment(int number) {
        Fragment fragment = null;
        switch (number) {
            case 0:    //主界面
                fragment = new MainFragment();
                break;
            case 1:    //系统设置
                fragment = new OtherFragment();
                break;
            case 2:    //系统帮助
                fragment = new OtherFragment();
                break;
            case 3:    //检查更新
                fragment = new OtherFragment();
                break;
            case 4:    //联系我们
                fragment = new OtherFragment();
                break;
            default:
                fragment = new OtherFragment();
        }

        return fragment;
    }

    public static void mainGvSelected(int position, Context context) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(context, CustomListActivity.class);
                break;
            case 1:
                intent.setClass(context, ProductListActivity.class);
                break;
            case 2:
                intent.setClass(context, NewApplyActivity.class);
                break;
            case 3:

            case 4:

            case 5:

            case 6:
                intent.setClass(context, CustomListActivity.class);
                break;
        }
        context.startActivity(intent);
    }

    public static User getUser(Context context) {
        User user = null;
        SharedPreferences sp = context.getSharedPreferences(AppConfig.MODEL_USER, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        try {
            user = StringUtil.deSerialization(sp.getString("user", null));
        } catch (ClassNotFoundException e) {
            user = new User();
        } catch (IOException e) {
            user = new User();
        }
        return user;
    }

    public static boolean upDateUserLocal(Context context, String passwd) {
        User user;
        SharedPreferences sp = context.getSharedPreferences(AppConfig.MODEL_USER, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        try {
            user = StringUtil.deSerialization(sp.getString("user", null));
            if (user != null) {
                user.setPassword(passwd);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("user", StringUtil.serialize(user));
                edit.putString("passwd",passwd);
                edit.commit();
                return true;
            }
        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
        }
        return false;
    }

    public static void upDateLogin(Context context, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences(AppConfig.MODEL_USER, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",isLogin);
        editor.putBoolean("isRememberMe", isLogin);
        editor.commit();
    }

    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
//            if (serviceList.get(i).service.getClassName().equals(className)) {
            if (serviceList.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static Map<String, String> getParam(Context context) {
        Map<String, String> param = new HashMap<String, String>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SYS_CONFIG, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        if (StringUtil.isBlank(sharedPreferences.getString(NETWORKURL, ""))) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(NETWORKURL, NetWorkUrl);
            editor.commit();
        }
        param.put("NETWORKURL", sharedPreferences.getString(NETWORKURL, NetWorkUrl));
//        String deviceNumber = getDeviceNumber(context);
//        String deviceNumber = "123123123";
//        //加密 (真实项目)
////        String tran_Esoter = AESTool.encrypt(deviceNumber + "~" + "~" + DateTool.getdatetimewithoutsecNow());
//        //不加密（测试）
//        String tran_Esoter = deviceNumber + "~" + "顶顶顶" + "~" + DateTool.getdatetimewithoutsecNow();
//        param.put("code", tran_Esoter);
        return param;
    }

    public static Map<String, String> getBaiduParam(String location) {
//        &location=39.983424,116.322987
        Map<String, String> param = new HashMap<String, String>();
        param.put("callback", "renderReverse");
        param.put("location", location);
        param.put("output", "json");
        param.put("pois", "1");
        // JSONObject parentJson = JsonTool.doPost(CommonTool.BAI_DU_CONVERT_URL, baidu_param);
        //   String status = parentJson.getString("status");
        //  String address = "未找到地址";
        //  if (status.equals("0")) {
        //       JSONObject jsonObject = parentJson.getJSONObject("result");
        //       address = jsonObject.getString("formatted_address");
        //   }
        return param;
    }

    public static void showNetWorkDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("无网络状态").setMessage("手机目前暂无网络，请您检查你的网络设置")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    //判断是否存在网络
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if ((info[i].getState() == NetworkInfo.State.CONNECTED) || (info[i].getState() == NetworkInfo.State.CONNECTING)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断SD卡是否存在
    public static boolean existSDcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static View.OnClickListener getCallListener(final Context context, final String telephone) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone)));
            }
        };
    }

    public static View.OnClickListener getMsgListener(final Context context, final String telephone, final String content) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + telephone));
                it.putExtra("sms_body", content);
                context.startActivity(it);
            }
        };
    }
}
