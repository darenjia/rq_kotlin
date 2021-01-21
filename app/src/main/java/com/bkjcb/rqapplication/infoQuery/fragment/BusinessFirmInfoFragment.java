package com.bkjcb.rqapplication.infoQuery.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.util.Utils;
import com.bkjcb.rqapplication.infoQuery.model.BusinessFirmModel;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by DengShuai on 2020/7/7.
 * Description :
 */
public class BusinessFirmInfoFragment extends FirmInfoFragment<List<BusinessFirmModel>> {


    @BindView(R.id.info_name)
    TextView mInfoName;
    @BindView(R.id.info_legal_person)
    TextView mInfoLegalPerson;
    @BindView(R.id.info_register_address)
    TextView mInfoRegisterAddress;
    @BindView(R.id.info_business_address)
    TextView mInfoBusinessAddress;
    @BindView(R.id.info_code)
    TextView mInfoCode;
    @BindView(R.id.info_contact)
    TextView mInfoContact;
    @BindView(R.id.info_tel)
    TextView mInfoTel;
    @BindView(R.id.info_apply_type)
    TextView mInfoApplyType;
    @BindView(R.id.info_sale_type)
    TextView mInfoSaleType;
    @BindView(R.id.info_card)
    TextView mInfoCard;
    @BindView(R.id.info_credit)
    TextView mInfoCredit;
    @BindView(R.id.info_id)
    TextView mInfoId;
    @BindView(R.id.info_address)
    TextView mInfoAddress;
    @BindView(R.id.info_person_role)
    TextView mInfoPersonRole;
    @BindView(R.id.info_date_first)
    TextView mInfoDateFirst;
    @BindView(R.id.info_date)
    TextView mInfoDate;
    @BindView(R.id.info_date_start)
    TextView mInfoDateStart;
    @BindView(R.id.info_date_end)
    TextView mInfoDateEnd;
    @BindView(R.id.info_location_district)
    TextView mInfoLocationDistrict;
    @BindView(R.id.info_manage_district)
    TextView mInfoManageDistrict;
    @BindView(R.id.empty_view)
    QMUIEmptyView mEmptyView;


    public static BusinessFirmInfoFragment newInstance(String code) {
        BusinessFirmInfoFragment fragment = new BusinessFirmInfoFragment();
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
    protected void setInfo(List<BusinessFirmModel> alarmFirmModels) {
        if (alarmFirmModels != null && alarmFirmModels.size() > 0) {
            BusinessFirmModel model = alarmFirmModels.get(0);
            setText(model.getQiyemingcheng(), mInfoName);
            setText(model.getFadingdaibiaoren(), mInfoLegalPerson);
            setText(model.getZhusuo(), mInfoRegisterAddress);
            setText(model.getYingyedizhi(), mInfoBusinessAddress);
            setText(model.getYoubian(), mInfoCode);
            setText(model.getLianxiren(), mInfoContact);
            setText(model.getDianhua(), mInfoTel);
            setText(model.getShenqingleixing(), mInfoApplyType);
            setText(model.getJingyingleixing(), mInfoSaleType);
            setText(model.getBusinesslicenseuniformid(), mInfoCard);
            setText(model.getTongyixinyongdaima(), mInfoCredit);
            setText(model.getXukezhengbianhao(), mInfoId);
            setText(model.getContractaddress(), mInfoAddress);
            setText(model.getContractduty(), mInfoPersonRole);
            setText(model.getJingyingleixing(), mInfoSaleType);
            setText(model.getRegionname(), mInfoLocationDistrict);
            setText(model.getGuanliqu(), mInfoManageDistrict);



            setText(Utils.dateFormat(model.getFirstsenddate()), mInfoDateFirst);
            setText(Utils.dateFormat(model.getSenddate()), mInfoDate);
            setText(Utils.dateFormat(model.getStartdate()), mInfoDateStart);
            setText(Utils.dateFormat(model.getYouxiaojiezhiriqi()), mInfoDateEnd);
        }
    }

    @Override
    protected Observable<SimpleHttpResult<List<BusinessFirmModel>>> createObservable() {
        return getService().getjingyingmx(code);
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_business_firm;
    }

}
