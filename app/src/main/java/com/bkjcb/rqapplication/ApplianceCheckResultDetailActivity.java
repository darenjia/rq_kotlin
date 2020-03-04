package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.ftp.FtpUtils;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.model.ApplianceCheckResultItem;
import com.bkjcb.rqapplication.model.ApplianceCheckResultItem_;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.retrofit.ApplianceCheckService;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class ApplianceCheckResultDetailActivity extends CheckResultDetailActivity {

    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, ApplianceCheckResultDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, ApplianceCheckResultDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void submitResult() {
        showLoading(true);
        List<ApplianceCheckResultItem> list = queryResult();
        disposable = UploadTask.createUploadTask(Arrays.asList(checkItem.filePath.split(",")), "", new FtpUtils.UploadProgressListener() {
            @Override
            public void onUploadProgress(String currentStep, long uploadSize, long size, File file) {

            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                    @Override
                    public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ApplianceCheckService service = retrofit.create(ApplianceCheckService.class);
                            switch (checkItem.zhandianleixing) {
                                case "维修检查企业":
                                    return service.saveDailyCheck(
                                            checkItem.c_id,
                                            MyApplication.user.getUserName(),
                                            checkItem.beijiandanweiid,
                                            checkItem.jianchariqi,
                                            checkItem.beizhu,
                                            getItemsID(list),
                                            getItemsResult(list));
                                case "报警器企业":
                                    return service.saveAlarmDailyCheck(
                                            checkItem.c_id,
                                            MyApplication.user.getUserName(),
                                            checkItem.beijiandanweiid,
                                            checkItem.jianchariqi,
                                            checkItem.beizhu,
                                            getItemsID(list),
                                            getItemsResultRecord(list));
                                case "销售企业":
                                    return service.saveSaleDailyCheck(
                                            checkItem.c_id,
                                            MyApplication.user.getUserName(),
                                            checkItem.beijiandanweiid,
                                            checkItem.jianchariqi,
                                            checkItem.beizhu,
                                            getItemsID(list),
                                            getItemsResult(list));
                                default:
                                    return null;
                            }
                        } else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult result) throws Exception {
                        if (result.pushState == 200) {
                            updateTaskStatus();
                        }
                        showTipInfo(result.pushMsg);
                        showLoading(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showLoading(false);
                        showTipInfo("上传失败！" + throwable.getMessage());
                    }
                });
    }

    @Override
    protected void toCheckContentActivity() {
        switch (checkItem.zhandianleixing) {
            case "维修检查企业":
                ApplianceCheckDetailActivity.ToActivity(this, checkItem.id);
                break;
            case "报警器企业":
                AlarmCheckDetailActivity.ToActivity(this, checkItem.id);
                break;
            case "销售企业":
                SellCheckDetailActivity.ToActivity(this, checkItem.id);
                break;
        }


    }

    private String[] getItemsID(List<ApplianceCheckResultItem> list) {
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = list.get(i).jianchaxiangid;
        }
        return strings;
    }

    private String[] getItemsResult(List<ApplianceCheckResultItem> list) {
        Gson gson = new Gson();
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = gson.toJson(list.get(i));
        }
        return strings;
    }

    private String[] getItemsResultRecord(List<ApplianceCheckResultItem> list) {
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = list.get(i).content;
        }
        return strings;
    }

    private List<ApplianceCheckResultItem> queryResult() {
        return ApplianceCheckResultItem.getBox().query().equal(ApplianceCheckResultItem_.jianchaid, checkItem.c_id).build().find();
    }
}
