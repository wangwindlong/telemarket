package com.example.telemarket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.bean.ProductInfo;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-17
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class ProductDetailActivity extends BaseActivity {
    private String productId;
    private ProductInfo productInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        initActionBar();
        initView();

    }

    private void initActionBar() {
        if (getIntent().getSerializableExtra("productInfo") != null) {
            productInfo = (ProductInfo) getIntent().getSerializableExtra("productInfo");
        }
        //标题栏
        TextView backTv = (TextView) findViewById(R.id.custom_back_tv);
        TextView titleTv = (TextView) findViewById(R.id.main_title_tv);
        titleTv.setText(productInfo.getProductName());
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        TextView sendSumTv = (TextView) findViewById(R.id.send_sum_tv);
        TextView sendEmailTv = (TextView) findViewById(R.id.send_email_tv);
        sendSumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, CustomListActivity.class);
                intent.putExtra("productId", productInfo.getProductId());
                startActivity(intent);
            }
        });
        sendEmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetailActivity.this, "发送Email", Toast.LENGTH_SHORT).show();
            }
        });

        ((TextView) findViewById(R.id.product_name_tv)).setText(productInfo.getProductName());
        ((TextView) findViewById(R.id.product_sex_tv)).setText(productInfo.getProductNum());    //产品编号
        ((TextView) findViewById(R.id.product_num_tv)).setText(productInfo.getProductSum());    //产品保额
        ((TextView) findViewById(R.id.product_company_tv)).setText(productInfo.getProductType());           //所属类型
        ((TextView) findViewById(R.id.product_position_tv)).setText(productInfo.getProductInstrument());      //说明
        ((TextView) findViewById(R.id.product_telephone_tv)).setText(productInfo.getCookieTime());         //有效销售期
        ((TextView) findViewById(R.id.product_easyname_tv)).setText(productInfo.getProductDeadLine());      //保险期限
    }
}
