package com.bkjcb.rqapplication.base.adapter;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.MenuItem;

import java.util.List;


/**
 * Created by DengShuai on 2019/5/15.
 * Description :
 */
public class MenuGridAdapter extends BaseAdapter {
    List<MenuItem> list;
    ViewHolder viewHolder;

    public MenuGridAdapter(List<MenuItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder(R.layout.item_menu_view);
            convertView = viewHolder.view;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MenuItem item = list.get(position);
        viewHolder.imageView.setImageResource(item.imgUrl);
        if (!item.purview){
            viewHolder.imageView.setImageTintList(ColorStateList.valueOf(MyApplication.getContext().getResources().getColor(R.color.colorTextSecond)));
        }
        viewHolder.textView.setText(item.text);
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;
        LinearLayout layout;

        ViewHolder(int layoutID) {
            view = View.inflate(MyApplication.getContext(), layoutID, null);
            imageView = view.findViewById(R.id.menu_item_image);
            textView = view.findViewById(R.id.menu_item_title);
            layout = view.findViewById(R.id.menu_item_layout);
        }
    }
}
