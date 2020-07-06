package com.bkjcb.rqapplication.userRecord;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.userRecord.adapter.GasStatisticAdapter;
import com.bkjcb.rqapplication.userRecord.model.GasStatisticData;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.userRecord.retrofit.GasService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.DataUtil;
import com.bkjcb.rqapplication.base.util.RxJavaUtil;
import com.bkjcb.rqapplication.base.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by DengShuai on 2020/6/30.
 * Description :
 */
public class GasUserStatisticActivity extends SimpleBaseActivity {
    @BindView(R.id.gas_statistic_list)
    RecyclerView mStatisticList;
    @BindView(R.id.statistic_all)
    SuperTextView mStatisticAll;
    @BindView(R.id.statistic_increase)
    SuperTextView mStatisticIncrease;
    @BindView(R.id.statistic_type1)
    SuperTextView mStatisticType1;
    @BindView(R.id.statistic_type2)
    SuperTextView mStatisticType2;
    private GasStatisticAdapter adapter;
    private String districtName = "";

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_user_statistic;
    }

    @Override
    protected void initView() {
        initTopbar("一户一档");
        mStatisticList.setLayoutManager(new LinearLayoutManager(this));
        List<GasStatisticData> list = new ArrayList<>();
        adapter = new GasStatisticAdapter(list);
        mStatisticList.setAdapter(adapter);
        initEmptyView();
        showLoadingView();
    }

    public static void toActivity(Context context) {
        context.startActivity(new Intent(context, GasUserStatisticActivity.class));
    }

    @Override
    protected void initData() {
        super.initData();
        if (!MyApplication.getUser().getUserleixing().equals("市用户")) {
            districtName = MyApplication.getUser().getArea().getArea_name();
        }
        getData();
    }

    private void getListData() {
        if (DataUtil.getInstance().getList() != null) {
            setListData();
            hideEmptyView();
            return;
        }
        disposable = NetworkApi.getService(GasService.class)
                .getStatisticData(districtName)
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<List<GasStatisticData>>>() {
                    @Override
                    public void accept(SimpleHttpResult<List<GasStatisticData>> result) throws Exception {
                        if (result.pushState == 200) {
                            DataUtil.getInstance().setList(result.getDatas());
                            setListData();
                            hideEmptyView();
                        } else {
                            showErrorView(null);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView(null);
                    }
                });
    }

    @OnClick({R.id.statistic_detail})
    public void onClick(View v) {
        GasUserRecordActivity.ToActivity(this);
    }

    private void setListData() {
        adapter.setNewData(new ArrayList<>(DataUtil.getInstance().getList()));
        if (DataUtil.getInstance().getList().size() == 1) {
            adapter.expandAll();
            mStatisticAll.setCenterTopString("全区");
        }
    }

    private void getData() {
        disposable = NetworkApi.getService(GasService.class)
                .getStatisticDataSimple(districtName, Utils.getCurrentTime(),"")
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<GasStatisticData>>() {
                    @Override
                    public void accept(SimpleHttpResult<GasStatisticData> result) throws Exception {
                        if (result.pushState == 200) {
                            mStatisticAll.setCenterString(result.getDatas().getGs());
                            mStatisticIncrease.setCenterString(result.getDatas().getJrgs());
                            mStatisticType1.setCenterString(result.getDatas().getTiaoyafa());
                            mStatisticType2.setCenterString(result.getDatas().getXihuobaohu());
                            getListData();
                        } else {
                            showErrorView(null);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView(null);
                    }
                });
    }

}
