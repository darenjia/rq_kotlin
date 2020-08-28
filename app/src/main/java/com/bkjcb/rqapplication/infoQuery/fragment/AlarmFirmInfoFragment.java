package com.bkjcb.rqapplication.infoQuery.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.util.Utils;
import com.bkjcb.rqapplication.infoQuery.model.AlarmFirmModel;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by DengShuai on 2020/7/7.
 * Description :
 */
public class AlarmFirmInfoFragment extends FirmInfoFragment<List<AlarmFirmModel>> {
    @BindView(R.id.info_id)
    TextView mInfoId;
    @BindView(R.id.info_credit)
    TextView mInfoCredit;
    @BindView(R.id.info_product)
    TextView mInfoProduct;
    @BindView(R.id.info_product_address)
    TextView mInfoProductAddress;
    @BindView(R.id.info_code)
    TextView mInfoCode;
    @BindView(R.id.info_sale)
    TextView mInfoSale;
    @BindView(R.id.info_sale_address)
    TextView mInfoSaleAddress;
    @BindView(R.id.info_tel)
    TextView mInfoTel;
    @BindView(R.id.info_contact)
    TextView mInfoContact;
    @BindView(R.id.info_date_first)
    TextView mInfoDateFirst;
    @BindView(R.id.info_date)
    TextView mInfoDate;
    @BindView(R.id.info_date_start)
    TextView mInfoDateStart;
    @BindView(R.id.info_date_end)
    TextView mInfoDateEnd;
    @BindView(R.id.empty_view)
    QMUIEmptyView mEmptyView;

    public static AlarmFirmInfoFragment newInstance(String code) {
        AlarmFirmInfoFragment fragment = new AlarmFirmInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initEmptyView() {
        this.mEmptyView = emptyView;
    }

    @Override
    protected void setInfo(List<AlarmFirmModel> alarmFirmModels) {
        if (alarmFirmModels != null && alarmFirmModels.size() > 0) {
            AlarmFirmModel model = alarmFirmModels.get(0);
            setText(model.getBeian_code(), mInfoId);
            setText(model.getCredit_code(), mInfoCredit);
            setText(model.getProduct_unit(), mInfoProduct);
            setText(model.getProduct_address(), mInfoProductAddress);
            setText(model.getSale_zipcode(), mInfoCode);
            setText(model.getSale_unit(), mInfoSale);
            setText(model.getSale_address(), mInfoSaleAddress);
            setText(model.getContact_phone(), mInfoTel);
            setText(model.getContact_people(), mInfoContact);
            setText(Utils.dateFormat(model.getFirstsenddate()), mInfoDateFirst);
            setText(Utils.dateFormat(model.getSenddate()), mInfoDate);
            setText(Utils.dateFormat(model.getStartdate()), mInfoDateStart);
            setText(Utils.dateFormat(model.getEnddate()), mInfoDateEnd);
        }
    }

    @Override
    protected Observable<SimpleHttpResult<List<AlarmFirmModel>>> createObservable() {
        return getService().getbaojingqimx(code);
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_sell_firm;
    }
}