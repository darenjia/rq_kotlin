package com.bkjcb.rqapplication.adapter;

import android.content.Context;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class DefectTreatmentItemAdapter extends BaseQuickAdapter<DefectTreatmentModel, BaseViewHolder> {
    public DefectTreatmentItemAdapter(int layoutResId) {
        super(layoutResId);
        setLoadMoreView(new CustomLoadMoreView());
        setEnableLoadMore(true);
    }

    @Override
    protected void convert(BaseViewHolder helper, DefectTreatmentModel item) {
        helper.setText(R.id.treatment_type, item.type)
                .setText(R.id.treatment_name, item.name)
                .setText(R.id.treatment_address, item.address)
                .setText(R.id.treatment_time, item.time);

    }

    public void showErrorView(Context context, View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.error_view, null);
        view.setOnClickListener(listener);
        setNewData(null);
        setEmptyView(view);
    }
    public void showLoadingView(){
        setNewData(null);
        setEmptyView(R.layout.loading_view);
    }
    public void showEmptyView(){
        setNewData(null);
        setEmptyView(R.layout.empty_textview);
    }
}
