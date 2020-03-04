package com.bkjcb.rqapplication.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.Emergency;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by DengShuai on 2017/8/28.
 */

public class EmergencyAdapter extends BaseQuickAdapter<Emergency, BaseViewHolder> {
    public EmergencyAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public EmergencyAdapter(@Nullable List data) {
        super(data);
    }

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
