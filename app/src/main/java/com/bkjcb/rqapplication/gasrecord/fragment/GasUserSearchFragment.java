package com.bkjcb.rqapplication.gasrecord.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.treatmentdefect.adapter.AddressItemAdapter;
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult;
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
public class GasUserSearchFragment extends BaseSimpleFragment {
    @BindView(R.id.search_view)
    EditText mSearchView;
    @BindView(R.id.clear_btn)
    ImageView mClearBtn;
    @BindView(R.id.address_list)
    RecyclerView mAddressList;
    @BindView(R.id.btn_add)
    Button mAddButton;
    private AddressItemAdapter adapter;
    private OnPageButtonClickListener listener;
    private int page = 0;
    private boolean loadMore = false;
    private boolean isHideAdd;

    public void setHideAdd(boolean hideAdd) {
        isHideAdd = hideAdd;
    }

    public void setListener(OnPageButtonClickListener listener) {
        this.listener = listener;
    }

    public static GasUserSearchFragment newInstance(OnPageButtonClickListener listener) {
        GasUserSearchFragment fragment = new GasUserSearchFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public static GasUserSearchFragment newInstance(OnPageButtonClickListener listener, boolean isHideAdd) {
        GasUserSearchFragment fragment = new GasUserSearchFragment();
        fragment.setListener(listener);
        fragment.setHideAdd(isHideAdd);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_gas_search;
    }

    @OnClick(R.id.btn_add)
    public void onClick(View v) {
        listener.onNext(null);
    }

    @Override
    protected void initView() {
        mAddressList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddressItemAdapter(R.layout.item_address_view);
        adapter.bindToRecyclerView(mAddressList);
        mAddressList.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onClick((UserInfoResult.UserInfo) adapter.getItem(position));
            }
        });
        if (isHideAdd) {
            mAddButton.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {
        //showLoadingView();
        //getData();
        setFilter();
    }

    private void getData() {
        disposable = NetworkApi.getService(GasService.class)
                .getUserInfo(page, 20, MyApplication.getUser().getArea().getArea_name(), "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfoResult>() {
                    @Override
                    public void accept(UserInfoResult result) throws Exception {
                        if (result.pushState == 200) {
                            showResultList(result.getDatas());
                        } else {
                            showErrorView();
                            Toast.makeText(getContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView();
                    }
                });

    }

    protected void showResultList(List<UserInfoResult.UserInfo> data) {
        if (data != null && data.size() > 0) {
            if (loadMore) {
                adapter.addData(data);
                adapter.loadMoreComplete();
            } else {
                adapter.setNewData(data);
            }
        } else {
            if (loadMore) {
                adapter.loadMoreEnd();
            } else {
                setEmptyList();
            }

        }
    }

    protected void setEmptyList() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.empty_textview, (ViewGroup) mAddressList.getParent());
    }

    protected void showErrorView() {
        if (loadMore) {
            adapter.loadMoreFail();
        } else {
            adapter.setNewData(null);
            adapter.setEmptyView(R.layout.error_view, (ViewGroup) mAddressList.getParent());

        }
    }

    protected void showLoadingView() {
        adapter.setEnableLoadMore(false);
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.loading_view, (ViewGroup) mAddressList.getParent());
    }

    private void setFilter() {
        Observable.merge(Observable.create(new ObservableOnSubscribe<String>() {
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
                        loadMore = false;
                        emitter.onNext(s.toString());
                        mClearBtn.setVisibility(View.VISIBLE);
                    }
                });
                mClearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadMore = false;
                        mClearBtn.setVisibility(View.GONE);
                        mSearchView.setText("");
                        emitter.onNext("");
                    }
                });
                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        loadMore = true;
                        page = adapter.getData().size();
                        emitter.onNext(mSearchView.getText().toString());
                    }
                }, mAddressList);

            }
        }), Observable.just(""))
                .debounce(500, TimeUnit.MILLISECONDS)
                /* .filter(new Predicate<String>() {
                     @Override
                     public boolean test(String s) throws Exception {
                         return s.length() > 0;
                     }
                 })*/
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!loadMore) {
                            showLoadingView();
                            page = 0;
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, Observable<UserInfoResult>>() {
                    @Override
                    public Observable<UserInfoResult> apply(String s) throws Exception {
                        return NetworkApi.getService(GasService.class)
                                .getUserInfo(page, 20, MyApplication.getUser().getArea().getArea_name(), "", s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        showLoadingView();
                    }

                    @Override
                    public void onNext(UserInfoResult result) {
                        if (result.pushState == 200) {
                            showResultList(result.getDatas());
                        } else {
                            showErrorView();
                            Toast.makeText(getContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorView();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
