package com.example.telemarket.ui;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import com.example.telemarket.AppConfig;
import com.example.telemarket.AppManager;
import com.example.telemarket.R;
import com.example.telemarket.bean.user.User;
import com.example.telemarket.fragment.MainFragment;

import java.util.*;

public class MainActivity extends Activity implements MainFragment.DrawerControl {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private User user = null;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFragmentTitles, mFragmentImages;

    private int oldDrawerPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        user = AppConfig.getUser(this);

        mTitle = mDrawerTitle = getTitle();
        mFragmentTitles = getResources().getStringArray(R.array.drawer_title_array);
        mFragmentImages = getResources().getStringArray(R.array.fragment_icon_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        setDrawerItem();  //为drawer list 的每一项赋值，并添加点击事件
//        initActionBar(); //使actionbar可以控制nav drawer的开关
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public void ShowDrawer() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) mDrawerLayout.closeDrawer(mDrawerList);
        else mDrawerLayout.openDrawer(mDrawerList);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    /* Called whenever we call invalidateOptionsMenu() */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, mTitle);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mDrawerLayout.closeDrawer(mDrawerList);
                mDrawerList.setItemChecked(oldDrawerPosition, true);
            } else if (position > 0) {
                selectItem(position - 1);
                oldDrawerPosition = position;
            }
            if (false) {   //如果当drawer打开时，当前界面不是drawer中所包含的界面时，需要将高亮取消！！！
                mDrawerList.setItemChecked(0, true);
            }
        }
    }

    private void selectItem(int position) {
        Fragment fragment = AppConfig.getFragment(position);
        Bundle args = new Bundle();
        args.putInt(AppConfig.ARG_FRAGMENT_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerList.setItemChecked(position + 1, true);
        setTitle(mFragmentTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
//        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
//    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Pass any configuration change to the drawer toggls
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
    private void setDrawerItem() {
        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mFragmentImages.length && i < mFragmentTitles.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageName", getResources().getIdentifier(mFragmentImages[i], "drawable", getPackageName()));
            item.put("titleName", mFragmentTitles[i]);
            content.add(item);
        }
        ImageView drawerHeaderView = new ImageView(this);
        drawerHeaderView.setBackgroundColor(R.color.drawer_top);
        drawerHeaderView.setImageResource(R.drawable.navigation_back);

        mDrawerList.addHeaderView(drawerHeaderView);
        SimpleAdapter drawerSimpleAdapter = new SimpleAdapter(this, content, R.layout.drawer_item,
                new String[]{"imageName", "titleName"}, new int[]{R.id.drawer_item_iv, R.id.drawer_item_tv});
        mDrawerList.setAdapter(drawerSimpleAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }


    private void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onBackPressed() {
        if (oldDrawerPosition != 1) {
            selectItem(0);
            oldDrawerPosition = 1;
            return;
        }
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        AppManager.getAppManager().AppExit(MainActivity.this);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}
