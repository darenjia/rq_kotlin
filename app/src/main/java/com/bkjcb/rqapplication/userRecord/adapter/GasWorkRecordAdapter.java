package com.bkjcb.rqapplication.userRecord.adapter;

import android.text.TextUtils;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.userRecord.model.GasUserRecordResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
public class GasWorkRecordAdapter extends BaseQuickAdapter<GasUserRecordResult.GasUserRecord, BaseViewHolder> {

    public GasWorkRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GasUserRecordResult.GasUserRecord item) {
        helper.setText(R.id.check_time, item.getJiandangriqi())
                .setText(R.id.check_name, item.getYonghuming())
                .setText(R.id.check_operate, item.getDizhi())
                .setText(R.id.check_type, item.getSuoshuqu() + " - " + item.getJiedao())
                .setGone(R.id.check_divider, !TextUtils.isEmpty(item.getRquserid()))
                .addOnClickListener(R.id.check_detail)
                .addOnClickListener(R.id.check_record);
    }

    private String getStatus(String status) {
        if (TextUtils.isEmpty(status)) {
            return "#未关联";
        }
        return "#已关联";
    }

    private int getColor(String type) {
        if (!TextUtils.isEmpty(type)) {
            return getColor(R.color.colorMint);
        } else {
            return getColor(R.color.colorTextThird);
        }
    }

    private int getColor(int resID) {
        return MyApplication.getContext().getResources().getColor(resID);
    }

    private int getColors(String type) {
        if (type.equals("0")) {
            return getColor(R.color.colorMint);
        }
        return getColor(R.color.colorAccent);
    }

}
