package com.bkjcb.rqapplication.infoQuery.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.infoQuery.BusinessFirmInfoDetailActivity;
import com.bkjcb.rqapplication.infoQuery.adapter.BusinessFirmItemAdapter;
import com.bkjcb.rqapplication.infoQuery.model.SimpleBusinessFirmModel;
import com.bkjcb.rqapplication.infoQuery.retrofit.FirmService;
import com.chad.library.adapter.base.BaseQuickAdapter;

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
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class BusinessFirmSearchFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.station_search)
    ImageView mStationSearch;
    @BindView(R.id.station_name)
    EditText mSearchView;
    @BindView(R.id.station_search_close)
    ImageView mStationSearchClose;
    @BindView(R.id.station_list)
    RecyclerView mStationList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    BusinessFirmItemAdapter adapter;
    private boolean isLoadMore;
    private int page;

    @Override
    public void setResId() {
        resId = R.layout.fragment_business_view;
    }

    @Override
    protected void initView() {
        mStationList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BusinessFirmItemAdapter(R.layout.item_business_firm_view);
        mStationList.setAdapter(adapter);
        adapter.bindToRecyclerView(mStationList);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        initDataQuery();
    }

    private void initDataQuery() {
        Observable.mergeArray(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mRefreshLayout.setOnRefreshListener(() -> {
                    isLoadMore = false;
                    emitter.onNext(mSearchView.getText().toString());
                });
            }
        }), Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        isLoadMore = true;
                        emitter.onNext(mSearchView.getText().toString());
                    }
                }, mStationList);
            }
        }), Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
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
            }
        }), Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mStationSearchClose.setOnClickListener(v -> {
                    isLoadMore = false;
                    emitter.onNext("");
                });
            }
        }), Observable.just(""))
                .debounce(200, TimeUnit.MILLISECONDS)
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
                        if (s.length() == 0 && mSearchView.getText().length() > 0) {
                            mSearchView.setText("");
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<SimpleHttpResult<List<SimpleBusinessFirmModel>>>>() {
                    @Override
                    public ObservableSource<SimpleHttpResult<List<SimpleBusinessFirmModel>>> apply(String s) throws Exception {
                        return NetworkApi.getService(FirmService.class)
                                .getjingyingList(page, 20,s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleHttpResult<List<SimpleBusinessFirmModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(SimpleHttpResult<List<SimpleBusinessFirmModel>> result) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        if (result.pushState == 200) {
                            adapter.setEnableLoadMore(result.getTotalCount() > adapter.getData().size() + 20);
                            showDataList(result.getDatas());
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

    private void showDataList(List<SimpleBusinessFirmModel> list) {
        if (list != null && list.size() > 0) {
            if (isLoadMore) {
                adapter.addData(list);
                adapter.loadMoreComplete();
            } else {
                adapter.replaceData(list);
                //adapter.loadMoreEnd();
            }
        } else {
            if (isLoadMore) {
                adapter.loadMoreEnd();
            } else {
                adapter.setNewData(null);
                adapter.showEmptyView();
            }
        }
    }

    private void showErrorView() {
        adapter.setNewData(null);
        adapter.showErrorView();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BusinessFirmInfoDetailActivity.ToActivity(getContext(),((SimpleBusinessFirmModel)adapter.getItem(position)));
    }
}
