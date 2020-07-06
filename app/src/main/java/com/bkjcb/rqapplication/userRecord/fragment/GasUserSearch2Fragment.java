package com.bkjcb.rqapplication.userRecord.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.userRecord.adapter.GasUserAdapter;
import com.bkjcb.rqapplication.base.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.userRecord.model.GasUserRecordResult;
import com.bkjcb.rqapplication.userRecord.retrofit.GasService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasUserSearch2Fragment extends GasUserSearchFragment {
    private GasUserAdapter adapter;
    private OnPageButtonClickListener listener;

    public void setListener(OnPageButtonClickListener listener) {
        this.listener = listener;
    }

    public static GasUserSearch2Fragment newInstance(OnPageButtonClickListener listener) {
        GasUserSearch2Fragment fragment = new GasUserSearch2Fragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_gas_search;
    }

    @Override
    protected void initView() {
        mAddButton.setVisibility(View.GONE);
        mAddressList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GasUserAdapter(R.layout.item_address_view);
        adapter.bindToRecyclerView(mAddressList);
        mAddressList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onClick(adapter.getItem(position));
            }
        });
    }

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        disposable = NetworkApi.getService(GasService.class)
                .getUserList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GasUserRecordResult>() {
                    @Override
                    public void accept(GasUserRecordResult result) throws Exception {
                        if (result.getPushState() == 200) {
                            showResult(result.getDatas());
                            setFilter();
                        } else {
                            setEmptyList();
                            Toast.makeText(getContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        setEmptyList();
                    }
                });

    }

    private void setFilter() {
        disposable = Observable.create(new ObservableOnSubscribe<String>() {
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
                        emitter.onNext(s.toString());
                        mClearBtn.setVisibility(View.VISIBLE);
                    }
                });
                mClearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClearBtn.setVisibility(View.GONE);
                        mSearchView.setText("");
                        emitter.onNext("");
                    }
                });

            }
        }).debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.length() > 0;
                    }
                })
                .doAfterNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        showLoading();
                    }
                })
                .flatMap(new Function<String, Observable<GasUserRecordResult>>() {
                    @Override
                    public Observable<GasUserRecordResult> apply(String s) throws Exception {
                        return NetworkApi.getService(GasService.class)
                                .getUserList(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GasUserRecordResult>() {
                    @Override
                    public void accept(GasUserRecordResult result) throws Exception {
                        if (result.getPushState() == 200) {
                            showResult(result.getDatas());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        setEmptyList();
                    }
                });
    }

    private void showResult(List<GasUserRecordResult.GasUserRecord> list) {
        if (list != null && list.size() > 0) {
            adapter.setNewData(list);
        } else {
            setEmptyList();
        }
    }

    private void showLoading() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.loading_view, (ViewGroup) mAddressList.getParent());
    }

}
