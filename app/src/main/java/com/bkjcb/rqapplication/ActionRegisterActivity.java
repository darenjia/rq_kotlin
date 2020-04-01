package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bkjcb.rqapplication.adapter.ActionRegisterItemAdapter;
import com.bkjcb.rqapplication.model.ActionRegisterItem;
import com.bkjcb.rqapplication.model.ActionRegisterItem_;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
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
    protected boolean isShowAll = false;

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
                showCheckList();
                refreshLayout.setRefreshing(false);
            }
        });
        mAppbar.setTitle(getTitleString());
        mAppbar.addRightImageButton(R.drawable.vector_drawable_add, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createNew(-1);
                    }
                });
        mAppbar.addRightImageButton(R.drawable.vector_drawable_all, R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIAlphaImageButton button = (QMUIAlphaImageButton) v;
                        button.setImageResource(isShowAll ? R.drawable.vector_drawable_sub : R.drawable.vector_drawable_all);
                        isShowAll = !isShowAll;
                        Toast.makeText(ActionRegisterActivity.this, isShowAll ? "显示未完成" : "显示全部", Toast.LENGTH_SHORT).show();
                        showCheckList();
                    }
                });
        mRegisterList.setLayoutManager(new LinearLayoutManager(this));
        createAdapter();
    }

    protected void createAdapter() {
        adapter = new ActionRegisterItemAdapter(R.layout.item_checkadapter_view);
        mRegisterList.setAdapter(adapter);
        adapter.bindToRecyclerView(mRegisterList);
        adapter.setOnItemClickListener(this);
    }

    protected String getTitleString() {
        return "立案";
    }

    @Override
    protected void initData() {
        showCheckList();
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, ActionRegisterActivity.class);
        context.startActivity(intent);
    }

    private List<ActionRegisterItem> queryLocalData() {
        if (isShowAll) {
            return ActionRegisterItem.getBox().getAll();
        } else {
            return ActionRegisterItem.getBox().query().notEqual(ActionRegisterItem_.status, 2).build().find();
        }
    }

    protected void showCheckList() {
        List<ActionRegisterItem> list = queryLocalData();
        if (list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setNewData(null);
            adapter.setEmptyView(createEmptyView());
        }
    }

    protected View createEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNew(-1);
            }
        });
        return view;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showCheckList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        createNew(position);
    }

    protected void createNew(int position) {
        if (position >= 0) {
            CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, (ActionRegisterItem) adapter.getItem(position));
        } else {
            CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, null);
        }
    }
}
