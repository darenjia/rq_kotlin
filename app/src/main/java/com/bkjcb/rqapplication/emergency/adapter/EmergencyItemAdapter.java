package com.bkjcb.rqapplication.emergency.adapter;

import android.util.Log;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.emergency.model.EmergencyItem;
import com.bkjcb.rqapplication.base.util.Utils;
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
public class EmergencyItemAdapter extends BaseQuickAdapter<EmergencyItem, BaseViewHolder> {
    private long currentTime = 0;

    public EmergencyItemAdapter(int layoutResId) {
        super(layoutResId);
        currentTime = getCurrentTime();
    }

    @Override
    protected void convert(BaseViewHolder helper, EmergencyItem item) {
        if (helper.getLayoutPosition() != 0) {
            EmergencyItem preItem = getData().get(helper.getLayoutPosition() - 1);
            if (isSampleDay(preItem.getSystime(), item.getSystime())) {
                helper.setGone(R.id.check_date, false);
            } else {
                helper.setGone(R.id.check_date, true);
            }
        }
        helper.setText(R.id.check_date, getTime(item.getSystime()))
                .setText(R.id.check_time, item.getAccidentDate())
                .setText(R.id.check_status, getStatus(item.getStatus()))
                .setText(R.id.check_name, item.getAccidentAddress())
                .setText(R.id.check_type, item.getQushu())
                .setTextColor(R.id.check_status,getColors(item.getStatus()))
                //.setText(R.id.check_operate, "")
                .setBackgroundColor(R.id.check_divider, Utils.getRandomColor(MyApplication.getContext()));
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
            case 1:
                retString = "待提交";
                break;
            case 2:
                retString = "已提交";
                break;
            default:
        }
        return retString;
    }

    private int getColors(int type) {
        if (type == 2) {
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
}
