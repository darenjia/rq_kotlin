package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bkjcb.rqapplication.adapter.GasWorkRecordAdapter;
import com.bkjcb.rqapplication.model.GasUserRecordResult;
import com.bkjcb.rqapplication.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private GasWorkRecordAdapter adapter;
    private int page = 0;
    private boolean isLoadMore = false;
    private String key = "";
    private String district;
    private String street = "";

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_check;
    }

    @Override
    protected void initView() {
        mAppbar.setTitle("一户一档");
        district = MyApplication.getUser().getUserleixing().equals("街镇用户") || MyApplication.getUser().getUserleixing().equals("区用户") ? MyApplication.getUser().getArea().getArea_name() : "";
        street = MyApplication.getUser().getUserleixing().equals("街镇用户") ? MyApplication.getUser().getAreacode().getStreet_jc() : "";
        if (!street.equals("")) {
            mAppbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AddNewGasUserActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });
            mAppbar.addRightImageButton(R.drawable.vector_drawable_temp_list, R.id.top_right_button2)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TempRecordActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });
            mAppbar.addRightImageButton(R.drawable.vector_drawable_setting, R.id.top_right_button1)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SettingActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });
        }

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
       /* initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                page = 0;
                showRefreshLayout(true);
                queryRemoteData();
            }
        });*/
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GasUserRecordResult.GasUserRecord item = (GasUserRecordResult.GasUserRecord) adapter.getItem(position);
                if (street.equals("")) {
                    GasUserRecordDetailActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid());
                } else {
                    GasReviewActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid(), 1, item.getYonghuming());
                }
            }
        });

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

   /* @OnClick({R.id.station_search_close})
    public void onClick(View v) {
        key = mSearchView.getText().toString();
        page = 0;
        isLoadMore = false;
        queryRemoteData();
    }*/

   /* protected void queryRemoteData() {
        adapter.setEnableLoadMore(false);
        disposable = NetworkApi.getService(GasService.class)
                .getWorkRecords(page, 20, district, street, key)
                .compose(RxJavaUtil.getObservableTransformer())
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
    }*/

    private void queryRemoteData() {
        Observable.merge(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mRefreshLayout.setOnRefreshListener(() -> {
                    isLoadMore = false;
                    emitter.onNext(mSearchView.getText().toString());
                });
                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        isLoadMore = true;
                        emitter.onNext(mSearchView.getText().toString());
                    }
                }, mCheckList);
                mSearchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        isLoadMore = false;
                        emitter.onNext(s.toString());
                    }
                });
                mClearBtn.setOnClickListener(v -> {
                    isLoadMore = false;
                    mSearchView.setText("");
                    emitter.onNext("");
                });
            }
        }), Observable.just(""))
                .debounce(500, TimeUnit.MILLISECONDS)
                //.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!isLoadMore) {
                            mRefreshLayout.setRefreshing(true);
                            page = 0;
                        } else {
                            page = adapter.getData().size();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<GasUserRecordResult>>() {
                    @Override
                    public ObservableSource<GasUserRecordResult> apply(String s) throws Exception {
                        return NetworkApi.getService(GasService.class)
                                .getWorkRecords(page, 20, district, street, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GasUserRecordResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(GasUserRecordResult result) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        if (result.pushState == 200) {
                            showCheckList(result.getDatas());
                            adapter.setEnableLoadMore(true);
                        } else {
                            showErrorView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        showErrorView();
                    }

                    @Override
                    public void onComplete() {

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
                //adapter.loadMoreEnd();
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
        if (!street.equals("")) {
            view.findViewById(R.id.empty_button).setOnClickListener(listener);
        } else {
            view.findViewById(R.id.empty_button).setVisibility(View.GONE);
        }
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
      /*  if (MyApplication.user.getAreacode() != null) {
            Intent intent = new Intent(context, GasUserRecordActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "此功能暂只对街镇用户开放", Toast.LENGTH_SHORT).show();
        }*/
        Intent intent = new Intent(context, GasUserRecordActivity.class);
        context.startActivity(intent);
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
