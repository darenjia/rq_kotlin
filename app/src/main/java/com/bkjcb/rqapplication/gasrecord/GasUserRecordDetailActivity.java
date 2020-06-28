package com.bkjcb.rqapplication.gasrecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.gasrecord.fragment.GasRecordDetailFragment;
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel;
import com.bkjcb.rqapplication.gasrecord.model.GasUserDetailResult;
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult;
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasUserRecordDetailActivity extends AddNewGasUserActivity {


    private String id;
    private String name;
    private boolean needRefresh = false;
    private GasRecordModel recordModel;

    @Override
    protected void initView() {
        boolean isCanChange = MyApplication.getUser().getUserleixing().equals("街镇用户");
        QMUITopBarLayout topBarLayout = initTopbar("一户一档详情");
        topBarLayout.addRightTextButton("复查记录", R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReviewRecordActivity.ToActivity(GasUserRecordDetailActivity.this, id, name);
                    }
                });
        if (isCanChange) {
            topBarLayout.addRightTextButton("修改", R.id.top_right_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GasUserRecordChangeActivity.toActivity(GasUserRecordDetailActivity.this, recordModel);
                        }
                    });
        }
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("ID");
        queryRemoteDta(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (needRefresh) {
            queryRemoteDta(id);
        }
    }

    private void queryRemoteDta(String id) {
        disposable = NetworkApi.getService(GasService.class)
                .getUserDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GasUserDetailResult>() {
                    @Override
                    public void accept(GasUserDetailResult result) throws Exception {
                        if (result.pushState == 200) {
                            needRefresh = false;
                            recordModel = result.getDatas();
                            if (recordModel != null) {
                                name = recordModel.yonghuming;
                                showUserInfoDetail(createGasRecordDetailFragment(null, recordModel));
                            }else {
                                Toast.makeText(GasUserRecordDetailActivity.this, "未获取到一户一档信息，请稍后再试", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected GasRecordDetailFragment createGasRecordDetailFragment(UserInfoResult.UserInfo userInfo, GasRecordModel recordModel) {
        return GasRecordDetailFragment.newInstance(userInfo, recordModel);
    }

    public static void ToActivity(Context context, String id) {
        Intent intent = new Intent(context, GasUserRecordDetailActivity.class);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == GasUserRecordChangeActivity.CHANGE_RECORD) {
            needRefresh = true;
        }
    }
}
