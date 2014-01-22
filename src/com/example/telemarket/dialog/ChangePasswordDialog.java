package com.example.telemarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.telemarket.AppConfig;
import com.example.telemarket.R;
import com.example.telemarket.bean.ModelResult;
import com.example.telemarket.bean.user.User;
import com.example.tool.JsonTool;
import com.example.tool.StringUtil;
import com.example.tool.VerifyTool;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-30
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class ChangePasswordDialog extends Dialog {
    private Button confirmTv, cancelTv;
    private EditText editpass_ori_et, editpass_new_et;
    private Context context;
    private LoadingDialog loadDialog;
    private User user;
    private String _password_ori, _password_new;

    public ChangePasswordDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        loadDialog = new LoadingDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_dialog);
        editpass_ori_et = (EditText) findViewById(R.id.editpass_ori);
        editpass_new_et = (EditText) findViewById(R.id.editpass_new);
        confirmTv = (Button) findViewById(R.id.confirm_tv);
        confirmTv.setOnClickListener(new confirmClickListener());
        cancelTv = (Button) findViewById(R.id.cancel_tv);
        cancelTv.setOnClickListener(new cancelClickListener());
    }

    class confirmClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //检查输入
            _password_ori = editpass_ori_et.getText().toString().trim();
            _password_new = editpass_new_et.getText().toString().trim();
            if (StringUtil.isBlank(_password_ori)) {
                Toast.makeText(getContext(), "请将旧、新密码填写完整", Toast.LENGTH_LONG).show();
                return;
            }
            user = AppConfig.getUser(context);
            if (!_password_ori.equals(user.getPassword())) {
                Toast.makeText(getContext(), "旧密码输入错误", Toast.LENGTH_LONG).show();
                return;
            }

            if (!VerifyTool.passWordCheck(_password_new)) {
                Toast.makeText(getContext(), "新密码需3位以上，包含字母和数字", Toast.LENGTH_LONG).show();
                return;
            }
            cancel();
//            try {
//                boolean IsSuccess = Boolean.valueOf(jsonObject.getString("IsSuccess"));
//                String message = jsonObject.getString("Message");
//                if (IsSuccess) {
//                    if (AppConfig.upDateUserLocal(context, _password_new)) {
//                        Toast.makeText(getContext(), "密码修改成功，本地数据已更新", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "密码修改成功:本地数据更新失败", Toast.LENGTH_SHORT).show();
//                    }
//                    cancel();
//                } else {
//                    Toast.makeText(getContext(), "密码修改失败:" + message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
//            }
            new ChangePasswdTask().execute("");
            dismiss();
        }
    }

    class ChangePasswdTask extends AsyncTask<String, String, ModelResult<Boolean>> {

        //取消一个正在执行的任务,onCancelled方法将会被调用
        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (loadDialog != null)
                loadDialog.dismiss();
        }

        @Override
        protected void onPostExecute(ModelResult<Boolean> result) {
            loadDialog.dismiss();
            if (result.isReturnResult()) {
                if (result.getPersisObject().booleanValue()) {
                    Toast.makeText(context, "密码修改成功，本地数据已更新", Toast.LENGTH_SHORT).show();
                    AppConfig.upDateUserLocal(context, _password_new);
                } else {
                    Toast.makeText(context, "密码修改失败,请稍侯再试", Toast.LENGTH_SHORT).show();
                }
            }
        }

        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog.show();
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected ModelResult<Boolean> doInBackground(String... params) {
            String uid = String.valueOf(user.getUserId());
            Map<String, String> param = AppConfig.getParam(context);
            String url = param.get("NETWORKURL") + "/ms_agent";
            param.put("methodName", "ChangePassword");
            param.put("sourcePassword", _password_ori);
            param.put("newPassword", _password_new);
            param.put("uID", uid);
            ModelResult<Boolean> modelResult = new ModelResult<Boolean>();
            try {
                modelResult = JsonTool.doPostResult(url, param);
                if (!modelResult.isReturnResult()) {
                    return modelResult;
                }
                JSONObject jsonObject = modelResult.getJsonObject();
                boolean IsSuccess = Boolean.valueOf(jsonObject.getString("IsSuccess"));
                String message = jsonObject.getString("Message");
                modelResult.setPersisObject(IsSuccess);

            } catch (Exception e) {
                modelResult.setPersisObject(false);
            }
            return modelResult;
        }
    }

    class cancelClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //进行登录操作
            cancel();
        }
    }
}
