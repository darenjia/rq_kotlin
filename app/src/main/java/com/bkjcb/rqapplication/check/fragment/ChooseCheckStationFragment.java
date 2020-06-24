package com.bkjcb.rqapplication.check.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.check.adapter.StationAdapter;
import com.bkjcb.rqapplication.check.model.CheckItem;
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.check.model.CheckStation;
import com.bkjcb.rqapplication.check.model.CheckStationResult;
import com.bkjcb.rqapplication.check.retrofit.CheckService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    @BindView(R.id.title)
    TextView mTitle;

    private StationAdapter adapter;
    protected CheckItem checkItem;
    private String stationType = "";
    private List<CheckStation> checkStationList;
    private Disposable editDisposbale;
    private int checkType = 0;

    public void setCheckType(int checkType) {
        this.checkType = checkType;
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
                mStationName.setText("");
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

    public static ChooseCheckStationFragment newInstance(OnChooseListener listener, int type) {
        ChooseCheckStationFragment fragment = new ChooseCheckStationFragment();
        fragment.setListener(listener);
        fragment.setCheckType(type);
        return fragment;
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_check_station_view;
    }

    @Override
    protected void initView() {
        if (checkItem.type == 1) {
            mTitle.setText("企业类型");
        }
        mStationList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StationAdapter(R.layout.item_station_view, checkType);
        mStationList.setAdapter(adapter);
        adapter.bindToRecyclerView(mStationList);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener(this);
        switch (checkItem.zhandianleixing) {
            case "维修检查企业":
                stationType = "安装维修";
                break;
            case "报警器企业":
                stationType = "报警";
                break;
            case "销售企业":
                stationType = "销售";
                break;
            default:
                stationType = checkItem.zhandianleixing;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getStationData();
    }

    protected void getStationData() {
        showRefreshing(true);
        disposable = NetworkApi.getService(CheckService.class)
                .getCheckUnit(stationType,null)
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
                            checkStationList = new ArrayList<>(checkStationResult.getDatas());
                            if (!TextUtils.isEmpty(checkItem.beijiandanweiid)) {
                                for (CheckStation station : checkStationList) {
                                    if (station.getGuid().equals(checkItem.beijiandanweiid)) {
                                        station.setChecked(true);
                                    }
                                }
                            }
                            adapter.setNewData(checkStationList);
                            createEditTextDisposable();
                            mStationName.setText(mStationName.getText());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (editDisposbale != null && !editDisposbale.isDisposed()) {
            editDisposbale.dispose();
        }
    }

    protected void setEmptyView() {
        adapter.setEmptyView(R.layout.empty_textview, (ViewGroup) mStationList.getParent());
    }

    protected void showRefreshing(boolean isShow) {
        mRefreshLayout.setRefreshing(isShow);
    }

    protected void createEditTextDisposable() {
        editDisposbale = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mStationName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        emitter.onNext(s.toString());
                    }
                });
            }
        }).debounce(200, TimeUnit.MILLISECONDS)
               /* .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.trim().length() > 0;
                    }
                })*/
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, ArrayList<CheckStation>>() {
                    @Override
                    public ArrayList<CheckStation> apply(String s) throws Exception {
                        return filterStation(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<CheckStation>>() {
                    @Override
                    public void accept(ArrayList<CheckStation> strings) throws Exception {
                       if (strings.size()==0){
                           adapter.setEmptyView(R.layout.emptyview, (ViewGroup) mStationList.getParent());
                           adapter.setNewData(null);
                       }else {
                           adapter.setNewData(strings);
                       }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ArrayList<CheckStation> filterStation(String s) {
        ArrayList<CheckStation> stations = new ArrayList<>();
        if (s.length()>0) {
            for (CheckStation station : checkStationList) {
                if (!TextUtils.isEmpty(station.getQiyemingcheng())&& station.getQiyemingcheng().contains(s) || !TextUtils.isEmpty(station.getGas_station())&&station.getGas_station().contains(s)) {
                    stations.add(station);
                }
            }
        }else {
            stations.addAll(checkStationList);
        }
        return stations;
    }
}
