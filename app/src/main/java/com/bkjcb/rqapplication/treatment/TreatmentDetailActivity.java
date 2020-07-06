package com.bkjcb.rqapplication.treatment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.SimpleBaseActivity;
import com.bkjcb.rqapplication.treatment.fragment.TreatmentDetailFragment;
import com.bkjcb.rqapplication.treatment.model.DefectTreatmentModel;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentDetailActivity extends SimpleBaseActivity {
    @BindView(R.id.content)
    FrameLayout mContent;
    @BindView(R.id.info_operation)
    QMUIRoundButton mInfoOperation;
    @BindView(R.id.info_export)
    QMUIRoundButton mInfoExport;
    @BindView(R.id.operate_layout)
    LinearLayout mOperateLayout;
    protected DefectTreatmentModel model;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_detail_treatment;
    }

    @Override
    protected void initView() {
        initTopbar(getTitleString());
        model = (DefectTreatmentModel) getIntent().getSerializableExtra("data");
    }

    @Override
    protected void initData() {
        loadView();
        if (model.getFlag()>0){
            mOperateLayout.setVisibility(View.GONE);
        }
    }

    protected String getTitleString() {
        return "事件详情";
    }

    protected void loadView() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, createFragment())
                .commit();
    }

    protected Fragment createFragment() {
        return TreatmentDetailFragment.newInstance(model);
    }

    @OnClick({R.id.info_operation, R.id.info_export})
    public void onClick(View v) {
        handleClick(v.getId());
    }

    protected void handleClick(int id) {
        TreatmentDefectActivity.toActivity(TreatmentDetailActivity.this, model, id == R.id.info_operation);
    }

    public static void toActivity(Activity context, DefectTreatmentModel model) {
        Intent intent = new Intent(context, TreatmentDetailActivity.class);
        intent.putExtra("data", model);
        context.startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 100 && resultCode == 100) {
            mOperateLayout.setVisibility(View.GONE);
            setResult(100);
        }
    }
}
