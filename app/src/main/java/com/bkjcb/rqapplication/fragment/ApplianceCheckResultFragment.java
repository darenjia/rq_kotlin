package com.bkjcb.rqapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckItem;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/2/21.
 * Description :
 */
public class ApplianceCheckResultFragment extends CheckResultFragment {


    @BindView(R.id.info_remark_title)
    TextView mInfoRemarkTitle;

    public static ApplianceCheckResultFragment newInstance(CheckItem checkItem) {
        ApplianceCheckResultFragment fragment = new ApplianceCheckResultFragment();
        fragment.setCheckItem(checkItem);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_appliance_check_result;
    }

    @Override
    protected void initView() {
        super.initView();

        if (checkItem.zhandianleixing.equals("报警器企业")) {
            mInfoRemarkTitle.setText("检查综合意见及整改要求");
        }
    }

    @Override
    protected void initData() {
        if (checkItem != null) {
            mInfoType.setText(checkItem.zhandianleixing);
            mInfoStation.setText(checkItem.beijiandanwei);
            mInfoDate.setText(checkItem.jianchariqi);
            mInfoRemark.setText(checkItem.beizhu);
            mInfoName.setText(checkItem.checkMan);
        }
    }
}


