package com.example.telemarket.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.comparator.PinyinComparator;
import com.example.component.MachineLetterLv;
import com.example.jiekou.CheckAllSelectedInterface;
import com.example.telemarket.R;
import com.example.telemarket.adapter.CustomPoolListAdapter;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.ui.CustomDetailActivity;
import com.example.telemarket.ui.CustomListActivity;
import com.example.tool.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomContactPoolFragment extends Fragment {
    private List<CustomInfo> listItems;
    private CustomPoolListAdapter customListAdapter;
    private ModeCallback mCallback;              //多选模式的回调接口
    private ListView customLv;
    private boolean isMultipleList = false;      //是否是多选模式下
    private ActionMode acMode;                   //多选模式
    private static CheckAllSelectedInterface checkAllSelectedInterface;
    private static CustomContactPoolFragment poolFragment;
    private TextView retriOverlay;//漂浮的字符
    private OverlayThread retriOverlayThread; //漂浮字体的线程
    private MachineLetterLv letterListView;
    private Handler searchhandler;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    public static Fragment getInstance(CheckAllSelectedInterface checkAllSelectedInterface) {
        CustomContactPoolFragment.checkAllSelectedInterface = checkAllSelectedInterface;
        if (poolFragment == null) {
            poolFragment = new CustomContactPoolFragment();
        }
        return poolFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        mCallback = new ModeCallback();
        view = inflater.inflate(R.layout.fragment_custom, container, false);
        customLv = (ListView) view.findViewById(R.id.fragment_custom_lv);

        letterListView = (MachineLetterLv) view.findViewById(R.id.machineRetriLetter);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
//        customLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        customLv.setMultiChoiceModeListener(mCallback);
        customLv.setOnItemClickListener(new CustomItemClickListener());
        customLv.setOnItemLongClickListener(new CustomItemLongClickListener());
        searchhandler = new Handler();
        pinyinComparator = new PinyinComparator();
        retriOverlayThread = new OverlayThread();
        initOverlay(inflater);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listItems = getCustomList();
        // 根据a-z进行排序源数据
        Collections.sort(listItems, pinyinComparator);
        customListAdapter = new CustomPoolListAdapter(getActivity(), listItems, R.layout.custom_list_item, checkAllSelectedInterface);
        customLv.setAdapter(customListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
//        new MainAsyncTask().execute("");
    }

    //漂浮的检索字
    private void initOverlay(LayoutInflater inflater) {
        retriOverlay = (TextView) inflater.inflate(R.layout.retri_overlay, null);
        retriOverlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(retriOverlay, lp);
    }

    private class LetterListViewListener implements MachineLetterLv.OnTouchingLetterChangedListener {
        @Override
        public void onTouchingLetterChanged(final String s) {
            //该字母首次出现的位置
            int position = customListAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                customLv.setSelection(position);
            }
            retriOverlay.setText(s);
            retriOverlay.setVisibility(View.VISIBLE);
            searchhandler.removeCallbacks(retriOverlayThread);
            searchhandler.postDelayed(retriOverlayThread, 1500);
        }
    }


    private List<CustomInfo> getCustomList() {
        List<CustomInfo> listItems = new ArrayList<CustomInfo>();

        for (int i = 0; i < 10; i++) {
            CustomInfo customInfo = new CustomInfo();
            customInfo.setName("吴时伟" + (i));
            customInfo.setBusiness("上海无敌信息管理咨询" + (i));
            customInfo.setCompany("广告策划有限公司第" + (i) + "分公司");
            customInfo.setPosition("董事长");
            customInfo.setTelephone("10086");
            customInfo.setRetriAlpha("" + StringUtil.getFirstLetter(customInfo.getName().charAt(0)));
            customInfo.setId("" + i);
            listItems.add(customInfo);
        }
        CustomInfo customInfo1 = new CustomInfo();
        customInfo1.setName("张三");
        customInfo1.setBusiness("上海无敌信息管理咨询");
        customInfo1.setCompany("广告策划有限公司第100分公司");
        customInfo1.setPosition("董事长");
        customInfo1.setTelephone("10086");
        customInfo1.setRetriAlpha("" + StringUtil.getFirstLetter(customInfo1.getName().charAt(0)));
        customInfo1.setId("");
        listItems.add(customInfo1);
        return listItems;
    }

    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            retriOverlay.setVisibility(View.GONE);
        }
    }

    class CustomItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            if (isMultipleList) {
                customListAdapter.changeItemState(position);
                mCallback.setSeletedCountShow();
                customListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "在单选模式下！点击 " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CustomDetailActivity.class);
                intent.putExtra("customDetail", listItems.get(position));
                startActivity(intent);
            }
            checkAllSelectedInterface.checkSelected(customListAdapter.isAllChecked());
        }
    }

    class CustomItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!isMultipleList) {
                acMode = getActivity().startActionMode(mCallback);
                customLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                isMultipleList = true;
                customLv.performItemClick(view, position, id);
            } else {
                customListAdapter.changeItemState(position);
            }
            customListAdapter.notifyDataSetChanged();
            checkAllSelectedInterface.checkSelected(customListAdapter.isAllChecked());
            return true;
        }
    }

    private class ModeCallback implements ListView.MultiChoiceModeListener {
        private View mMultiSelectActionBarView;
        private TextView mSelectedConvCount;

        @Override            // called when the action mode is created; startActionMode() was called
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            isMultipleList = true;
            // comes into MultiChoiceMode
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.multi_select_menu, menu);
            if (mMultiSelectActionBarView == null) {
                mMultiSelectActionBarView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.list_multi_select_actionbar, null);
                mSelectedConvCount = (TextView) mMultiSelectActionBarView.findViewById(R.id.selected_conv_count);
            }
            mode.setCustomView(mMultiSelectActionBarView);
            ((TextView) mMultiSelectActionBarView.findViewById(R.id.title)).setText("已选择客户：");
            setSeletedCountShow();
            return true;
        }

        // the following method is called each time
        // the action mode is shown. Always called after
        // onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (mMultiSelectActionBarView == null) {
                ViewGroup v = (ViewGroup) LayoutInflater.from(getActivity())
                        .inflate(R.layout.list_multi_select_actionbar, null);
                mode.setCustomView(v);
                mSelectedConvCount = (TextView) v.findViewById(R.id.selected_conv_count);
            }
            MenuItem mItem = menu.findItem(R.id.action_slelect);
            if (customListAdapter.isAllChecked()) {
                mItem.setTitle("取消所选");
            } else {
                mItem.setTitle("选择所有");
            }
            return true;
        }

        @Override    // called when the user selects a contextual menu item
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_slelect:
                    if (customListAdapter.isAllChecked()) {
                        customListAdapter.uncheckAll();
                        customLv.clearChoices();
                        mode.finish();
                    } else {
                        customListAdapter.checkAll();
                        for (int i = 0; i < listItems.size(); i++)
                            customLv.setSelection(i);
                    }
                    checkAllSelectedInterface.checkSelected(customListAdapter.isAllChecked());
                    customListAdapter.notifyDataSetChanged();
                    mSelectedConvCount.setText(Integer.toString(customListAdapter.getCheckedItemCount()));
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isMultipleList = false;
            checkAllSelectedInterface.checkSelected(false);
            customListAdapter.uncheckAll();
        }

        @Override   //Called when an item is checked or unchecked during selection mode.
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            int checkedCount = 0;
            customListAdapter.getItemState().put(position, !checked);
            checkedCount = customListAdapter.getCheckedItemCount();
            mSelectedConvCount.setText(Integer.toString(checkedCount));
            customListAdapter.notifyDataSetChanged();
            checkAllSelectedInterface.checkSelected(customListAdapter.isAllChecked());
        }

        public void setSeletedCountShow() {
            mSelectedConvCount.setText("" + customListAdapter.getCheckedItemCount());
        }
    }

    @Override
    public void onPause() {
        if (isMultipleList) {
            acMode.finish();
            isMultipleList = false;
        }
        super.onPause();
    }

    public void closeActionMode() {
        if (isMultipleList) {
            acMode.finish();
            isMultipleList = false;
        }
    }

    public void selectAll(boolean isSelected) {
        customListAdapter.selectAll(isSelected);
    }

    public ArrayList<CustomInfo> getSelectedCustom() {
        return customListAdapter.getSelectedCustom();
    }
}
