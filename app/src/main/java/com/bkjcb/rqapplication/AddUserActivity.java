package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.retrofit.DataService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/10/24.
 * Description :
 */
public class AddUserActivity extends SimpleBaseActivity {

    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.userAddress)
    EditText mUserAddress;
    @BindView(R.id.userType)
    MaterialSpinner mUserType;
    @BindView(R.id.scan_btn)
    ImageView mScanBtn;
    @BindView(R.id.scan_des)
    TextView mScanDes;
    private QMUITipDialog tipDialog;
    private List<String> userTypes = Arrays.asList("居民", "非居民");
    private String code;

    public static void ToAddUserActivity(Context context) {
        Intent intent = new Intent(context, AddUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_add_user;
    }

    @Override
    protected void initView() {
        QMUITopBarLayout barLayout = initTopbar("添加新用户");
        barLayout.addRightTextButton("提交", R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendChangeInfo();
                    }
                });
        mUserType.setAdapter(new MaterialSpinnerAdapter<>(this, userTypes));
    }

    private void sendChangeInfo() {
        String name = mUsername.getText().toString();
        String address = mUserAddress.getText().toString();
        if (name.trim().length() == 0 || address.trim().length() == 0) {
            showSnackbar(mUsername, "用户名和地址不能为空");
            return;
        }
        showLoading();
        disposable = NetworkApi.Companion.getService(DataService.class)
                .changeUserInfo(MyApplication.getUser().getAreacode().getArea_code(), name, address, mUserType.getSelectedIndex())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.pushState == 200) {
                            showSnackbar(mUsername, "提交成功");
                        } else {
                            showSnackbar(mUsername, httpResult.pushMsg);
                        }
                        tipDialog.dismiss();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showSnackbar(mUsername, throwable.getMessage());
                        tipDialog.dismiss();
                    }
                });
    }

    @OnClick(R.id.scan_btn)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.scan_btn:
                ScanActivity.ToScanActivity(this, 2);
                break;
        }
    }

    private void showLoading() {
        if (tipDialog == null) {
            tipDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在提交")
                    .create();
            tipDialog.setCanceledOnTouchOutside(false);
            tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancelDisposable();
                }
            });
        }
        tipDialog.show();
    }

    private void changeScanPic(){
        mScanBtn.setImageResource(R.drawable.vector_drawable_qcode);
        mScanDes.setText("已扫描二维码");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 2) {
            if (data != null) {
                code = data.getStringExtra("data");
                changeScanPic();
            }
        }

    }
}
