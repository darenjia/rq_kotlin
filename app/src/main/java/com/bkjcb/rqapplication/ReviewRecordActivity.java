package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.adapter.TimeLineAdapter;
import com.bkjcb.rqapplication.model.ReviewRecord;
import com.bkjcb.rqapplication.model.ReviewRecordResult;
import com.bkjcb.rqapplication.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class ReviewRecordActivity extends SimpleBaseActivity {
    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    private TimeLineAdapter adapter;
    private String id;
    private String name;
    private boolean isCanChange;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_record_check;
    }

    @Override
    protected void initView() {
        isCanChange = MyApplication.getUser().getUserleixing().equals("街镇用户");
        QMUITopBarLayout topBarLayout = initTopbar("复查记录");
        if (isCanChange) {
            topBarLayout.addRightTextButton("复查", R.id.top_right_button1)
                    .setOnClickListener(createClickListener());
        }
        adapter = new TimeLineAdapter(R.layout.item_timeline);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        adapter.bindToRecyclerView(mCheckList);
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRefreshLayout(true);
                queryRemoteData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GasReviewActivity.ToActivity(ReviewRecordActivity.this, ((ReviewRecord) adapter.getItem(position)));
            }
        });
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("ID");
        name = getIntent().getStringExtra("Name");
        queryRemoteData();
    }

    protected void queryRemoteData() {
        disposable = NetworkApi.getService(GasService.class)
                .getRecordList(id, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReviewRecordResult>() {
                    @Override
                    public void accept(ReviewRecordResult result) throws Exception {
                        showRefreshLayout(false);
                        if (result.pushState == 200) {
                            showResultList(result.getData());
                        } else {
                            showError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showRefreshLayout(false);
                        showError();
                    }
                });
    }

    private void showResultList(List<ReviewRecord> list) {
        if (list != null && list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setNewData(null);
            adapter.setEmptyView(createEmptyView(createClickListener()));
        }
    }

    protected View createEmptyView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        if (isCanChange) {
            view.findViewById(R.id.empty_button).setOnClickListener(listener);
        } else {
            view.findViewById(R.id.empty_button).setVisibility(View.GONE);
        }
        return view;
    }

    private void showError() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.error_view);
    }

    protected View.OnClickListener createClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GasReviewActivity.ToActivity(ReviewRecordActivity.this, id, 1, name);
            }
        };

    }

    protected static void ToActivity(Context context, String id, String name) {
        Intent intent = new Intent(context, ReviewRecordActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("Name", name);
        context.startActivity(intent);
    }
}
