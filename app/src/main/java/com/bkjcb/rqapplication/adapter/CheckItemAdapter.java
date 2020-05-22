package com.bkjcb.rqapplication.adapter;

import android.util.Log;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckItem;
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
public class CheckItemAdapter extends BaseQuickAdapter<CheckItem, BaseViewHolder> {
    private long currentTime = 0;

    public CheckItemAdapter(int layoutResId) {
        super(layoutResId);
        currentTime = getCurrentTime();
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckItem item) {
        if (helper.getLayoutPosition() != 0) {
            CheckItem preItem = getData().get(helper.getLayoutPosition() - 1);
            if (isSampleDay(preItem.systime, item.systime)) {
                helper.setGone(R.id.check_date, false);
            } else {
                helper.setGone(R.id.check_date, true);
            }
        }
        helper.setText(R.id.check_date, getTime(item.systime))
                .setText(R.id.check_time, item.jianchariqi)
                .setText(R.id.check_status, getStatus(item.status))
                .setText(R.id.check_name, item.beijiandanwei)
                .setText(R.id.check_type, "#" + item.zhandianleixing)
                .setTextColor(R.id.check_status,getColors(item.status))
                .setBackgroundColor(R.id.check_divider,getColor(item.zhandianleixing) );
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
                retString = "待检查";
                break;
            case 1:
                retString = "检查中";
                break;
            case 2:
                retString = "待上传";
                break;
            case 3:
                retString = "已完成";
            default:
        }
        return retString;
    }

    private int getColor(String type) {
        switch (type) {
            case "供应站":
            case "维修检查企业":
                return getColor(R.color.colorMint);
            case "储配站":
            case "报警器企业":
                return getColor(R.color.colorGrizzlyBear);
            case "加气站":
            case "销售企业":
                return getColor(R.color.colorTextThird);
            case "气化站":
                return getColor(R.color.colorYellow);
            default:
                return getColor(R.color.colorAccent);
        }
    }

    private int getColor(int resID) {
        return MyApplication.getContext().getResources().getColor(resID);
    }

    private int getColors(int type) {
        if (type == 3) {
            return getColor(R.color.colorMint);
        }
        return getColor(R.color.colorAccent);
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
