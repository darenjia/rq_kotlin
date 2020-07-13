package com.bkjcb.rqapplication.contactBook.adapter;

import android.text.TextUtils;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contactBook.model.Emergency;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2017/8/28.
 */

public class EmergencyAdapter extends BaseQuickAdapter<Emergency, BaseViewHolder> {

    public EmergencyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, Emergency emergency) {
        SuperTextView tv = holder.getView(R.id.third_content);
        tv.setLeftString(emergency.getTel())
                .setRightString(emergency.getUnit());
        if (!TextUtils.isEmpty(emergency.getArea()) && !TextUtils.isEmpty(emergency.getRemarks())) {
            tv.setCenterString(emergency.getArea() + "(" + emergency.getRemarks() + ")");
        } else if (!TextUtils.isEmpty(emergency.getArea()) && TextUtils.isEmpty(emergency.getRemarks())) {
            tv.setCenterString(emergency.getArea());
        } else {
            tv.setCenterString("");
        }
    }
}
