package com.bkjcb.rqapplication.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by DengShuai on 2019/4/17.
 * Description :
 */
public class CustomVideoView extends VideoView {

    public CustomVideoView(Context context) {
        super(context, null);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
