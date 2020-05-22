package com.bkjcb.rqapplication.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.GasUserRecordResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class GasUserAdapter extends BaseQuickAdapter<GasUserRecordResult.GasUserRecord, BaseViewHolder> {
    public GasUserAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GasUserRecordResult.GasUserRecord item) {
        helper.setText(R.id.road_name, item.getYonghuming())
                .setText(R.id.road_address, item.getDizhi())
                .setVisible(R.id.code, false);
    }
}
