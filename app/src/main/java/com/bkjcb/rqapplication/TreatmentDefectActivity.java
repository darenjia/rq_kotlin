package com.bkjcb.rqapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bkjcb.rqapplication.fragment.TreatmentBackFragment;
import com.bkjcb.rqapplication.fragment.TreatmentDefectFragment;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.model.DefectDetail;
import com.bkjcb.rqapplication.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.retrofit.TreatmentService;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentDefectActivity extends TreatmentDetailActivity {
    private boolean type;//true处置隐患 false退单
    private TreatmentBackFragment fragment;

    @Override
    protected String getTitleString() {
        return type ? "处置隐患" : "任务退回";
    }

    @Override
    protected void initView() {
        type = getIntent().getBooleanExtra("type", true);
        super.initView();
        if (type) {
            mInfoOperation.setText("提交");
            mInfoExport.setVisibility(View.GONE);
        } else {
            mInfoExport.setText("退回");
            mInfoOperation.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        fragment = type ? TreatmentDefectFragment.newInstance(model) : TreatmentBackFragment.newInstance(model);
        super.initData();
    }

    @Override
    protected Fragment createFragment() {
        return fragment;
    }

    @Override
    protected void handleClick(int id) {
        submitData();
    }

    public static void toActivity(Activity context, DefectTreatmentModel model, boolean type) {
        Intent intent = new Intent(context, TreatmentDefectActivity.class);
        intent.putExtra("data", model);
        intent.putExtra("type", type);
        context.startActivityForResult(intent,100);
    }

    private void submitData() {
        DefectDetail detail = fragment.getResult();
        if (detail != null) {
            UploadTask.createUploadTask(detail.getFilePaths(), detail.getRemotePath())
                    .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                        @Override
                        public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                            return aBoolean ? NetworkApi.getService(TreatmentService.class).saveTreatmentDefectResult(detail) : null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResult>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                            showLoading(true);
                        }

                        @Override
                        public void onNext(HttpResult httpResult) {
                            showLoading(false);
                            if (httpResult == null) {
                                showSnackbar(mContent, "文件上传失败,请重试");
                            } else {
                                if (httpResult.pushState == 200) {
                                    showSnackbar(mContent, "提交成功！");
                                    setResult(100);
                                    finish();
                                } else {
                                    showSnackbar(mContent, "提交失败：" + httpResult.pushMsg);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            showLoading(false);
                            showSnackbar(mContent, "提交错误：" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
