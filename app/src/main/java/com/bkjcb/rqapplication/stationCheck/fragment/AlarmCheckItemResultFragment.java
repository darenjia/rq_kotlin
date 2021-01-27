package com.bkjcb.rqapplication.stationCheck.fragment;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
public class AlarmCheckItemResultFragment extends BaseSimpleFragment implements RadioGroup.OnCheckedChangeListener {

    private CheckItem checkItem;
    @BindView(R.id.item_record)
    EditText mItemRecord;
    @BindView(R.id.check_result_radio_need)
    RadioButton need;
    @BindView(R.id.check_result_radio_donotneed)
    RadioButton notNeed;
    @BindView(R.id.check_result_radioGroup_need)
    RadioGroup needRadioGroup;

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public static AlarmCheckItemResultFragment newInstance(CheckItem item) {
        AlarmCheckItemResultFragment fragment = new AlarmCheckItemResultFragment();
        fragment.setCheckItem(item);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_check_item_result2;
    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(checkItem.tijiaobaogao)) {
            if ("1".equals(checkItem.tijiaobaogao)) {
                needRadioGroup.check(R.id.check_result_radio_need);
            } else {
                needRadioGroup.check(R.id.check_result_radio_donotneed);
            }
        }
    }

    @Override
    protected void initData() {
        mItemRecord.setText(checkItem.beizhu);
        if (checkItem.status < 3) {
            needRadioGroup.setOnCheckedChangeListener(this);
        } else {
            mItemRecord.setEnabled(false);
            need.setEnabled(false);
            notNeed.setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.check_result_radio_need) {
                checkItem.tijiaobaogao = "1";
            } else {
                checkItem.tijiaobaogao = "0";
            }
    }


    public String getRemark() {
        return mItemRecord.getText().toString();
    }
}
