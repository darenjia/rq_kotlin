package com.bkjcb.rqapplication.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.adapter.StationAdapter;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.model.CheckStation;
import com.bkjcb.rqapplication.model.CheckStationResult;
import com.bkjcb.rqapplication.retrofit.CheckService;
import com.chad.library.adapter.base.BaseQuickAdapter;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class ChooseCheckStationFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.station_search)
    ImageView mStationSearch;
    @BindView(R.id.station_name)
    EditText mStationName;
    @BindView(R.id.station_search_close)
    ImageView mStationSearchClose;
    @BindView(R.id.station_list)
    RecyclerView mStationList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private StationAdapter adapter;
    private CheckItem checkItem;

    public CheckItem getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    @OnClick({R.id.station_search, R.id.station_search_close, R.id.station_back})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.station_search:
                break;
            case R.id.station_search_close:
                break;
            case R.id.station_back:
                listener.back();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        listener.choose((CheckStation) adapter.getData().get(position));
    }

    @Override
    public void onRefresh() {
        getStationData();
    }

    public interface OnChooseListener {
        void choose(CheckStation type);

        void back();
    }

    private OnChooseListener listener;

    public void setListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public static ChooseCheckStationFragment newInstance(OnChooseListener listener) {
        ChooseCheckStationFragment fragment = new ChooseCheckStationFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_check_station_view;
    }

    @Override
    protected void initView() {
        mStationList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StationAdapter(R.layout.item_station_view);
        mStationList.setAdapter(adapter);
        adapter.bindToRecyclerView(mStationList);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        initRetrofit();
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getStationData();
    }

    private void getStationData() {
        showRefreshing(true);
        disposable = retrofit.create(CheckService.class)
                .getCheckUnit(checkItem.zhandianleixing)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<CheckStationResult>() {
                    @Override
                    public void accept(CheckStationResult checkStationResult) throws Exception {
                        showRefreshing(false);
                        //adapter.setEmptyView(null);
                    }
                })
                .subscribe(new Consumer<CheckStationResult>() {
                    @Override
                    public void accept(CheckStationResult checkStationResult) throws Exception {
                        if (checkStationResult.pushState == 200 && checkStationResult.getDatas() != null && checkStationResult.getDatas().size() > 0) {
                            if (!TextUtils.isEmpty(checkItem.beijiandanweiid)) {
                                for (CheckStation station : checkStationResult.getDatas()) {
                                    if (station.getGuid().equals(checkItem.beijiandanweiid)) {
                                        station.setChecked(true);
                                    }
                                }
                            }
                            adapter.replaceData(checkStationResult.getDatas());
                        } else {
                            setEmptyView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        setEmptyView();
                    }
                });
    }

    private void setEmptyView() {
        adapter.setEmptyView(R.layout.empty_textview, (ViewGroup) mStationList.getParent());
    }

    private void showRefreshing(boolean isShow) {
        mRefreshLayout.setRefreshing(isShow);
    }
}
