package com.bkjcb.rqapplication.treatmentdefect.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.treatmentdefect.TreatmentDetailActivity;
import com.bkjcb.rqapplication.treatmentdefect.adapter.DefectTreatmentItemAdapter;
import com.bkjcb.rqapplication.interfaces.OnTextChangeListener;
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.treatmentdefect.model.TreatmentResult;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.treatmentdefect.retrofit.TreatmentService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class DefectTreatmentFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.defect_content_layout)
    RecyclerView mContentLayout;
    @BindView(R.id.station_name)
    EditText mSearchText;
    @BindView(R.id.station_search_close)
    TextView mSearchButton;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private DefectTreatmentItemAdapter adapter;
    private boolean isLoadMore = false;
    private int currentCount = 0;
    private int type = 1;
    private String code = MyApplication.getUser().getAreacode().getArea_code();
    private OnTextChangeListener tempListener;

    public void setType(int type) {
        this.type = type;
    }

    public static DefectTreatmentFragment newInstance(int type) {
        DefectTreatmentFragment fragment = new DefectTreatmentFragment();
        fragment.setType(type);
        return fragment;
    }


    @Override
    protected void initView() {
        adapter = new DefectTreatmentItemAdapter(R.layout.item_defect_treatment);
        adapter.bindToRecyclerView(mContentLayout);
        mContentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentLayout.setAdapter(adapter);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener(this);
        initDisposable();
    }

    private void initDisposable() {
        disposable = Observable.merge(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftInput();
                        isLoadMore = false;
                        emitter.onNext(mSearchText.getText().toString());
                    }
                });
                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        isLoadMore = true;
                        emitter.onNext(mSearchText.getText().toString());
                    }
                }, mContentLayout);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        isLoadMore = false;
                        emitter.onNext(mSearchText.getText().toString());
                    }
                });
                tempListener = new OnTextChangeListener() {
                    @Override
                    public void textChange(String value) {
                        Logger.d("准备刷新");
                        isLoadMore = false;
                        emitter.onNext(mSearchText.getText().toString());
                    }
                };
            }
        }), Observable.just(""))
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!isLoadMore) {
                            refreshLayout.setRefreshing(true);
                            currentCount = 0;
                        } else {
                            currentCount = adapter.getData().size();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<TreatmentResult<DefectTreatmentModel>>>() {
                    @Override
                    public ObservableSource<TreatmentResult<DefectTreatmentModel>> apply(String s) throws Exception {
                        return NetworkApi.getService(TreatmentService.class).getTreatmentList(type, currentCount, 20, code, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TreatmentResult<DefectTreatmentModel>>() {
                    @Override
                    public void accept(TreatmentResult<DefectTreatmentModel> treatmentResult) throws Exception {
                        refreshLayout.setRefreshing(false);
                        if (treatmentResult.isPushSuccess()) {
                            if (20 <= treatmentResult.getTotalCount()) {
                                adapter.setEnableLoadMore(true);
                            } else {
                                adapter.setEnableLoadMore(false);
                                adapter.setOnLoadMoreListener(null);
                            }
                            showResultList(treatmentResult.getDatas());
                        } else {
                            Toast.makeText(context, "获取数据失败：" + treatmentResult.getPushMsg(), Toast.LENGTH_SHORT).show();
                            showErrorView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.setRefreshing(false);
                        Toast.makeText(context, "获取数据错误：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        showErrorView();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TreatmentDetailActivity.toActivity(getActivity(), (DefectTreatmentModel) adapter.getItem(position));
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(mSearchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showResultList(List<DefectTreatmentModel> list) {
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
                adapter.loadMoreEnd();
            } else {
                adapter.showEmptyView();
            }
        }
    }


    protected void showErrorView() {
        if (isLoadMore) {
            adapter.loadMoreFail();
        } else {
            adapter.showErrorView();
        }
    }

    public void refresh() {
        if (tempListener != null) {
            tempListener.textChange("");
        }
    }

    @Override
    public int initResID() {
        return R.layout.fragment_defect_treatment;
    }
}
