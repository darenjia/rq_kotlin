package com.bkjcb.rqapplication.fragment;

import android.support.v7.widget.RecyclerView;

import com.bkjcb.rqapplication.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentDetailFragment extends BaseSimpleFragment {
    @BindView(R.id.info_distribution_list)
    RecyclerView mInfoDistributionList;
    @BindView(R.id.info_distribution_list)
    RecyclerView mInfoCheckList;
    @Override
    public void setResId() {
        resId = R.layout.fragment_treatment_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
