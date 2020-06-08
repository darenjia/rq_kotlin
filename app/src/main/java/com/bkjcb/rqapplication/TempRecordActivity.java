package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.adapter.GasTempRecordAdapter;
import com.bkjcb.rqapplication.model.GasRecordModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class TempRecordActivity extends SimpleBaseActivity {

    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private GasTempRecordAdapter adapter;
    private GasRecordModel item;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check;
    }

    @Override
    protected void initView() {
        initTopbar("一户一档(暂存)");
        mRefreshLayout.setEnabled(false);
        adapter = new GasTempRecordAdapter(R.layout.item_temp_record_adapter_view);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        adapter.bindToRecyclerView(mCheckList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                item = (GasRecordModel) adapter.getItem(position);
                GasUserRecordChangeActivity.toActivity(TempRecordActivity.this, item);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                item = (GasRecordModel) adapter.getItem(position);
                showDeleteTip();
            }
        });

    }

    @Override
    protected void initData() {
        StyledDialog.init(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryLocalData();
    }

    private void queryLocalData() {
        showCheckList(GasRecordModel.all());
    }

    protected void showCheckList(List<GasRecordModel> list) {
        if (list != null && list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setNewData(null);
            adapter.setEmptyView(createEmptyView());
        }
    }

    protected View createEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setVisibility(View.GONE);
        return view;
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, TempRecordActivity.class);
        context.startActivity(intent);
    }

    private void showDeleteTip() {
        StyledDialog.buildIosAlert("警告", "当前记录尚未提交，是否删除？", new MyDialogListener() {
            @Override
            public void onFirst() {
                deleteRecord();
                showSnackbar(mRefreshLayout, "删除成功！");
                queryLocalData();
            }

            @Override
            public void onSecond() {

            }
        }).setBtnText("是", "否").show();
    }

    private void deleteRecord() {
        GasRecordModel.remove(item);
    }
}
