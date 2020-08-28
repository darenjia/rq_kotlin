package com.bkjcb.rqapplication.actionRegister.adapter;

import android.util.Log;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.actionRegister.model.ActionRegisterItem;
import com.bkjcb.rqapplication.base.MyApplication;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by DengShuai on 2019/12/23.
 * Description :
 */
public class ActionRegisterItemAdapter extends BaseQuickAdapter<ActionRegisterItem, BaseViewHolder> {
    private long currentTime = 0;

    public ActionRegisterItemAdapter(int layoutResId) {
        super(layoutResId);
        currentTime = getCurrentTime();
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionRegisterItem item) {
        if (helper.getLayoutPosition() != 0) {
            ActionRegisterItem preItem = getData().get(helper.getLayoutPosition() - 1);
            if (preItem.getSys_time().equals(item.getSys_time())) {
                helper.setGone(R.id.check_date, false);
            } else {
                helper.setGone(R.id.check_date, true);
            }
        }
        helper.setText(R.id.check_date, item.getSys_time())
                .setText(R.id.check_time, item.getCrime_time())
                .setText(R.id.check_status, getStatus(item.getStatus()))
                .setText(R.id.check_name, item.getCrime_address())
                .setText(R.id.check_type, item.getCase_source())
                .setTextColor(R.id.check_status, getColors(item.getStatus()))
                //.setText(R.id.check_operate, "")
                .setBackgroundColor(R.id.check_divider, getColors(item.getStatus()))
                .setImageResource(R.id.check_status_img, item.getStatus() == 0 ? R.drawable.vector_drawable_finished : R.drawable.vector_drawable_tip);
    }

    private String getTime(long time) {
        Log.w("time", time + ":::" + currentTime);
        if (time - currentTime > 0) {
            return "今天";
        } else if (currentTime - time < 24 * 3600) {
            return "昨天";
        } else {
            return new SimpleDateFormat("MM-dd", Locale.CHINESE).format(new Date(time));
        }
    }

    private String getStatus(int status) {
        String retString = "";
        switch (status) {
            case 0:
                retString = "已提交";
                break;
            case 1:
                retString = "待提交";
                break;
            default:
        }
        return retString;
    }

    private int getColors(int type) {
        if (type == 0) {
            return getColor(R.color.colorMint);
        }
        return getColor(R.color.colorAccent);
    }

    private int getColor(int resID) {
        return MyApplication.getContext().getResources().getColor(resID);
    }


    private boolean isSampleDay(long time1, long time2) {
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        return date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate();
    }

    private long getCurrentTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public void showEmpty() {
        setNewData(null);
        setEmptyView(R.layout.empty_textview);
    }

    public void showError() {
        setNewData(null);
        setEmptyView(R.layout.error_view);
    }

    public void showLoading() {
        setEmptyView(R.layout.loading_view);
    }
}
