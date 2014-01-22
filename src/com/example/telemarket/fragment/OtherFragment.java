package com.example.telemarket.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.telemarket.AppConfig;
import com.example.telemarket.R;

/**
 * Created with IntelliJ IDEA.
 * User: wyl
 * Date: 14-1-7
 * Time: 上午10:10
 * To change this template use File | Settings | File Templates.
 */
public class OtherFragment extends Fragment {
    public OtherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        int i = getArguments().getInt(AppConfig.ARG_FRAGMENT_NUMBER);
        String fragmentTitle = getResources().getStringArray(R.array.fragment_title_array)[i];
//            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                    "drawable", getActivity().getPackageName());     //根据名字获取到图片 的id
        ((TextView) rootView.findViewById(R.id.hello)).setText("这个界面是：" + fragmentTitle);
        getActivity().setTitle(fragmentTitle);
        return rootView;
    }
}
