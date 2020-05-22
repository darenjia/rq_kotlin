package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.adapter.GasWorkRecordAdapter;
import com.bkjcb.rqapplication.model.GasUserRecordResult;
import com.bkjcb.rqapplication.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasUserRecordActivity extends SimpleBaseActivity {

    @BindView(R.id.appbar)
    QMUITopBarLayout mAppbar;
    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    @BindView(R.id.station_name)
    EditText mSearchView;
    @BindView(R.id.station_search_close)
    TextView mClearBtn;
    private GasWorkRecordAdapter adapter;
    private int page = 0;
    private boolean isLoadMore = false;
    private String key = "";

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_check;
    }

    @Override
    protected void initView() {
        mAppbar.setTitle("一户一档");
        mAppbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddNewGasUserActivity.ToActivity(GasUserRecordActivity.this);
                    }
                });
       /* mAppbar.addRightImageButton(R.drawable.vector_drawable_check, R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GasReviewActivity.ToActivity(GasUserRecordActivity.this);
                    }
                });*/
        adapter = new GasWorkRecordAdapter(R.layout.item_record_adapter_view);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        adapter.bindToRecyclerView(mCheckList);
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                page = 0;
                showRefreshLayout(true);
                queryRemoteData();
            }
        });
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GasUserRecordResult.GasUserRecord item = (GasUserRecordResult.GasUserRecord) adapter.getItem(position);
                GasReviewActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid(), 1, item.getYonghuming());
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page = adapter.getData().size();
                isLoadMore = true;
            }
        }, mCheckList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GasUserRecordResult.GasUserRecord item = (GasUserRecordResult.GasUserRecord) adapter.getItem(position);
                GasUserRecordDetailActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        queryRemoteData();
    }

    @OnClick({R.id.station_search_close})
    public void onClick(View v) {
        key = mSearchView.getText().toString();
        page = 0;
        isLoadMore = false;
        queryRemoteData();
    }

    protected void queryRemoteData() {
        adapter.setEnableLoadMore(false);
        disposable = NetworkApi.getService(GasService.class)
                .getWorkRecords(page, 20, MyApplication.user.getArea().getArea_name(), MyApplication.user.getUsercomp(), key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GasUserRecordResult>() {
                    @Override
                    public void accept(GasUserRecordResult gasUserRecordResult) throws Exception {
                        showRefreshLayout(false);
                        if (gasUserRecordResult.pushState == 200) {
                            showCheckList(gasUserRecordResult.getDatas());
                            adapter.setEnableLoadMore(true);
                        } else {
                            showErrorView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView();
                    }
                });
    }

    protected void showCheckList(List<GasUserRecordResult.GasUserRecord> list) {
        if (list != null && list.size() > 0) {
            if (isLoadMore) {
                adapter.addData(list);
                adapter.loadMoreComplete();
            } else {
                adapter.setNewData(list);
                adapter.loadMoreEnd();
            }
        } else {
            if (isLoadMore) {
                adapter.loadMoreEnd(false);
            } else {
                adapter.setNewData(null);
                adapter.setEmptyView(createEmptyView(createClickListener()));
            }
        }
    }

    protected View createEmptyView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setOnClickListener(listener);
        return view;
    }

    protected View.OnClickListener createClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewGasUserActivity.ToActivity(GasUserRecordActivity.this);
            }
        };

    }

    protected static void ToActivity(Context context) {
        if (MyApplication.user.getAreacode() != null) {
            Intent intent = new Intent(context, GasUserRecordActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "此功能暂只对街镇用户开放", Toast.LENGTH_SHORT).show();
        }
    }

    protected void showErrorView() {
        if (isLoadMore) {
            adapter.loadMoreFail();
        } else {
            showRefreshLayout(false);
            adapter.setNewData(null);
            adapter.setEmptyView(R.layout.error_view, (ViewGroup) mCheckList.getParent());
        }
    }

}
