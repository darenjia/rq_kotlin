package com.bkjcb.rqapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckContentItem;
import com.bkjcb.rqapplication.model.CheckResultItem;
import com.bkjcb.rqapplication.model.CheckResultItem_;

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
public class CheckItemDetailFragment extends BaseLazyFragment {

    private View view;
    private CheckContentItem contentItem;
    private String uid;//项目id
    private CheckResultItem checkResultItem;
    private TextView mItemType;
    private TextView mItemContent;
    private TextView mItemBasis;
    private TextView mItemSection;
    private EditText mItemRecord;

    public void setContentItem(CheckContentItem contentItem) {
        this.contentItem = contentItem;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static CheckItemDetailFragment newInstances(CheckContentItem contentItem, String id) {
        CheckItemDetailFragment fragment = new CheckItemDetailFragment();
        fragment.setContentItem(contentItem);
        fragment.setUid(id);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_check_item_detail, null);
            mItemType = view.findViewById(R.id.item_type);
            mItemContent = view.findViewById(R.id.item_content);
            mItemBasis = view.findViewById(R.id.item_basis);
            mItemSection = view.findViewById(R.id.item_section);
            mItemRecord = view.findViewById(R.id.item_record);
            mItemType.setText(contentItem.getLeibie());
            mItemContent.setText(contentItem.getXuhao()+"、"+contentItem.getJianchaneirong());
            mItemBasis.setText(contentItem.getJianchayiju());
            mItemSection.setText(contentItem.getJianchalanmu().replace("<br/>","/"));
        }
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {

    }

   /* @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/

    @Override
    public void onStart() {
        super.onStart();
        checkResultItem = queryLocalItem();
        if (checkResultItem != null) {
            mItemRecord.setText(checkResultItem.jianchajilu);
        } else {
            checkResultItem = new CheckResultItem(uid, contentItem.getId());
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (!isVisible) {
            checkResultItem.jianchajilu = mItemRecord.getText().toString();
            CheckResultItem.getBox().put(checkResultItem);
        }
    }

    private CheckResultItem queryLocalItem() {
        return CheckResultItem.getBox().query().equal(CheckResultItem_.jianchaid, uid).and().equal(CheckResultItem_.jianchaxiangid, contentItem.getId()).build().findFirst();
    }
}
