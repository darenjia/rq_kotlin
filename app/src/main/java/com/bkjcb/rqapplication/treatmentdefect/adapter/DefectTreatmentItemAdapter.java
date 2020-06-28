package com.bkjcb.rqapplication.treatmentdefect.adapter;

import android.content.Context;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel;
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
    }

    @Override
    protected void convert(BaseViewHolder helper, DefectTreatmentModel item) {
        helper.setText(R.id.treatment_type, item.getProcessTime())
                .setText(R.id.treatment_name, item.getUserName())
                .setText(R.id.treatment_address, item.getUserAddress())
                .setText(R.id.treatment_time, item.getCasesType())
                .setText(R.id.treatment_opinion, item.getOpinions())
                .setGone(R.id.treatment_opinion, item.getFlag() == 0);
        if (item.getFlag() > 0) {
            helper.setText(R.id.treatment_time, obtainStatus(item.getProcessState()))
                    .setTextColor(R.id.treatment_time, mContext.getResources().getColor(R.color.colorMint));
        }

    }

    private String obtainStatus(int status) {
        if (status > 4) {
            return "处置完成";
        } else if (status > 2) {
            return "已结案";
        } else {
            return "退单";
        }
    }

    public void showErrorView(Context context, View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.error_view, null);
        view.setOnClickListener(listener);
        setNewData(null);
        setEmptyView(view);
    }

    public void showErrorView() {
        setNewData(null);
        setEmptyView(R.layout.error_view);
    }

    public void showLoadingView() {
        setNewData(null);
        setEmptyView(R.layout.loading_view);
    }

    public void showEmptyView() {
        setNewData(null);
        setEmptyView(R.layout.empty_textview);
    }
}
