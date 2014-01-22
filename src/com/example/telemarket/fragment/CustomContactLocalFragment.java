package com.example.telemarket.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import com.example.telemarket.R;
import com.example.telemarket.adapter.CustomLocalListAdapter;
import com.example.telemarket.bean.ContactMachineBean;
import com.example.telemarket.bean.CustomInfo;
import com.example.tool.ContactMachineHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomContactLocalFragment extends Fragment {
    private List<CustomInfo> listItems;
    private CustomLocalListAdapter customListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        listItems = getLocalContact();
        view = inflater.inflate(R.layout.fragment_custom, container, false);
        ListView customLv = (ListView) view.findViewById(R.id.fragment_custom_lv);
        customListAdapter = new CustomLocalListAdapter(getActivity(), listItems, R.layout.custom_list_item);
        customLv.setAdapter(customListAdapter);
        return view;
    }

    private List<CustomInfo> getLocalContact() {
        List<CustomInfo> listItems = new ArrayList<CustomInfo>();
        ContactMachineHelper helper = new ContactMachineHelper(getActivity());
        List<ContactMachineBean> retriList = helper.getLocalPhoneContact();

        for (int i = 0; i < retriList.size(); i++) {
            ContactMachineBean contactMachineBean = retriList.get(i);
            CustomInfo customInfo = new CustomInfo();
            customInfo.setName(contactMachineBean.getName());
            customInfo.setCompany(contactMachineBean.getNote());
            customInfo.setBusiness(contactMachineBean.getNickName());
            customInfo.setTelephone(contactMachineBean.getPhoneNumber());
            listItems.add(customInfo);
        }
        return listItems;
    }

    public void selectAll(boolean isSelected) {
        customListAdapter.selectAll(isSelected);
    }

    public ArrayList<CustomInfo> getSelectedCustom() {
        return customListAdapter.getSelectedCustom();
    }
}
