package com.bkjcb.rqapplication.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bkjcb.rqapplication.R;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

/**
 * Created by DengShuai on 2020/1/9.
 * Description :
 */
public class MyStyleTopBar extends QMUITopBarLayout {
    public MyStyleTopBar(Context context) {
        super(context);
    }

    public MyStyleTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyStyleTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundDividerEnabled(boolean enabled) {
        //super.setBackgroundDividerEnabled(enabled);
        QMUIViewHelper.setBackgroundKeepingPadding(this, getResources().getDrawable(R.drawable.main_background));
    }
}
