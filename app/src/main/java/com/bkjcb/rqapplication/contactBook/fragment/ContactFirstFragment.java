package com.bkjcb.rqapplication.contactBook.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.contactBook.ContactDetailActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.emergency.adapter.EmergencyAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.emergency.model.Emergency;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2020/3/3.
 * Description :
 */
public class ContactFirstFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.btn1)
    TextView mBtn1;
    @BindView(R.id.btn2)
    TextView mBtn2;
    @BindView(R.id.btn3)
    TextView mBtn3;
    @BindView(R.id.header)
    TextView mHeader;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(String tel);
    }

    public static ContactFirstFragment newInstance(OnClickListener listener) {
        ContactFirstFragment fragment = new ContactFirstFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_contact_first;
    }

    @Override
    protected void initView() {
        StyledDialog.init(getContext());
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        EmergencyAdapter adapter = new EmergencyAdapter(R.layout.item_emergency_view);
        mRecycler.setAdapter(adapter);
        adapter.setNewData(getEmergencyData());
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

    }

    private List<Emergency> getEmergencyData() {
        return ContactDataUtil.getAllEmergency();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onClick(View v) {
        int type = 0;
        switch (v.getId()) {
            case R.id.btn1:
                type = 2;
                break;
            case R.id.btn3:
                type = 1;
                break;
        }
        ContactDetailActivity.ToActivity(getContext(), type);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Emergency emergency = (Emergency) adapter.getItem(position);
        if (emergency != null) {
            showDialog(emergency.getTel());
        }
    }

    private void showDialog(String tel) {
        StyledDialog.buildIosAlert("是否拨打此号码", tel, new MyDialogListener() {
            @Override
            public void onFirst() {
                listener.onClick(tel);
            }

            @Override
            public void onSecond() {

            }
        }).setBtnText("拨打", "取消").show();
    }
}
