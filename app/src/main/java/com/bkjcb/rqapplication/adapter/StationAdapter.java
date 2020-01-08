package com.bkjcb.rqapplication.adapter;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckStation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class StationAdapter extends BaseQuickAdapter<CheckStation, BaseViewHolder> {
    public StationAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckStation item) {
        helper.setText(R.id.station_name, item.getQiyemingcheng())
        .setText(R.id.gas_station_name,item.getGas_station());
        if (item.isChecked()) {
            helper.setTextColor(R.id.station_name,getColor(R.color.colorText))
                    .setTextColor(R.id.gas_station_name,getColor(R.color.colorText))
            .setBackgroundColor(R.id.station_layout,getColor(R.color.colorPrimary));
        }else {
            helper.setTextColor(R.id.station_name,getColor(R.color.colorAccent))
                    .setTextColor(R.id.gas_station_name,getColor(R.color.colorSecondDrayText))
                    .setBackgroundColor(R.id.station_layout,getColor(R.color.colorText));
        }
    }
    private int getColor(int resID){
        return MyApplication.getContext().getResources().getColor(resID);
    }
}
