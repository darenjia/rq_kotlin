package com.bkjcb.rqapplication.fragment;

import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckItem;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/2/21.
 * Description :
 */
public class CheckResultFragment extends BaseSimpleFragment {

    @BindView(R.id.info_type)
    TextView mInfoType;
    @BindView(R.id.info_station)
    TextView mInfoStation;
    @BindView(R.id.info_date)
    TextView mInfoDate;
    @BindView(R.id.info_year)
    TextView mInfoYear;
    @BindView(R.id.info_result)
    TextView mInfoResult;
    @BindView(R.id.info_remark)
    TextView mInfoRemark;
    @BindView(R.id.info_name)
    TextView mInfoName;
    protected CheckItem checkItem;

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public static CheckResultFragment newInstance(CheckItem checkItem) {
        CheckResultFragment fragment = new CheckResultFragment();
        fragment.setCheckItem(checkItem);
        return fragment;
    }

    public void updateData(CheckItem checkItem) {
        this.checkItem = checkItem;
        initData();
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_check_result;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (checkItem != null) {
            mInfoType.setText(checkItem.zhandianleixing);
            mInfoStation.setText(checkItem.beijiandanwei);
            mInfoYear.setText(checkItem.year);
            mInfoDate.setText(checkItem.jianchariqi);
            mInfoRemark.setText(checkItem.beizhu);
            mInfoResult.setText(checkItem.jianchajieguo);
            mInfoName.setText(checkItem.checkMan);
        }
    }
}
