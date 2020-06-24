package com.bkjcb.rqapplication.contact.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contact.model.ContactBaseModel;
import com.bkjcb.rqapplication.contact.model.Level;
import com.bkjcb.rqapplication.contact.model.User;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * Created by DengShuai on 2020/3/17.
 * Description :
 */
public class ContactSearchResultAdapter extends BaseQuickAdapter<ContactBaseModel, BaseViewHolder> {
    public ContactSearchResultAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBaseModel item) {
        if (item instanceof User) {
            User user = (User) item;
            AvatarImageView iv = (AvatarImageView) helper.getView(R.id.item_avatar);
            iv.setTextAndColor(user.getUsername().substring(0, 1), Utils.getColor(mContext, helper.getLayoutPosition()));
            helper.setText(R.id.info_tv, user.getUsername())
                    .setText(R.id.info_dz, user.getDuty())
                    .setText(R.id.info_qx, user.getUnit() != null ? user.getUnit().getDistrictName() : "")
                    .setGone(R.id.class_a, user.getRole_a() == 1)
                    .setGone(R.id.class_b, user.getRole_b() == 1)
                    .setGone(R.id.class_c, user.getRole_c() == 1)
                    .setGone(R.id.class_d, user.getRole_d() == 1);
        }else {
            Level level= (Level) item;
            helper.setGone(R.id.item_avatar,false)
                    .setText(R.id.info_dz,level.getDepartmentname())
                    .setText(R.id.info_qx,level.getDistrictname())
                    .setGone(R.id.class_a, false)
                    .setGone(R.id.class_b, false)
                    .setGone(R.id.class_c, false)
                    .setGone(R.id.class_d, false)
                    .setGone(R.id.info_tv, false);
        }
    }

}
