package com.bkjcb.rqapplication.contactBook.adapter;

import android.support.annotation.Nullable;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactItemAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public ContactItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        AvatarImageView iv = (AvatarImageView) helper.getView(R.id.item_avatar);
        iv.setTextAndColor(item.getUsername().substring(0, 1), Utils.getColor(mContext, helper.getLayoutPosition()));
        helper.setText(R.id.info_tv, item.getUsername())
                .setText(R.id.info_dz, item.getDuty())
                .setText(R.id.info_qx, item.getUnit()!=null?item.getUnit().getDistrictName():"")
                .setGone(R.id.class_a, item.getRole_a() == 1)
                .setGone(R.id.class_b, item.getRole_b() == 1)
                .setGone(R.id.class_c, item.getRole_c() == 1)
                .setGone(R.id.class_d, item.getRole_d() == 1);
    }

    public void showEmptyView(){
        setEmptyView(R.layout.empty_textview);
    }

    @Override
    public void setNewData(@Nullable List<User> data) {
        super.setNewData(data);
        if (data==null||data.size()==0){
            showEmptyView();
        }
    }
}
