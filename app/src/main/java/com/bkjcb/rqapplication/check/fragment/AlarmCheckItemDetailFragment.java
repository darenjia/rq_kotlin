package com.bkjcb.rqapplication.check.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.check.model.ApplianceCheckContentItem;
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem;
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem_;
import com.bkjcb.rqapplication.fragment.BaseLazyFragment;

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
public class AlarmCheckItemDetailFragment extends BaseLazyFragment {

    private View view;
    private ApplianceCheckContentItem contentItem;
    private String uid;//项目id
    private ApplianceCheckResultItem checkResultItem;
    private EditText mItemRecord;
    private boolean type = false;

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setContentItem(ApplianceCheckContentItem contentItem) {
        this.contentItem = contentItem;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static AlarmCheckItemDetailFragment newInstances(ApplianceCheckContentItem contentItem, String id) {
        AlarmCheckItemDetailFragment fragment = new AlarmCheckItemDetailFragment();
        fragment.setContentItem(contentItem);
        fragment.setUid(id);
        return fragment;
    }

    public static AlarmCheckItemDetailFragment newInstances(ApplianceCheckContentItem contentItem, String id, boolean type) {
        AlarmCheckItemDetailFragment fragment = new AlarmCheckItemDetailFragment();
        fragment.setContentItem(contentItem);
        fragment.setUid(id);
        fragment.setType(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_check_item_detail, null);
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
        } else {
            checkResultItem = new ApplianceCheckResultItem(uid, contentItem.getGuid());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    private void saveData() {
        checkResultItem.content = mItemRecord.getText().toString();
        ApplianceCheckResultItem.getBox().put(checkResultItem);
    }

    private ApplianceCheckResultItem queryLocalItem() {
        return ApplianceCheckResultItem.getBox().query().equal(ApplianceCheckResultItem_.jianchaid, uid).and().equal(ApplianceCheckResultItem_.jianchaxiangid, contentItem.getGuid()).build().findFirst();
    }

    private void initView(View view) {
        TextView mItemType = (TextView) view.findViewById(R.id.item_type);
        TextView mItemContent = (TextView) view.findViewById(R.id.item_content);
        TextView mItemSection = (TextView) view.findViewById(R.id.item_section);
        TextView mItemBasis = view.findViewById(R.id.item_basis);
        mItemRecord = view.findViewById(R.id.item_record);
        mItemType.setText(contentItem.getCheaktype());
        mItemContent.setText(contentItem.getXuhao() + "、" + contentItem.getCheakname());
        mItemBasis.setVisibility(View.GONE);
        mItemSection.setVisibility(View.GONE);
        if (type){
            mItemRecord.setEnabled(false);
            mItemRecord.setHint("");
        }

    }


}
