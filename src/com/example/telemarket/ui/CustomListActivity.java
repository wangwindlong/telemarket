package com.example.telemarket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.jiekou.CheckAllSelectedInterface;
import com.example.telemarket.AppManager;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.fragment.CustomContactGroupFragment;
import com.example.telemarket.fragment.CustomContactLocalFragment;
import com.example.telemarket.fragment.CustomContactPoolFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class CustomListActivity extends FragmentActivity {
    private TextView[] topBarTvs;
    private ViewPager viewPager;
    private CheckBox checkBox;
    private int currIndex = 0;
    private ArrayList<Fragment> views = new ArrayList<Fragment>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.custom_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        TextView customPoolTv = (TextView) findViewById(R.id.custom_pool_tv);
        customPoolTv.setOnClickListener(new TopBarOnClickListener(0));
        TextView customGroupTv = (TextView) findViewById(R.id.my_custom_tv);
        customGroupTv.setOnClickListener(new TopBarOnClickListener(1));
        TextView localPhoneTv = (TextView) findViewById(R.id.local_contact_tv);
        localPhoneTv.setOnClickListener(new TopBarOnClickListener(2));
        topBarTvs = new TextView[]{customPoolTv, customGroupTv, localPhoneTv};

        Spinner customSp = (Spinner) findViewById(R.id.custom_sp);
        customSp.setAdapter(ArrayAdapter.createFromResource(this, R.array.custom_array, android.R.layout.simple_spinner_item));
        TextView backTv = (TextView) findViewById(R.id.custom_back_tv);
        TextView titleTv = (TextView) findViewById(R.id.main_title_tv);
        titleTv.setText(R.string.my_custom_title);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewPager();
        checkBox = (CheckBox) findViewById(R.id.select_all_cb);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkList(checkBox.isChecked());
            }
        });

        TextView newCustomTv = (TextView) findViewById(R.id.new_custom_tv);
        newCustomTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomListActivity.this, "功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        TextView sendMsgTv = (TextView) findViewById(R.id.send_message_tv);
        sendMsgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomListActivity.this, SendMessageActivity.class);
                intent.putExtra("customList", getCustomList());
                startActivity(intent);
            }
        });
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.mycustom_viewpager);
        views.add(CustomContactPoolFragment.getInstance(new CheckAllSelected()));
        views.add(new CustomContactGroupFragment());
        views.add(new CustomContactLocalFragment());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        topBarTvs[0].setTextColor(getResources().getColor(R.color.white));
        viewPager.setOnPageChangeListener(new PageChangeLisener());
    }

    class CheckAllSelected implements CheckAllSelectedInterface {
        @Override
        public void checkSelected(boolean isAllChecked) {
            checkBox.setChecked(isAllChecked);
        }
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

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }


    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
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
            if (currIndex == 0 && arg0 != 0) {
                ((CustomContactPoolFragment) views.get(currIndex)).closeActionMode();
            }
            topBarTvs[currIndex].setTextColor(getResources().getColor(R.color.gray));
            topBarTvs[arg0].setTextColor(getResources().getColor(R.color.white));
            currIndex = arg0;
            checkList(checkBox.isChecked());
        }
    }

    private ArrayList<CustomInfo> getCustomList() {
        switch (currIndex) {
            case 0: {
                return ((CustomContactPoolFragment) views.get(currIndex)).getSelectedCustom();
            }
            case 1: {
                return ((CustomContactGroupFragment) views.get(currIndex)).getSelectedCustom();
            }
            case 2: {
                return ((CustomContactLocalFragment) views.get(currIndex)).getSelectedCustom();
            }
            default:
                return new ArrayList<CustomInfo>();
        }
    }

    private void checkList(boolean isChecked) {
        switch (currIndex) {
            case 0: {
                ((CustomContactPoolFragment) views.get(currIndex)).selectAll(isChecked);
                break;
            }
            case 1: {
                ((CustomContactGroupFragment) views.get(currIndex)).selectAll(isChecked);
                break;
            }
            case 2: {
                ((CustomContactLocalFragment) views.get(currIndex)).selectAll(isChecked);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}