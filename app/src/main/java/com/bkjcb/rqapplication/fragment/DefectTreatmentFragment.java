package com.bkjcb.rqapplication.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.TreatmentDetailActivity;
import com.bkjcb.rqapplication.adapter.DefectTreatmentItemAdapter;
import com.bkjcb.rqapplication.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.model.TreatmentResult;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.retrofit.TreatmentService;
import com.chad.library.adapter.base.BaseQuickAdapter;

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
    private DefectTreatmentItemAdapter adapter;
    private boolean isLoadMore = false;
    private int currentCount = 0;
    private int type = 1;
    //    private String code = MyApplication.getUser().getAreacode().getArea_code();
    private String code = "310106021";

    public void setType(int type) {
        this.type = type;
    }

    public static DefectTreatmentFragment newInstance(int type) {
        DefectTreatmentFragment fragment = new DefectTreatmentFragment();
        fragment.setType(type);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_defect_treatment;
    }

    @Override
    protected void initView() {
        adapter = new DefectTreatmentItemAdapter(R.layout.item_defect_treatment);
        adapter.bindToRecyclerView(mContentLayout);
        mContentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentLayout.setAdapter(adapter);
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
            }
        }), Observable.just(""))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!isLoadMore) {
                            adapter.showLoadingView();
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
                        if (treatmentResult.isPushSuccess()) {
                            showResultList(treatmentResult.getDatas());
                            if (adapter.getData().size() >= treatmentResult.getTotalCount()) {
                                adapter.setEnableLoadMore(false);
                            }
                        } else {
                            Toast.makeText(context, "获取数据失败：" + treatmentResult.getPushMsg(), Toast.LENGTH_SHORT).show();
                            showErrorView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, "获取数据错误：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        showErrorView();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TreatmentDetailActivity.toActivity(getContext(), (DefectTreatmentModel) adapter.getItem(position));
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

}
