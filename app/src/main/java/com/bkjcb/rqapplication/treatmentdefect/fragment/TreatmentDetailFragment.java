package com.bkjcb.rqapplication.treatmentdefect.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.treatmentdefect.TreatmentDefectActivity;
import com.bkjcb.rqapplication.treatmentdefect.adapter.OrderListAdapter;
import com.bkjcb.rqapplication.treatmentdefect.adapter.SecurityCheckListAdapter;
import com.bkjcb.rqapplication.treatmentdefect.model.BottleResult;
import com.bkjcb.rqapplication.treatmentdefect.model.BottleSaleCheck;
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.retrofit.DataService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.view.MyDialogViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.config.ConfigBean;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentDetailFragment extends BaseSimpleFragment {
    @BindView(R.id.info_distribution_list)
    RecyclerView mInfoDistributionList;
    @BindView(R.id.info_check_list)
    RecyclerView mInfoCheckList;
    @BindView(R.id.info_result)
    TextView mInfoResult;
    @BindView(R.id.info_accident_type)
    TextView mInfoAccidentType;
    @BindView(R.id.info_opinion)
    TextView mInfoOpinion;
    @BindView(R.id.info_type)
    TextView mInfoType;
    @BindView(R.id.info_station)
    TextView mInfoStation;
    @BindView(R.id.info_date)
    TextView mInfoDate;
    @BindView(R.id.info_year)
    TextView mInfoYear;
    @BindView(R.id.info_name)
    TextView mInfoName;
    @BindView(R.id.defect_detail)
    TextView mDefectDetail;
    private DefectTreatmentModel model;
    private SecurityCheckListAdapter checkAdapter;
    private OrderListAdapter orderAdapter;
    private ConfigBean configBean1;
    private ConfigBean configBean2;

    public void setModel(DefectTreatmentModel model) {
        this.model = model;
    }

    public static TreatmentDetailFragment newInstance(DefectTreatmentModel model) {
        TreatmentDetailFragment fragment = new TreatmentDetailFragment();
        fragment.setModel(model);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_treatment_detail;
    }

    @Override
    protected void initView() {
        StyledDialog.init(context);
        if (model != null) {
            mInfoResult.setText(model.getProcessTime());
            mInfoAccidentType.setText(model.getCasesType());
            mInfoOpinion.setText(model.getOpinions());
            mInfoType.setText(model.getUserCode());
            mInfoStation.setText(model.getUserName());
            mInfoYear.setText(model.getUserType() == 0 ? "居民" : "非居民");
            mInfoName.setText(model.getQu());
            mInfoDate.setText(model.getUserAddress());

            mInfoCheckList.setLayoutManager(new LinearLayoutManager(context));
            checkAdapter = new SecurityCheckListAdapter(R.layout.item_security_check_view);
            mInfoCheckList.setAdapter(checkAdapter);
            checkAdapter.bindToRecyclerView(mInfoCheckList);
            checkAdapter.showLoading();
            checkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    showDetail((BottleSaleCheck) adapter.getItem(position), 1);
                }
            });

            mInfoDistributionList.setLayoutManager(new LinearLayoutManager(context));
            orderAdapter = new OrderListAdapter(R.layout.item_order_view);
            mInfoDistributionList.setAdapter(orderAdapter);
            orderAdapter.bindToRecyclerView(mInfoDistributionList);
            orderAdapter.showLoading();
            orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    showDetail((BottleSaleCheck) adapter.getItem(position), 0);
                }
            });
            if (model.getFlag() == 1) {
                showFinishBtn();
            }
        }
    }

    @Override
    protected void initData() {
        getBaseInfo();
    }

    private void getBaseInfo() {
        disposable = NetworkApi.getService(DataService.class)
                .getBottleData(model.getMbuGuid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BottleResult>() {
                    @Override
                    public void accept(BottleResult result) throws Exception {
                        if (result.pushState == 200) {
                            setCheckData(result.getDatas().getBottleSaleChecks());
                            setOrderData(result.getDatas().getBottleSaleChecks());
                        } else {
                            showError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showError();
                    }
                });
    }

    private void setOrderData(List<BottleSaleCheck> list) {
        if (list != null && list.size() > 0) {
            orderAdapter.setNewData(list);
        } else {
            orderAdapter.showEmpty();
        }
    }

    private void setCheckData(List<BottleSaleCheck> list) {
        if (list != null && list.size() > 0) {
            checkAdapter.setNewData(list);
        } else {
            checkAdapter.showEmpty();
        }
    }

    private void showError() {
        checkAdapter.showError();
        orderAdapter.showError();
    }

    private void showFinishBtn() {
        mDefectDetail.setVisibility(View.VISIBLE);
        mDefectDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreatmentDefectActivity.toActivity(getActivity(), model);
            }
        });
    }

    private void showDetail(BottleSaleCheck check, int type) {
        if (type == 0) {
            if (configBean1 == null) {
                MyDialogViewHolder holder1 = new MyDialogViewHolder(context, 0);
                configBean1 = StyledDialog.buildCustomInIos(holder1, null).setBtnText("确定");
            }
            ((MyDialogViewHolder) configBean1.customContentHolder).assingDatasAndEvents(context, check);
            configBean1.show();
        } else {
            if (configBean2 == null) {
                MyDialogViewHolder holder2 = new MyDialogViewHolder(context, 1);
                configBean2 = StyledDialog.buildCustomInIos(holder2, null).setBtnText("确定");
            }
            ((MyDialogViewHolder) configBean2.customContentHolder).assingDatasAndEvents(context, check);
            configBean2.show();

        }
    }
}
