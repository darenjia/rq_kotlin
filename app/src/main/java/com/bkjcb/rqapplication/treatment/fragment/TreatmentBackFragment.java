package com.bkjcb.rqapplication.treatment.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.treatment.model.DefectDetail;
import com.bkjcb.rqapplication.treatment.model.DefectTreatmentModel;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentBackFragment extends BaseSimpleFragment {


    @BindView(R.id.info_time)
    TextView mInfoTime;
    @BindView(R.id.info_accident_type)
    TextView mInfoAccidentType;
    @BindView(R.id.info_opinion)
    TextView mInfoOpinion;
    @BindView(R.id.info_name)
    TextView mInfoName;
    @BindView(R.id.info_address)
    TextView mInfoAddress;
    @BindView(R.id.record_remark)
    EditText mRecordRemark;
    protected DefectTreatmentModel model;
    protected DefectDetail result;
    protected boolean isCanChange = true;

    public DefectDetail getResult() {
        return result;
    }

    public void setResult(DefectDetail result) {
        this.result = result;
    }

    public void setModel(DefectTreatmentModel model) {
        this.model = model;
    }

    public static TreatmentBackFragment newInstance(DefectTreatmentModel model) {
        TreatmentBackFragment fragment = new TreatmentBackFragment();
        fragment.setModel(model);
        return fragment;
    }

    public static TreatmentBackFragment newInstance(DefectTreatmentModel model, DefectDetail result) {
        TreatmentBackFragment fragment = new TreatmentBackFragment();
        fragment.setModel(model);
        fragment.setResult(result);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_treatment_back;
    }

    @Override
    protected void initView() {
        if (model != null) {
            mInfoTime.setText(model.getUnitDisposalTime());
            mInfoAccidentType.setText(model.getCasesType());
            mInfoOpinion.setText(model.getOpinions());
            mInfoName.setText(model.getUserName());
            mInfoAddress.setText(model.getUserAddress());
        }
    }

    @Override
    protected void initData() {
        if (result == null) {
            result = new DefectDetail();
            result.setUserId(MyApplication.getUser().getUserId());
            result.setMtfId(model.getMtfId());
        } else {
            isCanChange = false;
            setText(mRecordRemark,result.getJzReasons());
        }
    }

    protected void collectParams() {
        result.setJzReasons(getText(mRecordRemark));
        result.setType("2");
        result.setRemotePath("");
    }

    protected boolean verifyData() {
        return verify(mRecordRemark, result.getJzReasons(), "请填写退单理由！");
    }

    protected boolean verify(View view, String value, String tip) {
        if (TextUtils.isEmpty(value)) {
            if (view != null) {
                view.requestFocus();
            }
            Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected String getText(TextView view) {
        return view.getText().toString();
    }

    protected void setText(TextView view, String value) {
        view.setText(value);
        view.setEnabled(isCanChange);
    }

    public DefectDetail prepareSubmit() {
        collectParams();
        return verifyData() ? result : null;
    }
}
