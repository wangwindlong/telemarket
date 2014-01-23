package com.example.telemarket.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.example.jiekou.CheckAllSelectedInterface;
import com.example.telemarket.R;
import com.example.telemarket.adapter.CustomGroupLocalListAdapter;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.db.dao.CustomInfoDao;
import com.example.telemarket.ui.CustomListActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomContactGroupFragment extends Fragment {
    private List<CustomInfo> listItems = new ArrayList<CustomInfo>();
    private CustomGroupLocalListAdapter customListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        listItems = getLocalGroups();
        view = inflater.inflate(R.layout.fragment_custom, container, false);
        ListView customLv = (ListView) view.findViewById(R.id.fragment_custom_lv);
        customListAdapter = new CustomGroupLocalListAdapter(getActivity(), listItems, R.layout.custom_list_item);
        customLv.setAdapter(customListAdapter);
        return view;
    }

    public void selectAll(boolean isSelected) {
        customListAdapter.selectAll(isSelected);
    }

    public ArrayList<CustomInfo> getSelectedCustom() {
        return customListAdapter.getSelectedCustom();
    }

    private List<CustomInfo> getLocalGroups() {
        List<CustomInfo> customInfoList = new ArrayList<CustomInfo>();
        CustomInfoDao customInfoDao = new CustomInfoDao(getActivity());
        customInfoList = customInfoDao.getCustomInfo();
        return customInfoList;
    }
}
