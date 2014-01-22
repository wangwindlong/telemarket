package com.example.telemarket.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.telemarket.AppConfig;
import com.example.telemarket.AppManager;
import com.example.telemarket.R;
import com.example.telemarket.adapter.MainGvadapter;
import com.example.telemarket.bean.ModelResult;
import com.example.telemarket.bean.user.User;
import com.example.telemarket.dialog.ChangePasswordDialog;
import com.example.telemarket.ui.NewCustomActivity;
import com.example.telemarket.ui.SendMessageActivity;
import com.example.telemarket.ui.SignInActivity;
import com.example.tool.DateTool;
import com.example.tool.JsonTool;
import com.example.tool.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 上午8:08
 * To change this template use File | Settings | File Templates.
 */
public class MainFragment extends Fragment {
    private String month;
    private TextView signedTv, totalTv, restTv;
    //    private VerticalScrollTextView verticalTv;
    private TextView verticalTv;
    private SharedPreferences sharedPreferences;
    private int currentpage = 1;
    private int pagesize = 10;

    public interface DrawerControl {
        public void ShowDrawer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        int i = getArguments().getInt(AppConfig.ARG_FRAGMENT_NUMBER);
        month = DateTool.getdateNowCn();
        sharedPreferences = getActivity().getSharedPreferences(AppConfig.MODEL_USER, Context.MODE_WORLD_READABLE);
        String fragmentTitle = getResources().getStringArray(R.array.fragment_title_array)[i];
//            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                    "drawable", getActivity().getPackageName());     //根据名字获取到图片 的id
        setGridViewItem(rootView);  //为gridview 的每一项赋值，并添加点击事件
        initView(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new MainAsyncTask().execute("");
    }

    private void initView(View rootView) {
        View settingBtn = (View) rootView.findViewById(R.id.main_tool_settings_iv);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DrawerControl) getActivity() != null) ((DrawerControl) getActivity()).ShowDrawer();
            }
        });
        View newCustom = (View) rootView.findViewById(R.id.new_custom_rl);
        View sendMsg = (View) rootView.findViewById(R.id.send_message_rl);
        newCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewCustomActivity.class));
            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendMessageActivity.class));
            }
        });

        TextView planTv = (TextView) rootView.findViewById(R.id.main_plan_tv);
        TextView nameTv = (TextView) rootView.findViewById(R.id.name_tv);
        TextView monthTv = (TextView) rootView.findViewById(R.id.main_month_tv);
        signedTv = (TextView) rootView.findViewById(R.id.main_signed_tv);  //已签订单
        totalTv = (TextView) rootView.findViewById(R.id.main_total_tv);    //任务总量
        restTv = (TextView) rootView.findViewById(R.id.main_rest_tv);      //剩余指标
        verticalTv = (TextView) rootView.findViewById(R.id.announcement_content_tv);      //公告
        TextView signTv = (TextView) rootView.findViewById(R.id.main_sign_tv);
        TextView changePwdTv = (TextView) rootView.findViewById(R.id.change_passwd_tv);
        TextView logoutTv = (TextView) rootView.findViewById(R.id.logout_tv);

        nameTv.setText(sharedPreferences.getString("user_nick", "未知用户"));
        monthTv.setText(month.substring(0, 8));
        planTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), NeedToDoActivity.class));
                Toast.makeText(getActivity(), "该功能正在开发中", Toast.LENGTH_SHORT).show();
            }
        });
        signTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });
        //修改密码
        changePwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog dialog = new ChangePasswordDialog(getActivity());
                dialog.show();
            }
        });
        //注销退出
        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("确认要注销退出吗？").setMessage("注销退出后将不会保存用户名和密码！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AppConfig.upDateLogin(getActivity(), false);
                                AppManager.getAppManager().AppExit(getActivity());
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
        });
    }


    private class GridViewItemClickListener implements GridView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AppConfig.mainGvSelected(position, getActivity());
        }
    }

    private void setGridViewItem(View rootView) {
        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        String[] itemImageName = getResources().getStringArray(R.array.main_image_array);
        String[] itemTitleName = getResources().getStringArray(R.array.main_title_array);
        for (int i = 0; i < 6; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageName", getResources().getIdentifier(itemImageName[i], "drawable", getActivity().getPackageName()));
            item.put("titleName", itemTitleName[i]);
            content.add(item);
        }
        GridView mainGv = (GridView) rootView.findViewById(R.id.main_gv);
        MainGvadapter mainGvadapter = new MainGvadapter(getActivity(), content, mainGv);
//        SimpleAdapter gvSimpleAdapter = new SimpleAdapter(getActivity(), content, R.layout.main_gv_item,
//                new String[]{"imageName", "titleName"}, new int[]{R.id.griditem_pic, R.id.griditem_title});
        mainGv.setAdapter(mainGvadapter);
        mainGv.setOnItemClickListener(new GridViewItemClickListener());
    }

    class MainAsyncTask extends AsyncTask<String, String, ModelResult<MainData>> {
        //取消一个正在执行的任务,onCancelled方法将会被调用
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(ModelResult<MainData> result) {
            if (result.isReturnResult()) {
                MainData mainData = result.getPersisObject();
                String xsjh = mainData.getXsjh();
                String xsje = mainData.getXsje();
                xsje = StringUtil.deletePoint(xsje);
                xsjh = StringUtil.deletePoint(xsjh);
                if (StringUtil.isBlank(xsjh) || StringUtil.isBlank(xsje)) {
                    Toast.makeText(getActivity(), "很抱歉，未获得到本月销售计划", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        signedTv.setText(Html.fromHtml(getResources().getString(R.string.signed).replace("@signed", nf.format((float) Integer.parseInt(xsje) / 10000) + "万")));
                        totalTv.setText(Html.fromHtml(getResources().getString(R.string.total).replace("@total", nf.format((float) Integer.parseInt(xsjh) / 10000) + "万")));
                        restTv.setText(Html.fromHtml(getResources().getString(R.string.rest).replace("@rest", nf.format((float) (Integer.parseInt(xsjh)
                                - Integer.parseInt(xsje)) / 10000) + "万")));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "很抱歉，无法对当前销售额和计划进行数学运算", Toast.LENGTH_LONG).show();
                    }
                }
                String announcement = "";
                for (int i = 0; i < mainData.getAnnouncement().length; i++) {
                    announcement += mainData.getAnnouncement()[i] + "\n\n";
                }
                verticalTv.setText(announcement);
                verticalTv.setMovementMethod(ScrollingMovementMethod.getInstance());

            } else {
                Toast.makeText(getActivity(), result.getPersisObject().getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MainActivity", result.getPersisObject().getMessage());
            }
        }

        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected ModelResult<MainData> doInBackground(String... params) {
            Map<String, String> param = AppConfig.getParam(getActivity());
            String url = param.get("NETWORKURL") + "/d_agent";
            ModelResult<MainData> modelResult = new ModelResult<MainData>();
            MainData result = new MainData();
            try {
//                http://veryhoo.com/SCenter/ms_agent?methodName=Login&username=admin&password=111&scode=1111
                param.put("methodName", "LoadDatas");
                param.put("tID", "687");
                param.put("limit", String.valueOf(pagesize));
                param.put("start", String.valueOf((currentpage - 1) * pagesize));
//                param.put("sort", "FJSJ DESC");
                modelResult = JsonTool.doPostResult(url, param);
                if (!modelResult.isReturnResult()) {
                    return modelResult;
                }
                JSONObject jsonObject1 = modelResult.getJsonObject();
                if ("true".equals(jsonObject1.getString("IsSuccess"))) {
                    JSONArray parentJson = jsonObject1.getJSONObject("Value").getJSONArray("data");
                    int count = parentJson.length();
                    String[] announcements = new String[count > 3 ? 3 : count];
                    for (int i = 0; i < count && i < 3; i++) {
                        JSONObject childJson = parentJson.getJSONObject(i);
                        announcements[i] = childJson.getString("ZW") + "\n          ————" + childJson.getString("FJR") + "  " +
                                childJson.getString("FJSJ").substring(5);
                    }
                    result.setAnnouncement(announcements);
                } else {
                    modelResult.setReturnResult(false);
                    result.setMessage(jsonObject1.getString("Message"));
                }

            } catch (Exception e) {
                modelResult.setReturnResult(false);
                modelResult.setCode(-2);
                result.appendMessage(e.toString());
            }
            Map<String, String> param2 = new HashMap<String, String>();
            url = param.get("NETWORKURL") + "/b_agent";
            try {
                param2.put("methodName", "GetSalesStatistic");
                String types = getXSJH();
                param2.put("types", types);
                modelResult = JsonTool.doPostResult(url, param2);
                if (!modelResult.isReturnResult()) {
                    return modelResult;
                }
                JSONObject jsonObject2 = modelResult.getJsonObject();
                if ("true".equals(jsonObject2.getString("IsSuccess"))) {
                    JSONObject childJson = jsonObject2.getJSONObject("Value");
                    result.setXsjh(childJson.getString("XSJH"));
                    result.setXsje(childJson.getString("XSJL"));
                } else {
                    modelResult.setReturnResult(false);
                    result.appendMessage(jsonObject2.getString("Message"));
                }
            } catch (Exception e) {
                modelResult.setReturnResult(false);
                modelResult.setCode(-2);
                result.appendMessage(e.toString());
            }
            modelResult.setPersisObject(result);
            return modelResult;
        }
    }

    private String getXSJH() {
        try {
            JSONArray jsonArray = new JSONArray();
            String startDay = DateTool.getCurrMonthFirstDay();
            String endDay = DateTool.getCurrMonthLastDay();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("NAME", "XSJH");
            jsonObject1.put("FILTER", "TJZQ='月度计划' and FZRID=23");
            jsonObject1.put("START", startDay);
            jsonObject1.put("END", endDay);
            jsonArray.put(jsonObject1);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("NAME", "XSJL");
            jsonObject2.put("FILTER", "qdsj>='@start' and qdsj<='@end'".replace("@start", startDay).replace("@end", endDay));
            jsonArray.put(jsonObject2);
            return jsonArray.toString();
        } catch (Exception e) {
        }
        return "";
    }

    class MainData implements Serializable {
        private String xsjh;//销售计划
        private String xsje;//销售金额
        private String[] announcement;//公告列表
        private String message; //系统信息

        String getXsjh() {
            return xsjh;
        }

        void setXsjh(String xsjh) {
            this.xsjh = xsjh;
        }

        String getXsje() {
            return xsje;
        }

        void setXsje(String xsje) {
            this.xsje = xsje;
        }

        String[] getAnnouncement() {
            return announcement;
        }

        void setAnnouncement(String[] announcement) {
            this.announcement = announcement;
        }

        String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }

        void appendMessage(String message) {
            this.message += message;
        }
    }
}
