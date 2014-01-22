package com.example.telemarket.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.telemarket.AppConfig;
import com.example.telemarket.R;
import com.example.tool.DateTool;
import com.example.tool.JsonTool;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-20
 * Time: 下午5:43
 * To change this template use File | Settings | File Templates.
 */
public class SignInActivity extends BaseActivity {
    private static final String TAG = "SignInActivity";
    private LocationClient mLocClient;
    private TextView qdLocation;
    private MyLocationListenner myListener = new MyLocationListenner();
    private LocationData locData = null;
    private MyLocationOverlay myLocationOverlay = null;
    private MapView workAttenMapview = null;
    private MapController mMapController = null;
    private MKMapViewListener mMapListener = null;
    private String localAddress = null;
    private BMapManager mBMapManager;
    private SharedPreferences sharedPreferences;
    private Button refreshBtn;
    private TextView signTv, qdDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initEngineManager(getApplication());
        setContentView(R.layout.sign);

        sharedPreferences = getSharedPreferences(AppConfig.MODEL_USER, MODE_PRIVATE);
        initView();
    }

    void initView() {
        TextView backTv = (TextView) findViewById(R.id.sign_back_tv);
        signTv = (TextView) findViewById(R.id.sing_tv);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "提交当前位置", Toast.LENGTH_SHORT).show();
            }
        });
        TextView qdName = (TextView) findViewById(R.id.sign_name);
        qdName.setText(sharedPreferences.getString("user_nick", "系统管理员"));
        qdDate = (TextView) findViewById(R.id.work_atten_date);
        qdLocation = (TextView) findViewById(R.id.work_atten_location);
        refreshBtn = (Button) findViewById(R.id.work_atten_refresh);
        qdDate.setText(DateTool.getDateTimeNow());

        workAttenMapview = (MapView) findViewById(R.id.work_atten_mapview);
        mMapController = workAttenMapview.getController();
        workAttenMapview.setLongClickable(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        workAttenMapview.getController().setZoom(14);
        workAttenMapview.getController().enableClick(true);
        workAttenMapview.setBuiltInZoomControls(true);
        mMapListener = new MapUpdateOnClickListener();
        workAttenMapview.regMapViewListener(mBMapManager, mMapListener);
        myLocationOverlay = new MyLocationOverlay(workAttenMapview);
        locData = new LocationData();
        myLocationOverlay.setData(locData);
        workAttenMapview.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        workAttenMapview.refresh();
        refreshBtn.setOnClickListener(new RefreshAddressOnClickListener());
    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(AppConfig.strKey, new MyGeneralListener())) {
            Log.e(TAG, "BMapManager  初始化错误!");
//            CommonToast.showToast(ExitApplication.getInstance().getApplicationContext(),
//                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }

    class RefreshAddressOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mLocClient.requestLocation();
        }
    }

    class MapUpdateOnClickListener implements MKMapViewListener {
        @Override
        public void onMapMoveFinish() {
        }

        @Override
        public void onClickMapPoi(MapPoi mapPoi) {
        }
    }


    @Override
    protected void onPause() {
        workAttenMapview.onPause();
        if (mBMapManager != null) {
            mBMapManager.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        workAttenMapview.onResume();
        if (mBMapManager != null) {
            mBMapManager.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mLocClient != null) {
            mLocClient.stop();
        }
        if (mBMapManager != null) {
            mBMapManager.destroy();
            mBMapManager = null;
        }
        workAttenMapview.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        workAttenMapview.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        workAttenMapview.onRestoreInstanceState(savedInstanceState);
    }


    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    private class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Log.e(TAG, "您的网络出错啦!");
//                CommonToast.showToast(ExitApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
//                        Toast.LENGTH_LONG).show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Log.e(TAG, "输入正确的检索条件!");
//                CommonToast.showToast(ExitApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
//                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                Log.e(TAG, "请在 CommonTool.java文件输入正确的授权Key！!");
                //授权Key错误：
//                CommonToast.showToast(ExitApplication.getInstance().getApplicationContext(),
//                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            workAttenMapview.refresh();
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)), null);
            String location_xy = location.getLatitude() + "," + location.getLongitude();
            new MapListenerAsyncTask(location_xy).execute();

        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    class MapListenerAsyncTask extends AsyncTask<String, Integer, Boolean> {
        String location_xy;

        MapListenerAsyncTask(String location_xy) {
            this.location_xy = location_xy;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            qdLocation.setText("正在获取你的位置信息....");
            qdLocation.setTextColor(Color.BLUE);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (localAddress == null) {
                qdLocation.setText("无法获取到当前位置");
                qdLocation.setTextColor(Color.BLACK);
                refreshBtn.setVisibility(View.VISIBLE);
            } else {
                qdLocation.setText(localAddress);
                qdLocation.setTextColor(Color.BLACK);
                qdDate.setText(DateTool.getDateTimeNow());
                refreshBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            if (location_xy != null) {
                //1、坐标系
                //1、坐标系
                Map<String, String> baidu_param = AppConfig.getBaiduParam(location_xy);
                //2、转换地址
                JSONObject parentJson = JsonTool.doPost(AppConfig.BAI_DU_CONVERT_URL, baidu_param);
                if (null != parentJson) {
                    try {
                        String status = parentJson.getString("status");
                        if (status.equals("0")) {
                            JSONObject jsonObject = parentJson.getJSONObject("result");
                            localAddress = jsonObject.getString("formatted_address");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
    }
}