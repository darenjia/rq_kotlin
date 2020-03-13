package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.adapter.ActionRegisterItemAdapter;
import com.bkjcb.rqapplication.model.ActionRegisterItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/3/5.
 * Description :
 */
public class ActionRegisterActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.appbar)
    QMUITopBarLayout mAppbar;
    @BindView(R.id.check_list)
    RecyclerView mRegisterList;
    private ActionRegisterItemAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check;
    }

    @Override
    protected void initView() {
        super.initView();
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCheckList(queryLocalData());
                refreshLayout.setRefreshing(false);
            }
        });
        mAppbar.setTitle("立案列表");
        mAppbar.addRightImageButton(R.drawable.vector_drawable_add, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, null);
                    }
                });
        adapter = new ActionRegisterItemAdapter(R.layout.item_checkadapter_view);
        mRegisterList.setLayoutManager(new LinearLayoutManager(this));
        mRegisterList.setAdapter(adapter);
        adapter.bindToRecyclerView(mRegisterList);

    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener(this);
        showCheckList(queryLocalData());
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, ActionRegisterActivity.class);
        context.startActivity(intent);
    }

    private List<ActionRegisterItem> queryLocalData() {
        return ActionRegisterItem.getBox().getAll();
    }

    private void queryRemateDta() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showCheckList(queryLocalData());
    }

    protected void showCheckList(List<ActionRegisterItem> list) {
        if (list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setEmptyView(createEmptyView());
        }
    }

    private View createEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, null);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, (ActionRegisterItem) adapter.getItem(position));
    }
}
