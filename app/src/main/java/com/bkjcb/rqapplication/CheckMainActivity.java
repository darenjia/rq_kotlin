package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.adapter.CheckItemAdapter;
import com.bkjcb.rqapplication.model.CheckItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class CheckMainActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.appbar)
    QMUITopBarLayout mAppbar;
    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CheckItemAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check;
    }

    @Override
    protected void initView() {
        mAppbar.setTitle("检查列表");
        mAppbar.addRightImageButton(R.drawable.vector_drawable_add, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateCheckTaskActivity.ToActivity(CheckMainActivity.this);
                    }
                });
        adapter = new CheckItemAdapter(R.layout.item_checkadapter_view);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        adapter.bindToRecyclerView(mCheckList);
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setNewData(queryLocalData());
                showRefreshLayout(false);
            }
        });
    }

    @Override
    protected void initData() {
        adapter.setNewData(queryLocalData());
        adapter.setOnItemClickListener(this);
    }

    private List<CheckItem> queryLocalData() {
        return CheckItem.getBox().getAll();
    }

    private void queryRemateDta() {

    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, CheckMainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CheckResultDetailActivity.ToActivity(this, (CheckItem) adapter.getItem(position));
    }
}
