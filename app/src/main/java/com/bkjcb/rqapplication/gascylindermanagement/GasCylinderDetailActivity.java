package com.bkjcb.rqapplication.gascylindermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.gascylindermanagement.adapter.CirculationAdapter;
import com.bkjcb.rqapplication.gascylindermanagement.model.CirculationInfo;
import com.bkjcb.rqapplication.gascylindermanagement.retrofit.QueryService;
import com.kingja.loadsir.callback.Callback;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2021/3/1.
 * Description :
 */
public class GasCylinderDetailActivity extends SimpleBaseActivity implements Callback.OnReloadListener {
    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String Query_TYPE = "type";
    String cardID;
    String cardContent;
    int type;

    @BindView(R.id.content)
    View mContent;
    @BindView(R.id.info_id)
    TextView infoId;
    @BindView(R.id.info_type)
    TextView infoType;
    @BindView(R.id.info_company)
    TextView infoCompany;
    @BindView(R.id.info_date)
    TextView infoDate;
    @BindView(R.id.inf_circulation)
    RecyclerView infCirculation;
    private CirculationAdapter circulationAdapter;


    public static void ToGasCylinderDetailActivity(Context context, String id, String content,int type) {
        Intent intent = new Intent(context, GasCylinderDetailActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(CONTENT, content);
        intent.putExtra(Query_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_cyliner_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        initTopbar("气瓶配送详情");
//        initLoadsir(0, mContent, this);
        initEmptyView();
        infCirculation.setLayoutManager(new LinearLayoutManager(this));
        circulationAdapter = new CirculationAdapter(R.layout.item_circulation);
        infCirculation.setAdapter(circulationAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        cardID = getIntent().getStringExtra(ID);
        cardContent = getIntent().getStringExtra(CONTENT);
//        cardID="17000417";
//        cardContent="311607";
        type = getIntent().getIntExtra(Query_TYPE,1);
        getDataFromNet();
    }

    private void getDataFromNet() {
//        loadService.showCallback(LoadingCallback.class);
        emptyView.show(true);
        NetworkApi.getService(QueryService.class)
                .query(cardID, cardContent,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CirculationInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull CirculationInfo info) {
                        if (info.isSuccess()) {
                            setBaseValues(info.getData());
                            setCirculationList(info.getReturnValue());
                        } else {
//                            loadService.showCallback(ErrorCallback.class);
                            showErrorView(GasCylinderDetailActivity.this::onReload);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
//                        loadService.showCallback(ErrorCallback.class);
                        showErrorView(GasCylinderDetailActivity.this::onReload);
                    }


                    @Override
                    public void onComplete() {
//                        loadService.showSuccess();
                        hideEmptyView();
                    }
                });
    }

    private void setCirculationList(List<CirculationInfo.ReturnValueBean> list) {
        circulationAdapter.replaceData(list);
    }

    private void setBaseValues(CirculationInfo.DataBean data) {
        infoId.setText(data.getGpno());
        infoCompany.setText(data.getMadeName());
        infoDate.setText(data.getMadedate());
        infoType.setText(data.getName());
    }

    @Override
    public void onReload(View v) {
        getDataFromNet();
    }
}
