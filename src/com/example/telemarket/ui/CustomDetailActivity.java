package com.example.telemarket.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.example.telemarket.AppConfig;
import com.example.telemarket.AppManager;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.fragment.CustomDetailFollowFragment;
import com.example.telemarket.fragment.CustomDetailInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-9
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
public class CustomDetailActivity extends FragmentActivity {
    private CustomInfo customDetail;
    private TextView[] topBarTvs;
    private ViewPager viewPager;
    private int currIndex = 0;
    private ArrayList<Fragment> views = new ArrayList<Fragment>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.custom_detail);
        customDetail = (CustomInfo) getIntent().getSerializableExtra("customDetail");
        initTopBottomTool();
        initViewPager();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.custom_detail_viewpager);
        CustomDetailInfoFragment infoFragment = new CustomDetailInfoFragment(0, customDetail);
        CustomDetailFollowFragment followFragment = new CustomDetailFollowFragment(1, customDetail);
            views.add(infoFragment);
            views.add(followFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        topBarTvs[0].setTextColor(getResources().getColor(R.color.white));
        viewPager.setOnPageChangeListener(new PageChangeLisener());
    }

    private void initTopBottomTool() {
        TextView titleTv = (TextView) findViewById(R.id.custom_detail_title_tv);
        titleTv.setText(customDetail.getName());
        TextView infoTv = (TextView) findViewById(R.id.custom_info_tv);
        infoTv.setOnClickListener(new TopBarOnClickListener(0));
        TextView followTv = (TextView) findViewById(R.id.custom_follow_tv);
        followTv.setOnClickListener(new TopBarOnClickListener(1));
        topBarTvs = new TextView[]{infoTv, followTv};
        TextView backTv = (TextView) findViewById(R.id.custom_back_tv);
        TextView editTv = (TextView) findViewById(R.id.custom_edit_tv);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView callTv = (TextView) findViewById(R.id.send_call_tv);
        TextView sendMsgTv = (TextView) findViewById(R.id.send_msg_tv);
        callTv.setOnClickListener(AppConfig.getCallListener(this, customDetail.getTelephone()));
        sendMsgTv.setOnClickListener(AppConfig.getMsgListener(this, customDetail.getTelephone(),"尊敬的" + customDetail.getName() + ",您好!"));
    }

    class TopBarOnClickListener implements View.OnClickListener {
        int num;

        TopBarOnClickListener(int num) {
            this.num = num;
        }

        @Override
        public void onClick(View v) {
//            topBarTvs[currIndex].setTextColor(getResources().getColor(R.color.gray));
//            topBarTvs[num].setTextColor(getResources().getColor(R.color.white));
            viewPager.setCurrentItem(num);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> views;

        public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> views) {
            super(fm);
            this.views = views;
        }

        @Override
        public Fragment getItem(int arg0) {
            return views.get(arg0);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        public List<Fragment> getViewList() {
            return views;
        }

        public void addTail(Fragment fragment) {
            views.add(fragment);
        }
    }

    class PageChangeLisener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            topBarTvs[currIndex].setTextColor(getResources().getColor(R.color.gray));
            topBarTvs[arg0].setTextColor(getResources().getColor(R.color.white));
            currIndex = arg0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}