package com.example.telemarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jiekou.CheckAllSelectedInterface;
import com.example.telemarket.R;
import com.example.telemarket.bean.CustomInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 客户列表Adapter类
 */
public class CustomPoolListAdapter extends BaseAdapter {
    private Context context;//运行上下文
    private List<CustomInfo> listItems = new ArrayList<CustomInfo>();//数据集合
    private LayoutInflater listContainer;//视图容器
    private int itemViewResource;//自定义项视图源
    private HashMap<Integer, Boolean> hashMap; //保存checbox的状态
    private CheckAllSelectedInterface checkAllSelectedInterface;
//    private boolean[] itemState;

    static class ListItemView {                //自定义控件集合
        public CheckBox checkBox;
        public TextView name;       //姓名
        public TextView position;   //职位
        public TextView business;  //所属行业
        public TextView company;   //所属公司
        public TextView retriAlpha;   //分组条
        public ImageView photo;   //头像

        public ImageView callIv;  //打电话的imageview
        public ImageView msgIv;   //发短信的imageview
    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param resource
     */
    public CustomPoolListAdapter(Context context, List<CustomInfo> data, int resource, CheckAllSelectedInterface checkAllSelected) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);    //创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
        this.checkAllSelectedInterface = checkAllSelected;
        this.hashMap = new HashMap<Integer, Boolean>();
        // 初始化数据
        initData();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param listItems
     */
    public void updateListView(List<CustomInfo> listItems){
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    // 初始化isSelected的数据
    private void initData() {
        for (int i = 0; i < listItems.size(); i++) {
            hashMap.put(i, false);
        }
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

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public HashMap<Integer, Boolean> getItemState() {
        return hashMap;
    }

    public void changeItemState(int position) {
        hashMap.put(position, !hashMap.get(position));
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
            listItemView.checkBox = (CheckBox) convertView.findViewById(R.id.custom_list_item_cb);
            listItemView.name = (TextView) convertView.findViewById(R.id.custom_item_name_tv);
            listItemView.retriAlpha = (TextView) convertView.findViewById(R.id.retriAlpha);
            listItemView.position = (TextView) convertView.findViewById(R.id.custom_item_position_tv);
            listItemView.business = (TextView) convertView.findViewById(R.id.custom_item_business_tv);
            listItemView.company = (TextView) convertView.findViewById(R.id.custom_item_company_tv);
            listItemView.photo = (ImageView) convertView.findViewById(R.id.custom_item_photo_iv);
            listItemView.callIv = (ImageView) convertView.findViewById(R.id.custom_item_call_iv);
            listItemView.msgIv = (ImageView) convertView.findViewById(R.id.custom_item_msg_iv);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        //设置文字和图片
        final CustomInfo customitem = listItems.get(position);
        listItemView.name.setText(customitem.getName());
//		listItemView.name.setTag(news);//设置隐藏参数(实体类)
        listItemView.position.setText(customitem.getPosition());
        listItemView.business.setText(customitem.getBusiness());
        listItemView.company.setText(customitem.getCompany());
        listItemView.retriAlpha.setText(customitem.getRetriAlpha());
        listItemView.photo.setImageResource(R.drawable.person_head);
        listItemView.callIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customitem.getTelephone())));
            }
        });
        listItemView.msgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + customitem.getTelephone()));
                it.putExtra("sms_body", "尊敬的" + customitem.getName() + ",您好!");
                context.startActivity(it);
            }
        });
        listItemView.checkBox.setChecked(hashMap.get(position));
        listItemView.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashMap.put(position, !hashMap.get(position));
                checkAllSelectedInterface.checkSelected(isAllChecked());
                notifyDataSetChanged();
            }
        });
        updateBackground(position,convertView);
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            listItemView.retriAlpha.setVisibility(View.VISIBLE);
            listItemView.retriAlpha.setText(customitem.getRetriAlpha());
        }else{
            listItemView.retriAlpha.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void updateBackground(int position, View view) {
        int backgroundId;
        if (hashMap.get(position)) {
            backgroundId = R.drawable.list_longpressed_holo;
        } else {
            backgroundId = R.color.full_transparent;
        }
        Drawable background = context.getResources().getDrawable(backgroundId);
        view.setBackgroundDrawable(background);
    }

    public void selectAll(boolean isSelected) {
        for (int i = 0; i < listItems.size(); i++) {
            hashMap.put(i, isSelected);
        }
        notifyDataSetChanged();
    }

    public ArrayList<CustomInfo> getSelectedCustom() {
        ArrayList<CustomInfo> customList = new ArrayList<CustomInfo>();
        for (int i = 0; i < listItems.size(); i++) {
            if (hashMap.get(i)) {
                customList.add(listItems.get(i));
            }
        }
        return customList;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return listItems.get(position).getRetriAlpha().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = listItems.get(i).getRetriAlpha();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public void uncheckAll() {
        for (int i = 0; i < listItems.size(); i++) {
            hashMap.put(i, false);
        }
    }

    public boolean isAllChecked() {
        for (int i = 0; i < listItems.size(); i++) {
            if (!hashMap.get(i)) return false;
        }
        return true;
    }

    public void checkAll() {
        for (int i = 0; i < listItems.size(); i++) {
            hashMap.put(i, true);
        }
    }

    public int getCheckedItemCount() {
        int count = 0;
        for (int i = 0; i < listItems.size(); i++) {
            if (hashMap.get(i)) count++;
        }
        return count;
    }

}