package com.bkjcb.rqapplication.stationCheck.fragment;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;
import com.bkjcb.rqapplication.stationCheck.model.ModifyNotificationModel;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/12/31.
 * Description :
 */
public class EditNotificationFragment extends BaseSimpleFragment {
    @BindView(R.id.info_zi)
    EditText mInfoZi;
    @BindView(R.id.info_name)
    EditText mInfoName;
    @BindView(R.id.info_time)
    EditText mInfoTime;
    @BindView(R.id.info_address)
    EditText mInfoAdress;
    @BindView(R.id.info_des)
    EditText mInfoDes;
    @BindView(R.id.info_provision)
    EditText mInfoProvision;
    @BindView(R.id.info_provision_1)
    EditText mInfoProvision1;
    @BindView(R.id.info_provision_2)
    EditText mInfoProvision2;
    @BindView(R.id.info_provision_3)
    EditText mInfoProvision3;
    @BindView(R.id.info_provision1_1)
    EditText mInfoProvision11;
    @BindView(R.id.info_provision1_2)
    EditText mInfoProvision12;
    @BindView(R.id.info_provision1_3)
    EditText mInfoProvision13;
    @BindView(R.id.info_condition1)
    CheckBox mInfoCondition1;
    @BindView(R.id.info_condition2)
    CheckBox mInfoCondition2;
    @BindView(R.id.info_condition2_time)
    EditText mInfoCondition2Time;
    @BindView(R.id.info_content)
    EditText mInfoContent;
    CheckItem checkItem;
    ModifyNotificationModel model;

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public static EditNotificationFragment newInstance(CheckItem checkItem) {
        EditNotificationFragment fragment = new EditNotificationFragment();
        fragment.setCheckItem(checkItem);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_notification_edit;
    }

    @Override
    protected void initView() {
        if (checkItem != null) {
            setTextString(mInfoName, checkItem.beijiandanwei);
        }
    }

    @Override
    protected void initData() {

    }

    protected void setTextString(TextView view, String value) {
        view.setText(value);
    }

    protected String getTextString(EditText view) {
        return view.getText().toString();
    }

    public ModifyNotificationModel saveData() {
        model = new ModifyNotificationModel();
        model.guid = checkItem.c_id;
        model.zi=getTextString(mInfoZi);
        model.name = checkItem.beijiandanwei;
        model.time = getTextString(mInfoTime);
        model.address = getTextString(mInfoAdress);
        model.des = getTextString(mInfoDes);
        model.provision = getTextString(mInfoProvision);
        model.provision1 = getTextString(mInfoProvision1);
        model.provision2 = getTextString(mInfoProvision2);
        model.provision3 = getTextString(mInfoProvision3);
        model.provision11 = getTextString(mInfoProvision11);
        model.provision12 = getTextString(mInfoProvision12);
        model.provision13 = getTextString(mInfoProvision13);
        model.provisionName = getTextString(mInfoProvision1);
        model.content = getTextString(mInfoContent);
        if (mInfoCondition1.isChecked()) {
            model.typeTime = "";
            model.type = 1;
        } else {
            model.type = 2;
            model.typeTime = getTextString(mInfoCondition2Time);
        }
        ModifyNotificationModel.save(model);
        return model;

    }
}
