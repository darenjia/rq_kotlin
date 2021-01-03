package com.bkjcb.rqapplication.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.UserResult;
import com.bkjcb.rqapplication.base.retrofit.DataService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.ActivityManagerTool;
import com.bkjcb.rqapplication.base.util.MD5Util;
import com.bkjcb.rqapplication.base.util.Utils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.IOException;
import java.io.ObjectOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class LoginActivity extends SimpleBaseActivity {
    private int errorCount = 0;

    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.user_tip)
    TextView mUserTip;
    @BindView(R.id.sign_in_button)
    Button mSignInButton;
    @BindView(R.id.box_password)
    CheckBox mBox;
    @BindView(R.id.login_type1)
    RadioButton type1;
    @BindView(R.id.login_type2)
    RadioButton type2;
    @BindView(R.id.login_type3)
    RadioButton type3;
    @BindView(R.id.login_type)
    RadioGroup mRadioGroup;
    @BindView(R.id.app_version)
    TextView mVersionText;
    QMUITipDialog tipDialog;

    private long errorTime;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        resetRadioButtonImage(R.drawable.municipal_bg, type1);
        resetRadioButtonImage(R.drawable.district_bg, type2);
        resetRadioButtonImage(R.drawable.street_bg, type3);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checked(checkedId);
            }
        });
    }

    private void checked(int checkedId) {
        String tipText = "";
        switch (checkedId) {
            case R.id.login_type1:
                tipText = "当前为市级用户";
                type1.setTextColor(getTextColor(R.color.colorApplication));
                type2.setTextColor(getTextColor(R.color.colorDivider));
                type3.setTextColor(getTextColor(R.color.colorDivider));
                break;
            case R.id.login_type2:
                tipText = "当前为区级用户";
                type1.setTextColor(getTextColor(R.color.colorDivider));
                type2.setTextColor(getTextColor(R.color.colorApplication));
                type3.setTextColor(getTextColor(R.color.colorDivider));
                break;
            case R.id.login_type3:
                tipText = "当前为街镇用户";
                type1.setTextColor(getTextColor(R.color.colorDivider));
                type2.setTextColor(getTextColor(R.color.colorDivider));
                type3.setTextColor(getTextColor(R.color.colorApplication));
                break;
            default:
                break;
        }
        mUserTip.setText(tipText);
    }

    private int getTextColor(int color) {
        return getResources().getColor(color);
    }

    private void resetRadioButtonImage(int drawableId, RadioButton radioButton) {
        Drawable drawable_news = getResources().getDrawable(drawableId);
        drawable_news.setBounds(0, 0, Utils.dip2px(this, 48), Utils.dip2px(this, 48));
        radioButton.setCompoundDrawables(null, drawable_news, null, null);
    }

    @Override
    protected void initData() {
        super.initData();
        ActivityManagerTool.getActivityManager().finishAll(this);
        boolean isRemember = getSharedPreferences().getBoolean("remember", false);
        if (isRemember) {
            mBox.setChecked(true);
            mUsername.setText(getSharedPreferences().getString("name", ""));
            mPassword.setText(getSharedPreferences().getString("password", ""));
            String level = getSharedPreferences().getString("level", "");
            if (!TextUtils.isEmpty(level)) {
                if (level.contains("市")) {
                    type1.toggle();
                    checked(R.id.login_type1);
                } else if (level.contains("区")) {
                    type2.toggle();
                    checked(R.id.login_type2);
                } else {
                    type3.toggle();
                    checked(R.id.login_type3);
                }
            }
        }
        errorTime = getSharedPreferences().getLong("limitTime", 0);
        requestPermission();
        mVersionText.setText(String.format("版本号：V %s", Utils.getCurrentVersion()));

    }

    @OnClick(R.id.sign_in_button)
    public void onClick(View v) {
        String user = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (user.trim().length() == 0) {
            showSnackbar("用户名不能为空！");
            return;
        }
        if (password.trim().length() == 0) {
            showSnackbar("请输入密码！");
            return;
        }
        login(user, password);
    }

    private void login(String name, String password) {
        if (errorCount >= 5 || (errorTime > 0 && errorTime + 5 * 60 * 1000 > System.currentTimeMillis())) {
            Toast.makeText(LoginActivity.this, "多次登录失败！请在五分钟后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        disposable = NetworkApi.getService(DataService.class)
                .getLoginUser(name, MD5Util.encode(password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserResult>() {
                    @Override
                    public void accept(UserResult result) throws Exception {
                        tipDialog.dismiss();
                        if (result.pushState == 200) {
                            MyApplication.setUser(result.getDatas());
                            loginSuccess();
                        } else {
                            errorCount++;
                            tipDialog.dismiss();
                            if (errorCount >= 5) {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误！(请在五分钟后重试)", Toast.LENGTH_SHORT).show();
                                saveErrorTime(true);
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误！(剩余" + (5 - errorCount) + "次)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tipDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
        //loginSuccess();
    }

    public void showLoading() {
        if (tipDialog == null) {
            tipDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在登录")
                    .create();
        }
        tipDialog.show();
    }

    private void loginSuccess() {
        if (mBox.isChecked()) {
            getSharedPreferences("user", MODE_PRIVATE).edit()
                    .putString("name", mUsername.getText().toString())
                    .putString("password", mPassword.getText().toString())
                    .putBoolean("remember", true)
                    .putLong("limitTime", 0)
                    .putString("level", MyApplication.getUser().getUserleixing())
                    .apply();
        } else {
            saveErrorTime(false);
        }
//        ServiceUtil.startService(1, MessageService.class,this);
        try {
            ObjectOutputStream os = new ObjectOutputStream(openFileOutput("CacheUser", MODE_PRIVATE));
            os.writeObject(MyApplication.getUser());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (MyApplication.getUser().getUserleixing().equals("街镇用户")) {
//            GasUserRecordActivity.ToActivity(LoginActivity.this);
//        } else {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void showSnackbar(String message) {
        showSnackbar(mUsername, message);
    }

    @Override
    public void onBackPressed() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
            showSnackbar("登录已取消");
            cancelDisposable();
        } else {
            super.onBackPressed();
        }
    }

    private void saveErrorTime(boolean type) {
        getSharedPreferences().edit().putLong("limitTime", type ? System.currentTimeMillis() : 0)
                .apply();
    }

}
