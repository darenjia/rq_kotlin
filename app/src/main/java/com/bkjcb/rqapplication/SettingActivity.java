package com.bkjcb.rqapplication;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2019/11/7.
 * Description :
 */
public class SettingActivity extends SimpleBaseActivity {
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.introduction)
    TextView mIntroduction;
    @BindView(R.id.clear)
    TextView mClear;
    @BindView(R.id.checkUpload)
    TextView mCheckUpload;
    @BindView(R.id.logout)
    TextView mLogout;
    @BindView(R.id.appbar)
    QMUITopBarLayout mAppbar;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initTopbar("设置中心");
    }

    @OnClick({R.id.introduction, R.id.clear, R.id.checkUpload, R.id.logout})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.introduction:
                break;
            case R.id.clear:
                break;
            case R.id.checkUpload:
                break;
            case R.id.logout:
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
