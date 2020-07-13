package com.bkjcb.rqapplication.stationCheck.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.infoQuery.fragment.AlarmFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.fragment.InstallationFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.fragment.SellFirmInfoFragment;

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
        initFirmContent();
    }

    private void initFirmContent() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.firm_layout, createFragment())
                .commit();
    }

    private Fragment createFragment() {
        Fragment fragment;
        switch (getCheckItem().zhandianleixing) {
            case "报警器企业":
                fragment = AlarmFirmInfoFragment.newInstance(getCheckItem().beijiandanweiid);
                break;
            case "维修检查企业":
                fragment = InstallationFirmInfoFragment.newInstance(getCheckItem().beijiandanweiid);
                break;
            default:
                fragment = SellFirmInfoFragment.newInstance(getCheckItem().beijiandanweiid);
        }
        return fragment;
    }
}
