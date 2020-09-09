package com.bkjcb.rqapplication.treatment.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.adapter.BaseRecycleViewAdapter;
import com.bkjcb.rqapplication.treatment.model.DefectTreatmentModel;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class DefectTreatmentItemAdapter extends BaseRecycleViewAdapter<DefectTreatmentModel, BaseViewHolder> {
    public DefectTreatmentItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DefectTreatmentModel item) {
        helper.setText(R.id.treatment_type, item.getProcessTime())
                .setText(R.id.treatment_name, item.getUserName())
                .setText(R.id.treatment_address, item.getUserAddress())
                .setText(R.id.treatment_time, item.getCasesType())
                .setText(R.id.treatment_opinion, item.getOpinions());

        if (item.getFlag() != 1 || item.getProcessState() != 3) {
            helper.setGone(R.id.treatment_opinion, item.getFlag() == 0);
            helper.setText(R.id.treatment_time, obtainStatus(item.getProcessState(), item.getFlag()))
                    .setTextColor(R.id.treatment_time, mContext.getResources().getColor(R.color.colorMint));
        }

    }

    private String obtainStatus(int status, int flag) {
        if (status == 3 && flag == 7) {
            return "退单";
        } else if (status == 5 && flag == 1) {
            return "处置完成";
        } else if (status == 4 && flag == 2) {
            return "已结案";
        } else {
            return "";
        }
    }

}
