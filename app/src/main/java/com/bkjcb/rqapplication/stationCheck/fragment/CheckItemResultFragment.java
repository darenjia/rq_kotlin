package com.bkjcb.rqapplication.stationCheck.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseLazyFragment;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
public class CheckItemResultFragment extends BaseLazyFragment implements RadioGroup.OnCheckedChangeListener {

    private View view;
    private CheckItem checkItem;
    private EditText mItemRecord;
    private RadioButton ok;
    private RadioButton failure;

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public static CheckItemResultFragment newInstance(CheckItem item) {
        CheckItemResultFragment fragment = new CheckItemResultFragment();
        fragment.setCheckItem(item);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_check_item_result, null);
            mItemRecord = view.findViewById(R.id.item_record);
            mItemRecord.setText(checkItem.beizhu);
            RadioGroup radioGroup = view.findViewById(R.id.check_result_radioGroup);
            RadioGroup needRadioGroup = view.findViewById(R.id.check_result_radioGroup_need);
            ok = view.findViewById(R.id.check_result_radio_ok);
            failure = view.findViewById(R.id.check_result_radio_failure);
           RadioButton need = view.findViewById(R.id.check_result_radio_need);
            RadioButton notNeed = view.findViewById(R.id.check_result_radio_donotneed);
            if (!TextUtils.isEmpty(checkItem.jianchajieguo)) {
                if ("合格".equals(checkItem.jianchajieguo)) {
                    radioGroup.check(R.id.check_result_radio_ok);
                    changeTextColor(true);
                } else {
                    radioGroup.check(R.id.check_result_radio_failure);
                    changeTextColor(false);
                }
            }
            if (!TextUtils.isEmpty(checkItem.tijiaobaogao)) {
                if ("需要整改报告".equals(checkItem.tijiaobaogao)) {
                    needRadioGroup.check(R.id.check_result_radio_need);
                } else {
                    needRadioGroup.check(R.id.check_result_radio_donotneed);
                }
            }
            if (checkItem.status <3) {
                radioGroup.setOnCheckedChangeListener(this);
                needRadioGroup.setOnCheckedChangeListener(this);
            }else {
                mItemRecord.setEnabled(false);
                ok.setEnabled(false);
                failure.setEnabled(false);
                need.setEnabled(false);
                notNeed.setEnabled(false);
            }
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.check_result_radioGroup) {
            if (checkedId == R.id.check_result_radio_ok) {
                checkItem.jianchajieguo = "合格";
                changeTextColor(true);
            } else {
                checkItem.jianchajieguo = "不合格";
                changeTextColor(false);
            }
        } else {
            if (checkedId == R.id.check_result_radio_need) {
                checkItem.tijiaobaogao = "需要整改报告";
            } else {
                checkItem.tijiaobaogao = "不需要整改报告";
            }
        }
    }

    private void changeTextColor(boolean isOk) {
        if (isOk) {
            ok.setTextColor(getResources().getColor(R.color.colorText));
            failure.setTextColor(getResources().getColor(R.color.colorSecondDrayText));
        } else {
            ok.setTextColor(getResources().getColor(R.color.colorSecondDrayText));
            failure.setTextColor(getResources().getColor(R.color.colorText));
        }
    }

    public String getRemark() {
        return mItemRecord.getText().toString();
    }
}
