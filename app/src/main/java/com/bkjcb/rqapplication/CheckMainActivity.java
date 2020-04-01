package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bkjcb.rqapplication.adapter.CheckItemAdapter;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.model.CheckItem_;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
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
    private boolean isShowAll = false;
    private int type;

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
                        if (type == 0) {
                            CreateCheckTaskActivity.ToActivity(CheckMainActivity.this);
                        } else {
                            CreateApplianceCheckTaskActivity.ToActivity(CheckMainActivity.this);
                        }
                    }
                });
        mAppbar.addRightImageButton(R.drawable.vector_drawable_all, R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIAlphaImageButton button = (QMUIAlphaImageButton) v;
                        button.setImageResource(isShowAll ? R.drawable.vector_drawable_sub : R.drawable.vector_drawable_all);
                        isShowAll = !isShowAll;
                        Toast.makeText(CheckMainActivity.this, isShowAll ? "显示未完成" : "显示全部", Toast.LENGTH_SHORT).show();
                        showCheckList();
                    }
                });
        //mAppbar.setBackgroundAlpha(0);
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
        type = getIntent().getIntExtra("Type", 0);
        adapter.setOnItemClickListener(this);
        showCheckList();
    }

    private List<CheckItem> queryLocalData() {
        if (isShowAll) {
            return CheckItem.getBox().query().equal(CheckItem_.type, type).build().find();
        } else {
            return CheckItem.getBox().query().equal(CheckItem_.type, type).notEqual(CheckItem_.status, 3).build().find();
        }
    }

    private void queryRemateDta() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showCheckList();
    }

    protected void showCheckList() {
        List<CheckItem> list = queryLocalData();
        if (list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setEmptyView(createEmptyView(createClickListener()));
        }
    }

    protected View.OnClickListener createClickListener() {
        if (type == 0) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateCheckTaskActivity.ToActivity(CheckMainActivity.this);
                }
            };
        } else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateApplianceCheckTaskActivity.ToActivity(CheckMainActivity.this);
                }
            };
        }
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, CheckMainActivity.class);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, int type) {
        Intent intent = new Intent(context, CheckMainActivity.class);
        intent.putExtra("Type", type);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (type == 0) {
            CheckResultDetailActivity.ToActivity(this, (CheckItem) adapter.getItem(position));
        } else {
            ApplianceCheckResultDetailActivity.ToActivity(this, (CheckItem) adapter.getItem(position));
        }
    }

    private View createEmptyView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setOnClickListener(listener);
        return view;
    }
}
