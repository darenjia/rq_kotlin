package com.bkjcb.rqapplication.treatmentdefect.adapter;

import android.support.annotation.Nullable;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.treatmentdefect.model.BottleSaleCheck;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public class SecurityCheckListAdapter extends BaseQuickAdapter<BottleSaleCheck, BaseViewHolder> {
    public SecurityCheckListAdapter(int layoutResId, @Nullable List<BottleSaleCheck> data) {
        super(layoutResId, data);
    }

    public SecurityCheckListAdapter(@Nullable List<BottleSaleCheck> data) {
        super(data);
    }

    public SecurityCheckListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BottleSaleCheck item) {
        helper.setText(R.id.security_remark, item.getCheckRemark())
                .setText(R.id.security_status, item.getCheckState())
                .setText(R.id.security_time, item.getSaleTime());
    }

    public void showEmpty() {
        setEmptyView(R.layout.empty_textview);
    }

    public void showError() {
        setEmptyView(R.layout.error_view);
    }

    public void showLoading() {
        setEmptyView(R.layout.loading_view);
    }
}
