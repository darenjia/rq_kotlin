package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.model.UserResult;
import com.bkjcb.rqapplication.util.Utils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2019/11/7.
 * Description :
 */
public class SettingActivity extends SimpleBaseActivity {
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_type)
    TextView mUserType;
    @BindView(R.id.user_comp)
    TextView mUserComp;
    @BindView(R.id.introduction)
    SuperTextView mIntroduction;
    @BindView(R.id.clear)
    TextView mClear;
    @BindView(R.id.checkUpload)
    SuperTextView mCheckUpload;
    @BindView(R.id.logout)
    QMUIRoundButton mLogout;
    @BindView(R.id.appbar)
    QMUITopBarLayout mAppbar;
    @BindView(R.id.setting_hide_btn)
    SuperTextView mHideBtn;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initTopbar("设置中心");
        UserResult.User user = MyApplication.getUser();
        if (user != null) {
            mUserName.setText(user.getReal_name());
            mUserType.setText(user.getUserleixing());
            if (user.getAreacode() != null) {
                mUserComp.setText(user.getAreacode().getStreet_jc());
            } else {
                mUserComp.setText(user.getUsercomp());
            }

        }
        mHideBtn.setSwitchIsChecked(getSharedPreferences().getBoolean("hide_finished", true));
    }

    @Override
    protected void initData() {
        mHideBtn.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences().edit().putBoolean("hide_finished", isChecked).apply();
            }
        });
        mCheckUpload.setRightString("v " + Utils.getCurrentVersion());
    }

    @OnClick({R.id.introduction, R.id.clear, R.id.checkUpload, R.id.logout})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.introduction:
                showSnackbar(mAppbar, "当前版本是:" + Utils.getCurrentVersion());
                break;
            case R.id.clear:
                PictureFileUtils.deleteCacheDirFile(this);
                showSnackbar(mAppbar, "缓存已清理！");
                break;
            case R.id.checkUpload:
                Beta.checkUpgrade();
                break;
            case R.id.logout:
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public static void ToActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }
}
