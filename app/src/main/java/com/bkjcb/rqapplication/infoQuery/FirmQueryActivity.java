package com.bkjcb.rqapplication.infoQuery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.infoQuery.adapter.FirmItemAdapter;
import com.bkjcb.rqapplication.infoQuery.model.BaseFirmModel;
import com.bkjcb.rqapplication.infoQuery.retrofit.FirmService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.ArrayList;
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
public class FirmQueryActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.station_name)
    EditText mStationName;
    @BindView(R.id.station_list)
    RecyclerView mStationList;
    @BindView(R.id.station_search_close)
    ImageView mStationClose;
    @BindView(R.id.station_type)
    MaterialSpinner mStationType;

    public final static String FIRM_TYPE_1 = "经营许可企业";
    public final static String FIRM_TYPE_2 = "安装维修";
    public final static String FIRM_TYPE_3 = "报警器";
    public final static String FIRM_TYPE_4 = "销售备案";

    private String firmType;
    private int firmFlag;
    private FirmItemAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_firm_query;
    }

    @Override
    protected void initView() {
        initTopbar("企业查询");
        List<String> typeList = new ArrayList<>(4);
        typeList.add(FIRM_TYPE_1);
        typeList.add(FIRM_TYPE_2);
        typeList.add(FIRM_TYPE_3);
        typeList.add(FIRM_TYPE_4);
        mStationType.setAdapter(new MaterialSpinnerAdapter<>(this, typeList));
        firmType = FIRM_TYPE_1;
    }

    @Override
    protected void initData() {
        mStationList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FirmItemAdapter(R.layout.item_business_firm_view);
        mStationList.setAdapter(adapter);
        adapter.bindToRecyclerView(mStationList);
        adapter.setOnItemClickListener(this);
        initQueryView();
    }

    private void initQueryView() {
        Observable.merge(Observable.create(new ObservableOnSubscribe<String>() {
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
        }), Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mStationClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitter.onNext("");
                    }
                });
            }
        }), Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mStationType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        firmType = (String) item;
                        firmFlag = position;
                        emitter.onNext("");
                    }
                });
            }
        }), Observable.just("")).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        showLoadingView();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<SimpleHttpResult<List<BaseFirmModel>>>>() {
                    @Override
                    public ObservableSource<SimpleHttpResult<List<BaseFirmModel>>> apply(String s) throws Exception {
                        return NetworkApi.getService(FirmService.class).getComboList(firmType, s);
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleHttpResult<List<BaseFirmModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(SimpleHttpResult<List<BaseFirmModel>> result) {
                        if (result.pushState == 200) {
                            if (result.getDatas() != null && result.getDatas().size() > 0) {
                                adapter.replaceData(result.getDatas());
                            }
                        } else {
                            showErrorView();
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

    @Override
    protected void showLoadingView() {
        adapter.setNewData(null);
        adapter.showLoadingView();
    }

    private void showErrorView() {
        adapter.setNewData(null);
        adapter.showErrorView();
    }

    @Override
    public void showEmpty() {
        adapter.setNewData(null);
        adapter.showEmptyView();
    }

    public static void ToActivity(Context context) {
        context.startActivity(new Intent(context, FirmQueryActivity.class));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BaseFirmModel firmModel = (BaseFirmModel) adapter.getItem(position);
        firmModel.type = firmFlag;
        FirmInfoDetailActivity.ToActivity(this, firmModel);
    }
}
