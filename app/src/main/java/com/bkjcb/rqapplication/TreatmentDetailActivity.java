package com.bkjcb.rqapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected int setLayoutID() {
        return R.layout.activity_detail_treatment;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @OnClick({R.id.info_operation, R.id.info_export})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.info_operation:
                break;
            case R.id.info_export:
                break;

        }
    }
}
