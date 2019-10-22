package com.bkjcb.rqapplication;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class LoginActivity extends SimpleBaseActivity {


    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.sign_in_button)
    QMUIRoundButton mSignInButton;
    @BindView(R.id.box_password)
    CheckBox mBox;
    QMUITipDialog tipDialog;
    @Override
    protected int setLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        initRetrofit();
        boolean isRemember = getSharedPreferences().getBoolean("remember",false);
        if (isRemember){
            mBox.setChecked(true);
            mUsername.setText(getSharedPreferences().getString("name",""));
            mPassword.setText(getSharedPreferences().getString("password",""));
        }
        requestPermission();
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
        login(user,password);
    }

    private void login(String name, String password) {
        showLoading();
     /*   disposable = retrofit.create(DataService.class)
                .getuser(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserResult>() {
                    @Override
                    public void accept(UserResult result) throws Exception {
                        tipDialog.dismiss();
                        if (result.isSuccess()) {
                            SampleApplicationLike.user = result;
                            getSharedPreferences().edit().putString("userid", result.getUserid()).apply();
                            loginSuccess();
                        } else {
                            tipDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tipDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });*/
        loginSuccess();
    }

    private void showLoading() {
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
                    .apply();
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mUsername, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
       if (tipDialog.isShowing()){
           tipDialog.dismiss();
           showSnackbar("登录已取消");
           cancelDisposable();
       }else {
           super.onBackPressed();
       }
    }
}
