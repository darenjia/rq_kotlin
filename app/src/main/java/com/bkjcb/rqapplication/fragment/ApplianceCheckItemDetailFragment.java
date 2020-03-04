package com.bkjcb.rqapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.ApplianceCheckContentItem;
import com.bkjcb.rqapplication.model.ApplianceCheckResultItem;
import com.bkjcb.rqapplication.model.ApplianceCheckResultItem_;

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
public class ApplianceCheckItemDetailFragment extends BaseLazyFragment implements RadioGroup.OnCheckedChangeListener {

    private View view;
    private ApplianceCheckContentItem contentItem;
    private String uid;//项目id
    private ApplianceCheckResultItem checkResultItem;
    private EditText mItemRecord;
    private EditText mItemRemark;
    private RadioButton mCheckResultRadioOk;
    private RadioButton mCheckResultRadioFailure;
    private RadioGroup mCheckResultRadioGroup;
    private boolean type;

    public void setType(boolean type) {
        this.type = type;
    }

    public void setContentItem(ApplianceCheckContentItem contentItem) {
        this.contentItem = contentItem;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static ApplianceCheckItemDetailFragment newInstances(ApplianceCheckContentItem contentItem, String id) {
        ApplianceCheckItemDetailFragment fragment = new ApplianceCheckItemDetailFragment();
        fragment.setContentItem(contentItem);
        fragment.setUid(id);
        return fragment;
    }

    public static ApplianceCheckItemDetailFragment newInstances(ApplianceCheckContentItem contentItem, String id, boolean type) {
        ApplianceCheckItemDetailFragment fragment = new ApplianceCheckItemDetailFragment();
        fragment.setContentItem(contentItem);
        fragment.setUid(id);
        fragment.setType(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_appliance_check_item_detail, null);
            initView(view);

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkResultItem = queryLocalItem();
        if (checkResultItem != null) {
            mItemRecord.setText(checkResultItem.content);
            mItemRemark.setText(checkResultItem.remark);
            if (!TextUtils.isEmpty(checkResultItem.ischeck)) {
                if ("1".equals(checkResultItem.ischeck)) {
                    mCheckResultRadioGroup.check(R.id.check_result_radio_ok);
                    changeTextColor(true);

                } else {
                    mCheckResultRadioGroup.check(R.id.check_result_radio_failure);
                    changeTextColor(false);
                }
            }
        } else {
            checkResultItem = new ApplianceCheckResultItem(uid, contentItem.getGuid());
        }
    }


    private ApplianceCheckResultItem queryLocalItem() {
        return ApplianceCheckResultItem.getBox().query().equal(ApplianceCheckResultItem_.jianchaid, uid).and().equal(ApplianceCheckResultItem_.jianchaxiangid, contentItem.getGuid()).build().findFirst();
    }

    private void initView(View view) {
        TextView mItemType = (TextView) view.findViewById(R.id.item_type);
        TextView mItemContent = (TextView) view.findViewById(R.id.item_content);
        TextView mItemSection = (TextView) view.findViewById(R.id.item_section);
        mCheckResultRadioOk = (RadioButton) view.findViewById(R.id.check_result_radio_ok);
        mCheckResultRadioFailure = (RadioButton) view.findViewById(R.id.check_result_radio_failure);
        mCheckResultRadioGroup = (RadioGroup) view.findViewById(R.id.check_result_radioGroup);
        TextView mItemRecordTitle = (TextView) view.findViewById(R.id.item_record_title);
        mItemRecord = (EditText) view.findViewById(R.id.item_record);
        LinearLayout mItemRecord1 = (LinearLayout) view.findViewById(R.id.item_record1);
        TextView mItemRemarkTitle = (TextView) view.findViewById(R.id.item_remark_title);
        mItemRemark = (EditText) view.findViewById(R.id.item_remark);
        LinearLayout mItemRecord2 = (LinearLayout) view.findViewById(R.id.item_record2);

        mItemType.setText(contentItem.getCheaktype());
        mItemSection.setText(contentItem.getCheakdatail());
        mItemContent.setText(contentItem.getXuhao() + "、" + contentItem.getCheakname());
        if (!TextUtils.isEmpty(contentItem.getCheakrecord())) {
            mItemRecord1.setVisibility(View.VISIBLE);
            mItemRecordTitle.setText(contentItem.getCheakrecord());
        }
        if (!TextUtils.isEmpty(contentItem.getCheakrecord2())) {
            mItemRecord2.setVisibility(View.VISIBLE);
            mItemRemarkTitle.setText(contentItem.getCheakrecord2());
        }
        if (type) {
            mItemRecord.setEnabled(false);
            mItemRemark.setEnabled(false);
        } else {
            mCheckResultRadioGroup.setOnCheckedChangeListener(this);
        }
    }

    private void changeTextColor(boolean isOk) {
        if (isOk) {
            mCheckResultRadioOk.setTextColor(getResources().getColor(R.color.colorText));
            mCheckResultRadioFailure.setTextColor(getResources().getColor(R.color.colorSecondDrayText));
        } else {
            mCheckResultRadioOk.setTextColor(getResources().getColor(R.color.colorSecondDrayText));
            mCheckResultRadioFailure.setTextColor(getResources().getColor(R.color.colorText));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.check_result_radio_ok) {
            checkResultItem.ischeck = "1";
            changeTextColor(true);
        } else {
            checkResultItem.ischeck = "0";
            changeTextColor(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();

    }

    private void saveData() {
        checkResultItem.content = mItemRecord.getText().toString();
        checkResultItem.remark = mItemRemark.getText().toString();
        ApplianceCheckResultItem.getBox().put(checkResultItem);
    }
}
