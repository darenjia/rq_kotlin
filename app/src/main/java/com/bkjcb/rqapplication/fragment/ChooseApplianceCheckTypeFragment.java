package com.bkjcb.rqapplication.fragment;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class ChooseApplianceCheckTypeFragment extends ChooseCheckTypeFragment {
    public static ChooseApplianceCheckTypeFragment newInstance(OnChooseListener listener, String[] type) {
        ChooseApplianceCheckTypeFragment fragment = new ChooseApplianceCheckTypeFragment();
        fragment.setListener(listener);
        fragment.setTypeString(type);
        return fragment;
    }



}

