package com.bkjcb.rqapplication.adapter;

import android.util.Log;

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
            if (isSampleDay(preItem.date, item.date)) {
                helper.setGone(R.id.check_date, false);
            } else {
                helper.setGone(R.id.check_date, true);
            }
        }
        helper.setText(R.id.check_date, getTime(item.date));
    }

    private String getTime(long time) {
        Log.w("time", time + ":::" + currentTime);
        if (time - currentTime > 0) {
            return "今天";
        } else if (currentTime - time < 24 * 3600) {
            return "昨天";
        } else {
            return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINESE).format(new Date(time));
        }
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
