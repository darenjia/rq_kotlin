package com.bkjcb.rqapplication.base.loadsir.callback;

import com.bkjcb.rqapplication.R;
import com.kingja.loadsir.callback.Callback;

/**
 * Created by DengShuai on 2020/7/10.
 * Description :
 */
public class EmptyCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.empty_textview;
    }
}
