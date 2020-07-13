package com.bkjcb.rqapplication.emergency.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.adapter.BaseRecycleViewAdapter;
import com.bkjcb.rqapplication.emergency.model.EmergencyModel;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/7/8.
 * Description :
 */
public class EmergencyAdapter extends BaseRecycleViewAdapter<EmergencyModel, BaseViewHolder> {
    public EmergencyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmergencyModel item) {
        helper.setText(R.id.emergency_type,item.getReport_party())
                .setText(R.id.emergency_des,item.getMemo())
                .setText(R.id.emergency_address,item.getCompleteaddress())
                .setText(R.id.emergency_time,item.getReceivedate());
        if (item.getState().equals("已销根")){
            helper.setImageResource(R.id.emergency_type_img,R.drawable.vector_drawable_finished);
        }
    }
}
