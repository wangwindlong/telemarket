package com.example.telemarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.bean.ProductInfo;
import com.example.telemarket.ui.CustomDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 产品列表Adapter类
 */
public class ProductListAdapter extends BaseAdapter {
    private Context context;//运行上下文
    private List<ProductInfo> listItems;//数据集合
    private LayoutInflater listContainer;//视图容器
    private int itemViewResource;//自定义项视图源
//    private HashMap<Integer, Boolean> hashMap; //保存checbox的状态

    static class ListItemView {                //自定义控件集合
        public TextView nameTv;       //产品名称
        public TextView sumTv;        //保额
        public TextView durationTv;   //有效销售期

        public ImageView photoIv;  //打电话的imageview
    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param resource
     */
    public ProductListAdapter(Context context, List<ProductInfo> data, int resource) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);    //创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    /**
     * ListView Item设置
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(this.itemViewResource, null);
            listItemView = new ListItemView();
            //获取控件对象
            listItemView.nameTv = (TextView) convertView.findViewById(R.id.product_item_name_tv);
            listItemView.durationTv = (TextView) convertView.findViewById(R.id.product_item_duration_tv);
            listItemView.sumTv = (TextView) convertView.findViewById(R.id.product_item_sum_tv);
            listItemView.photoIv = (ImageView) convertView.findViewById(R.id.product_item_photo_iv);

            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        //设置文字和图片
        final ProductInfo productitem = listItems.get(position);

        listItemView.nameTv.setText(productitem.getProductName());
        listItemView.durationTv.setText(productitem.getCookieTime());
        listItemView.sumTv.setText(productitem.getProductSum());
        listItemView.photoIv.setImageResource(R.drawable.person_head);

        return convertView;
    }

    public List<ProductInfo> getListItems() {
        return listItems;
    }
}