package com.example.telemarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.telemarket.R;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-26
 * Time: 下午7:14
 * To change this template use File | Settings | File Templates.
 */
public class MainGvadapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, Object>> contents;
    private GridView gridView;

    public MainGvadapter(Context context, List<Map<String, Object>> contents, GridView gridView) {
        this.contents = contents;
        this.gridView = gridView;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        int num = contents.size();
        return num < 6 ? num : 6;
    }

    public Object getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> item = contents.get(position);
        //此处不能用if (convertView == null),第一个item不会绘制，具体原因不明
        convertView = mInflater.inflate(R.layout.main_gv_item, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, gridView.getHeight() / 2);
        convertView.setLayoutParams(params);
        ImageView picIv = (ImageView) convertView.findViewById(R.id.griditem_pic);
        TextView textTv = (TextView) convertView.findViewById(R.id.griditem_title);
        picIv.setImageResource(Integer.parseInt(item.get("imageName").toString()));
        textTv.setText(item.get("titleName").toString());
        return convertView;
    }
}
