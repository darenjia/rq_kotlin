package com.bkjcb.rqapplication.check.fragment;

import android.view.View;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class ChooseApplianceCheckInfoFragment extends ChooseCheckInfoFragment {


    public static ChooseApplianceCheckInfoFragment newInstance(OnChooseListener listener) {
        ChooseApplianceCheckInfoFragment fragment = new ChooseApplianceCheckInfoFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        mInfoYear.setVisibility(View.GONE);
    }
}
