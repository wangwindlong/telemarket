package com.example.telemarket.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.telemarket.AppConfig;
import com.example.telemarket.R;
import com.example.telemarket.adapter.ProductListAdapter;
import com.example.telemarket.bean.ModelResult;
import com.example.telemarket.bean.ProductInfo;
import com.example.telemarket.dialog.LoadingDialog;
import com.example.tool.JsonTool;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 下午12:04
 * To change this template use File | Settings | File Templates.
 */
public class ProductListActivity extends BaseActivity {
    private ListView productLv;
    private ProductListAdapter productAdapter;
    private LoadingDialog loadDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        loadDialog = new LoadingDialog(this);
        //标题栏
        TextView backTv = (TextView) findViewById(R.id.custom_back_tv);
        TextView titleTv = (TextView) findViewById(R.id.main_title_tv);
        titleTv.setText(R.string.product_list_title);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner productSp = (Spinner) findViewById(R.id.product_sp);
        productSp.setAdapter(ArrayAdapter.createFromResource(this, R.array.product_array, android.R.layout.simple_spinner_item));
        productLv = (ListView) findViewById(R.id.fragment_custom_lv);
        productLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("productInfo",  productAdapter.getListItems().get(position));
                startActivity(intent);
            }
        });
        new ProductAsyncTask().execute("");
    }

    class ProductAsyncTask extends AsyncTask<String, String, ModelResult<ProductInfo>> {
        //取消一个正在执行的任务,onCancelled方法将会被调用
        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadDialog.dismiss();
        }

        @Override
        protected void onPostExecute(ModelResult<ProductInfo> result) {
            loadDialog.dismiss();
            if (result.isReturnResult()) {
                List<ProductInfo> beanList = result.getPersisObjectList(ProductListActivity.this);
                productAdapter = new ProductListAdapter(ProductListActivity.this, beanList, R.layout.product_list_item);
                productLv.setAdapter(productAdapter);
            } else {
                Toast.makeText(ProductListActivity.this, R.string.network_bugeili, Toast.LENGTH_SHORT).show();
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
        protected ModelResult<ProductInfo> doInBackground(String... params) {
            Map<String, String> param = AppConfig.getParam(ProductListActivity.this);
            String url = param.get("NETWORKURL") + "/d_agent";
            List<ProductInfo> productInfos = new ArrayList<ProductInfo>();
            ModelResult<ProductInfo> modelResult = new ModelResult<ProductInfo>();
            try {
//                http://veryhoo.com/SCenter/ms_agent?methodName=Login&username=admin&password=111&scode=1111
                param.put("methodName", "LoadDatas");
                param.put("tID", "685");
                modelResult = JsonTool.doPostResult(url, param);
                if (!modelResult.isReturnResult()) {
                    return modelResult;
                }
                JSONObject jsonObject = modelResult.getJsonObject();
                JSONArray parentJson = jsonObject.getJSONObject("Value").getJSONArray("data");
                for (int i = 0; i < parentJson.length(); i++) {
                    JSONObject childJson = parentJson.getJSONObject(i);
                    ProductInfo productInfo = new ProductInfo();
                    productInfo.setProductId(childJson.getString("id"));      //产品id
                    productInfo.setProductSum(childJson.getString("CPBE"));   //产品保额
                    productInfo.setProductName(childJson.getString("CPMC"));  //产品名称
                    productInfo.setCookieTime(childJson.getString("YXXSQ")); //销售有效期
                    productInfo.setProductNum(childJson.getString("CPBH"));  //产品编号
                    productInfo.setProductType(childJson.getString("CPLX"));  //产品类型
                    productInfo.setProductInstrument(childJson.getString("SM"));  //说明
                    productInfo.setProductDeadLine(childJson.getString("BXQX"));  //保险期限
                    productInfo.setProductPhoto(childJson.getString("XCC"));  //宣传册
                    productInfos.add(productInfo);
                }
                modelResult.setReturnResult(true);
                modelResult.setPersisObjectList(productInfos);
            } catch (Exception e) {
                modelResult.setReturnResult(false);
                modelResult.setCode(-2);
            }
            return modelResult;
        }
    }


    private List<ProductInfo> getProductList() {
        List<ProductInfo> listItems = new ArrayList<ProductInfo>();
        for (int i = 0; i < 20; i++) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCookieTime((20 + i) + "个月");
            productInfo.setProductId("" + i);
            productInfo.setProductName("都市白领理财保险");
            productInfo.setProductSum(i + "W/年");
            listItems.add(productInfo);
        }
        return listItems;
    }
}