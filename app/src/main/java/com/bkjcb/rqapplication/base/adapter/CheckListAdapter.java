package com.bkjcb.rqapplication.base.adapter;

import android.support.annotation.Nullable;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.BottleTourCheck;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public class CheckListAdapter extends BaseQuickAdapter<BottleTourCheck, BaseViewHolder> {
    public CheckListAdapter(int layoutResId, @Nullable List<BottleTourCheck> data) {
        super(layoutResId, data);
    }

    public CheckListAdapter(@Nullable List<BottleTourCheck> data) {
        super(data);
    }

    public CheckListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BottleTourCheck item) {
        helper.setText(R.id.check_person,item.getTourPeople())
                .setText(R.id.check_remark,item.getTourRemark())
                .setText(R.id.check_status,item.getTourState())
                .setText(R.id.check_time,item.getTourTime());
    }
}
