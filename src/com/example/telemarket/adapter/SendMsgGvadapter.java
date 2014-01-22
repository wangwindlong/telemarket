package com.example.telemarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-26
 * Time: 下午7:14
 * To change this template use File | Settings | File Templates.
 */
public class SendMsgGvadapter extends BaseAdapter {
    private LayoutInflater mInflater;
//    private List<Map<String, Object>> contents;
    private ArrayList<CustomInfo> contents;
    private Context context;

    class ViewHolder {
        public TextView nameTv;       //姓名
        public TextView numTv;   //号码
        public TextView plusTv;   //添加图标
    }

    public SendMsgGvadapter(Context context, ArrayList<CustomInfo> contents) {
        this.contents = contents;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setContents(ArrayList<CustomInfo> contents) {
        this.contents = contents;
    }

    public int getCount() {
        return contents.size() + 1;
    }

    public Object getItem(int position) {
//        return contents.get(position);
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.send_message_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.FaxinNameTv);
            viewHolder.numTv = (TextView) convertView.findViewById(R.id.FaxinPhnumTv);
            viewHolder.plusTv = (TextView) convertView.findViewById(R.id.FaxinPlusTv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (contents.size() > position) {
            CustomInfo item = contents.get(position);
            if (item != null) {
                viewHolder.nameTv.setVisibility(View.VISIBLE);
                viewHolder.nameTv.setText(item.getName());
                viewHolder.numTv.setVisibility(View.VISIBLE);
                viewHolder.numTv.setText(item.getTelephone());
                viewHolder.plusTv.setVisibility(View.GONE);
            }
        } else {
            viewHolder.plusTv.setVisibility(View.VISIBLE);
            viewHolder.numTv.setVisibility(View.GONE);
            viewHolder.nameTv.setVisibility(View.GONE);
        }

        return convertView;
    }
}
