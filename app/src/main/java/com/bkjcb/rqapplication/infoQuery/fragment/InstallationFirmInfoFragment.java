package com.bkjcb.rqapplication.infoQuery.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.util.Utils;
import com.bkjcb.rqapplication.infoQuery.model.InstallationFirmModel;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by DengShuai on 2020/7/6.
 * Description :
 */
public class InstallationFirmInfoFragment extends FirmInfoFragment<List<InstallationFirmModel>> {


    @BindView(R.id.info_id)
    TextView mInfoId;
    @BindView(R.id.info_credit)
    TextView mInfoCredit;
    @BindView(R.id.info_name)
    TextView mInfoName;
    @BindView(R.id.info_register_address)
    TextView mInfoRegisterAddress;
    @BindView(R.id.info_work_address)
    TextView mInfoWorkAddress;
    @BindView(R.id.info_contact)
    TextView mInfoContact;
    @BindView(R.id.info_tel)
    TextView mInfoTel;
    @BindView(R.id.info_repair_product)
    TextView mInfoRepairProduct;
    @BindView(R.id.info_repair_brand)
    TextView mInfoRepairBrand;
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

    public static InstallationFirmInfoFragment newInstance(String code) {
        InstallationFirmInfoFragment fragment = new InstallationFirmInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initEmptyView() {
        mEmptyView = emptyView;
    }

    @Override
    protected void setInfo(List<InstallationFirmModel> installationFirmModels) {
        if (installationFirmModels != null && installationFirmModels.size() > 0) {
            InstallationFirmModel model = installationFirmModels.get(0);
            setText(model.getLicense_code(), mInfoId);
            setText(model.getCredit_code(), mInfoCredit);
            setText(model.getUnit_name(), mInfoName);
            setText(model.getPerson_address(), mInfoRegisterAddress);
            setText(model.getUnit_address(), mInfoWorkAddress);
            setText(model.getFix_item(), mInfoRepairProduct);
            setText(model.getFix_brand(), mInfoRepairBrand);
            setText(model.getContact_phone(), mInfoTel);
            setText(model.getContact_person(), mInfoContact);
            setText(Utils.dateFormat(model.getFirstsenddate()), mInfoDateFirst);
            setText(Utils.dateFormat(model.getSenddate()), mInfoDate);
            setText(Utils.dateFormat(model.getStartdate()), mInfoDateStart);
            setText(Utils.dateFormat(model.getEnd_date()), mInfoDateEnd);
        }
    }

    @Override
    protected Observable<SimpleHttpResult<List<InstallationFirmModel>>> createObservable() {
        return getService().getanzhuangweixiumx(code);
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_installation_firm;
    }

    @Override
    protected void initView() {
        super.initView();
    }

}
