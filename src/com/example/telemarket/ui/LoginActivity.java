package com.example.telemarket.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.telemarket.AppConfig;
import com.example.telemarket.R;
import com.example.telemarket.bean.ModelResult;
import com.example.telemarket.bean.user.User;
import com.example.telemarket.dialog.LoadingDialog;
import com.example.tool.JsonTool;
import com.example.tool.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-15
 * Time: 下午5:19
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    private EditText nameEt, pwdEt;
    private String username;
    private String password, imei;
    private String result;
    private CheckBox rememberCb, loginCb;
    private LoadingDialog loadDialog;
    private SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        loadDialog = new LoadingDialog(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        sharedPreferences = getSharedPreferences(AppConfig.MODEL_USER, MODE_WORLD_WRITEABLE | MODE_WORLD_READABLE);
        nameEt = (EditText) findViewById(R.id.login_name_et);
        pwdEt = (EditText) findViewById(R.id.login_pwd_et);
        rememberCb = (CheckBox) findViewById(R.id.remember_cb);
        loginCb = (CheckBox) findViewById(R.id.load_cb);

        if (sharedPreferences.getBoolean("isRememberMe", false)) {
            nameEt.setText(sharedPreferences.getString("user_name", ""));
            pwdEt.setText(sharedPreferences.getString("passwd", ""));
            rememberCb.setChecked(true);
        }
//        AppConfig.upgradeRootPermission(getPackageCodePath());

        Button loginBtn = (Button) findViewById(R.id.login_dl_xt_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameEt.getText().toString().trim();
                password = pwdEt.getText().toString().trim();
                if (StringUtil.isBlank(username)) {
                    nameEt.setHint(R.string.login_name_hint);
                    return;
                }
                if (StringUtil.isBlank(password)) {
                    pwdEt.setHint(R.string.login_pwd_hint);
                    return;
                }
                if (!AppConfig.isNetworkAvailable(LoginActivity.this)) {
                    AppConfig.showNetWorkDialog(LoginActivity.this);
                } else {
//                startActivity(new Intent(LoginActivity.this, InspectionFlowActivity.class));
                    new LoginTask().execute();
                }
            }
        });
//        startActivity(new Intent(this, MainActivity.class));
    }

    class LoginTask extends AsyncTask<String, String, ModelResult<User>> {
        //取消一个正在执行的任务,onCancelled方法将会被调用
        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadDialog.dismiss();
        }

        @Override
        protected void onPostExecute(ModelResult<User> result) {
            loadDialog.dismiss();
            if (result.isReturnResult()) {
                //创建用户
                User user = result.getPersisObject();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id", String.valueOf(user.getUserId()));
                editor.putString("user_name", username);
                editor.putString("user_nick", user.getNickName());
                editor.putString("zone_type", user.getZoneType());
                editor.putString("passwd", password);
                editor.putString("scode", ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId());
                editor.putBoolean("isRememberMe", rememberCb.isChecked());
                editor.putBoolean("isLogin", loginCb.isChecked());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            } else {
                if (result.getCode() == -2) {
                    Toast.makeText(LoginActivity.this, result.getPersisObject().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.network_bugeili, Toast.LENGTH_SHORT).show();
                }
            }
        }

        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog.show();
            Toast.makeText(LoginActivity.this, "username:" + username + ",passeord:" + password, Toast.LENGTH_SHORT).show();
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected ModelResult<User> doInBackground(String... params) {
            createDatabase();
            Map<String, String> param = AppConfig.getParam(LoginActivity.this);
            String url = param.get("NETWORKURL") + "/ms_agent";
            User user = new User();
            ModelResult<User> modelResult = new ModelResult<User>();
            try {
//                http://veryhoo.com/SCenter/ms_agent?methodName=Login&username=admin&password=111&scode=1111
                param.put("methodName", "Login");
                param.put("username", username);
                param.put("password", password);
                modelResult = JsonTool.doPostResult(url, param);
                if (!modelResult.isReturnResult()) {
                    return modelResult;
                }
                JSONObject jsonObject = modelResult.getJsonObject();
                boolean IsSuccess = Boolean.valueOf(jsonObject.getString("IsSuccess"));
                String message = jsonObject.getString("Message");
                user.setMessage(message);
                if (IsSuccess) {
                /*{"IsSuccess":"true", "Message":"登录成功", "ClsType":"ModelUser", "CustomJson":"", "Value":[{
                    'ClassType':'ModelUser', 'Id':'19', 'SortId':'1', 'UserName':'admin', 'Password':'313131', 'Xm':
                    '系统管理员', 'Xb':'女', 'Csny':'1970-1-1T0:00:00', 'Xl':'', 'Gddh':'', 'Jtzz':'', 'Jtdh':'', 'Sfzhm':
                    '', 'Sj':'18965423575', 'Email':'123@qq.com', 'Qq':'', 'Zw':'系统管理员', 'Szbm':'', 'Szyhz':
                    '0', 'Type':'48', 'Sfscfw':'1', 'Bz':'', 'Ssbm':'1', 'InviteCode':
                    '4A18F43C222A4564982AC7A6C3F7E94B', 'UpperName':'ADMIN', 'MsgFileLimit':'2048', 'MsgImageLimlt':
                    '200', 'AccptStramgerIm':'1', 'IsTemp':'0', 'DiskSize':'200', 'RegisterTime':
                    '2010-9-8T23:03:38', 'HomePage':'', 'HeadImg':'', 'RoleIds':'15', 'DeptId':'-999', 'DeptName':
                    '未分配部门', 'Domain':'', 'Province':'', 'City':'', 'County_District':'', 'Ower_Sign':'', 'ZP':''
                }]}*/
                    JSONArray jsonArray = jsonObject.getJSONArray("Value");
                    JSONObject childObject = jsonArray.getJSONObject(0);
                    user.setId(childObject.getInt("Id"));
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setNickName(childObject.getString("Xm"));
                    user.setUserId(childObject.getInt("Id"));
                    user.setSex(childObject.getString("Xb"));
                    user.setPost(childObject.getString("Zw"));
                    user.setEmail(childObject.getString("Email"));
                    String headImage = childObject.getString("ZP");
                    user.setRoleIds(childObject.getString("RoleIds"));
                    user.setDomain(childObject.getString("Domain"));
                    user.setProvince(childObject.getString("Province"));
                    user.setCity(childObject.getString("City"));
                    user.setCounty(childObject.getString("County_District"));
                    user.setDeptId(Integer.parseInt(childObject.getString("DeptId")));
                    user.setDeptName(childObject.getString("DeptName"));
                    user.setOwerSign(childObject.getString("Ower_Sign"));
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("user", StringUtil.serialize(user));
                    edit.commit();
                } else {
                    modelResult.setReturnResult(false);
                    modelResult.setCode(-2);
                }
            } catch (Exception e) {
                modelResult.setReturnResult(false);
                modelResult.setCode(-2);
                String message = getResources().getResourceName(R.string.user_password_error);
                user.setMessage(message);
            }
            modelResult.setPersisObject(user);
            return modelResult;
        }
    }

    //创建数据库
    private void createDatabase() {
        try {
            String path = AppConfig.DATA_FILE_PATH;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(path, AppConfig.DATABASE_FILENAME);
            if (!file.exists()) {
                InputStream inputStream = getResources().openRawResource(R.raw.telemarket);
                FileOutputStream fos = new FileOutputStream(file);
                int count;
                byte[] buffer = new byte[8192];
                while ((count = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}