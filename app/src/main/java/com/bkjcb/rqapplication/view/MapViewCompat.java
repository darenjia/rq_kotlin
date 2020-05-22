package com.bkjcb.rqapplication.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by admin on 2019/4/19.
 */

public class MapViewCompat extends FrameLayout {


    public MapViewCompat(@NonNull Context context) {
        super(context);
    }

    public MapViewCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MapViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);//请求父控件不拦截触摸事件
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }
}