package com.bkjcb.rqapplication;

import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class ScanActivity extends SimpleBaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.zbarview)
    ZBarView mZbarview;
    @BindView(R.id.light_click)
    TextView mLight;
    private boolean lightIsOpen = false;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initView() {
        initTopbar("扫一扫");
        mZbarview.setDelegate(this);
        mZbarview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZbarview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @OnClick(R.id.light_click)
    public void onClick(View view) {
        if (!lightIsOpen) {
            mZbarview.openFlashlight();
            mLight.setText("关闭闪光灯");
            lightIsOpen = true;
        } else {
            mZbarview.closeFlashlight();
            mLight.setText("打开闪光灯");
            lightIsOpen = false;
        }
    }

    @Override
    protected void onStop() {
        mZbarview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZbarview.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        mZbarview.startSpot();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZbarview.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZbarview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZbarview.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showSnackbar(mZbarview, "扫描失败！");
    }
}
