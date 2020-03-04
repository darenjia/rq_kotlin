package com.bkjcb.rqapplication.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkjcb.rqapplication.ContactDetailActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.adapter.EmergencyAdapter;
import com.bkjcb.rqapplication.datebase.ContactDataUtil;
import com.bkjcb.rqapplication.model.Emergency;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2020/3/3.
 * Description :
 */
public class ContactFirstFragment extends BaseSimpleFragment {
    @BindView(R.id.station_search)
    ImageView mStationSearch;
    @BindView(R.id.station_name)
    EditText mStationName;
    @BindView(R.id.station_search_close)
    ImageView mStationSearchClose;
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

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_contact_first;
    }

    @Override
    protected void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        EmergencyAdapter adapter = new EmergencyAdapter(R.layout.item_emergency_view);
        mRecycler.setAdapter(adapter);
        adapter.setNewData(getEmergencyData());
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
}
