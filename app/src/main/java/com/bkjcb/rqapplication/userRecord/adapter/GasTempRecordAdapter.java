package com.bkjcb.rqapplication.userRecord.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.userRecord.model.GasRecordModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
public class GasTempRecordAdapter extends BaseQuickAdapter<GasRecordModel, BaseViewHolder> {

    public GasTempRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GasRecordModel item) {
        helper.setText(R.id.check_time, "建档日期：" + item.jiandangriqi)
                .setText(R.id.check_name, item.yonghuming)
                .setText(R.id.check_operate, item.dizhi)
                .addOnClickListener(R.id.check_type);
    }
}
