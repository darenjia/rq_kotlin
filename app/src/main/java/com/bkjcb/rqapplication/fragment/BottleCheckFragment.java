package com.bkjcb.rqapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.treatment.adapter.SecurityCheckListAdapter;
import com.bkjcb.rqapplication.model.BottleSaleCheck;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public class BottleCheckFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.layout_order)
    LinearLayout mLayoutOrder;
    @BindView(R.id.layout_security_check)
    LinearLayout mLayoutSecurityCheck;
    @BindView(R.id.layout_check)
    LinearLayout mLayoutCheck;
    @BindView(R.id.orderList)
    RecyclerView mOrderList;
    private View view;
    private Unbinder unbinder;
    private SecurityCheckListAdapter adapter;
    List<BottleSaleCheck> saleChecks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getActivity(), R.layout.order_data_view, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        mLayoutSecurityCheck.setVisibility(View.VISIBLE);
        mOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SecurityCheckListAdapter(R.layout.item_security_check_view);
        adapter.bindToRecyclerView(mOrderList);
        mOrderList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        setOrderList();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setCheckList(List<BottleSaleCheck> list) {
        this.saleChecks = list;
        setOrderList();
    }

    public void setOrderList() {
        if (adapter != null) {
            if (saleChecks != null && saleChecks.size() > 0) {
                adapter.setNewData(saleChecks);
            } else if (saleChecks == null) {
                setLoadingView();
            } else {
                setEmptyView();
            }
        }
    }

    public void setEmptyView() {
        this.saleChecks = new ArrayList<>();
        if (adapter != null && mOrderList != null) {
            adapter.setNewData(null);
            adapter.setEmptyView(R.layout.empty_textview, (ViewGroup) mOrderList.getParent());
        }
    }

    public void setLoadingView() {
        if (adapter != null && mOrderList != null) {
            adapter.setNewData(null);
            adapter.setEmptyView(R.layout.load_view, (ViewGroup) mOrderList.getParent());
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BottleSaleCheck saleCheck = (BottleSaleCheck) adapter.getData().get(position);
        alertInfo(saleCheck.getCheckRemark());
    }

    private void alertInfo(String message) {
        new QMUIDialog.MessageDialogBuilder(getContext()).setMessage(message).setTitle("安检信息").show();
    }
}
