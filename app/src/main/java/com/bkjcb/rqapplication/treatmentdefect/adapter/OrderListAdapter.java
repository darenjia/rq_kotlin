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
public class OrderListAdapter extends BaseQuickAdapter<BottleSaleCheck, BaseViewHolder> {
    public OrderListAdapter(int layoutResId, @Nullable List<BottleSaleCheck> data) {
        super(layoutResId, data);
    }

    public OrderListAdapter(@Nullable List<BottleSaleCheck> data) {
        super(data);
    }

    public OrderListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BottleSaleCheck item) {
        helper.setText(R.id.order_id,item.getBottleCode())
                .setText(R.id.order_company,item.getUnitName())
                .setText(R.id.order_person,item.getCheckPeople())
                .setText(R.id.order_time,item.getSaleTime());
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
