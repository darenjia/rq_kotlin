package com.bkjcb.rqapplication.treatmentdefect;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.treatmentdefect.fragment.TreatmentBackFragment;
import com.bkjcb.rqapplication.treatmentdefect.fragment.TreatmentDefectFragment;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetail;
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetailResult;
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.treatmentdefect.retrofit.TreatmentService;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import butterknife.BindView;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentDefectActivity extends TreatmentDetailActivity {
    private boolean type;//true处置隐患 false退单
    private TreatmentBackFragment fragment;
    @BindView(R.id.empty_view)
    QMUIEmptyView emptyView;

    @Override
    protected String getTitleString() {
        return type ? "处置隐患" : "任务退回";
    }

    @Override
    protected void initView() {
        type = getIntent().getBooleanExtra("type", true);
        super.initView();
        if (model.getFlag() == 0) {
            if (type) {
                mInfoOperation.setText("提交");
                mInfoExport.setVisibility(View.GONE);
            } else {
                mInfoExport.setText("退回");
                mInfoOperation.setVisibility(View.GONE);
            }
        } else {
            mOperateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        if (model.getFlag() == 1) {
            getDefectDetail();
        } else {
            fragment = type ? TreatmentDefectFragment.newInstance(model) : TreatmentBackFragment.newInstance(model);
            loadView();
        }
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
        context.startActivityForResult(intent, 100);
    }

    public static void toActivity(Activity context, DefectTreatmentModel model) {
        Intent intent = new Intent(context, TreatmentDefectActivity.class);
        intent.putExtra("data", model);
        context.startActivity(intent);
    }

    private void submitData() {
        DefectDetail detail = fragment.prepareSubmit();
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
                                    Toast.makeText(TreatmentDefectActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
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

    private void getDefectDetail() {
        emptyView.show(true, "请稍等", "正在获取数据", null, null);
        disposable = NetworkApi.getService(TreatmentService.class)
                .getTreatmentDefectDetail(model.getMtfId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DefectDetailResult>() {
                    @Override
                    public void accept(DefectDetailResult result) throws Exception {
                        if (result.isPushSuccess()) {
                            emptyView.hide();
                            fragment = TreatmentDefectFragment.newInstance(model, result.getDatas());
                            loadView();
                        } else {
                            showErrorView(result.pushMsg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView(throwable.getMessage());
                    }
                });
    }

    private void showErrorView(String tip) {
        emptyView.show(false, "获取失败", tip, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDefectDetail();
            }
        });
    }
}
