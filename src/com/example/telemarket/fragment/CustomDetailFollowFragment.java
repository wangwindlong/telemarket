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
 * 客户详情的跟进界面
 */
public class CustomDetailFollowFragment extends Fragment {
    private int index;
    private CustomInfo customDetail;
    private Spinner duringSp;

    public CustomDetailFollowFragment(int index, CustomInfo customDetail) {
        this.index = index;
        this.customDetail = customDetail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_custom_detail_follow, container, false);
        duringSp = (Spinner) view.findViewById(R.id.follow_during_sp);
        duringSp.setAdapter(getArrayAdapter(2));

        RadioButton yearCb = (RadioButton) view.findViewById(R.id.follow_year_cb);
        RadioButton jiduCb = (RadioButton) view.findViewById(R.id.follow_jidu_cb);
        RadioButton monthCb = (RadioButton) view.findViewById(R.id.follow_month_cb);
        RadioButton weekCb = (RadioButton) view.findViewById(R.id.follow_week_cb);
        RadioButton[] radioGroup = new RadioButton[]{yearCb, jiduCb, monthCb, weekCb};
        yearCb.setOnClickListener(new FilterTypeClickListener(0, radioGroup));
        jiduCb.setOnClickListener(new FilterTypeClickListener(1, radioGroup));
        monthCb.setOnClickListener(new FilterTypeClickListener(2, radioGroup));
        weekCb.setOnClickListener(new FilterTypeClickListener(3, radioGroup));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    class FilterTypeClickListener implements View.OnClickListener {
        int type;
        RadioButton[] radioGroup;

        FilterTypeClickListener(int type, RadioButton[] radioGroup) {
            this.type = type;
            this.radioGroup = radioGroup;
        }

        @Override
        public void onClick(View v) {
            duringSp.setAdapter(getArrayAdapter(type));
            for (int i = 0; i < 4; i++) {
                radioGroup[i].setChecked(false);
            }
            radioGroup[type].setChecked(true);
        }
    }

    private ArrayAdapter<CharSequence> getArrayAdapter(int type) {
        switch (type) {
            case 0:
                return new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.follow_year));
            case 1:
                return new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.follow_jidu));
            case 2:
                return new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.follow_month));
            case 3:
                return new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.follow_week));
        }
        return new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.follow_month));
    }

}
