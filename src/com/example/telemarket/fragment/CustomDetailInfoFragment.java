package com.example.telemarket.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;

import java.util.List;

/**
 * 客户详情的详细信息界面
 */
public class CustomDetailInfoFragment extends Fragment {
    private int index;
    private CustomInfo customDetail;

    public CustomDetailInfoFragment(int index, CustomInfo customDetail) {
        this.index = index;
        this.customDetail = customDetail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_custom_detail_info, container, false);
        TextView nameTv = (TextView) view.findViewById(R.id.custom_name_tv);
        TextView sexTv = (TextView) view.findViewById(R.id.custom_sex_tv);
        TextView numTv = (TextView) view.findViewById(R.id.custom_num_tv);
        TextView companyTv = (TextView) view.findViewById(R.id.custom_company_tv);
        TextView positionTv = (TextView) view.findViewById(R.id.custom_position_tv);
        TextView telephoneTv = (TextView) view.findViewById(R.id.custom_telephone_tv);
        TextView wirephoneTv = (TextView) view.findViewById(R.id.custom_wirephone_tv);
        TextView easyNameTv = (TextView) view.findViewById(R.id.custom_easyname_tv);
        TextView isMsgTv = (TextView) view.findViewById(R.id.custom_isMsg_tv);
        TextView preferenceTv = (TextView) view.findViewById(R.id.custom_preference_tv);
        TextView birthdayTv = (TextView) view.findViewById(R.id.custom_birthday_tv);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
