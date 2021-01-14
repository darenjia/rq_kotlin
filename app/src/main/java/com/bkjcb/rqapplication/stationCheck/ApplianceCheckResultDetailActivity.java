package com.bkjcb.rqapplication.stationCheck;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bkjcb.rqapplication.base.ftp.UploadTask;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResultItem;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResultItem_;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;
import com.bkjcb.rqapplication.stationCheck.retrofit.ApplianceCheckService;
import com.google.gson.Gson;

import java.util.ArrayList;
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
    protected void initData() {
        super.initData();
        mInfoExport.setVisibility(View.GONE);
        mInfoModify.setVisibility(View.GONE);
    }

    @Override
    protected void submitResult() {
        showLoading(true);
        List<ApplianceCheckResultItem> list = queryResult();
        List<String> paths = new ArrayList<>();
        if (!TextUtils.isEmpty(checkItem.filePath)) {
            paths.addAll(Arrays.asList(checkItem.filePath.split(",")));
        }
        disposable = UploadTask.createUploadTask(paths, prePath).subscribeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                    @Override
                    public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ApplianceCheckService service = NetworkApi.getService(ApplianceCheckService.class);
                            switch (checkItem.zhandianleixing) {
                                case "维修检查企业":
                                    return service.saveDailyCheck(
                                            null,
                                            checkItem.checkMan,
                                            checkItem.beijiandanweiid,
                                            checkItem.jianchariqi,
                                            checkItem.beizhu,
                                            getItemsID(list),
                                            getItemsResult(list),
                                            getRemoteFilePath(checkItem));
                                case "报警器企业":
                                    return service.saveAlarmDailyCheck(
                                            null,
                                            checkItem.checkMan,
                                            checkItem.beijiandanweiid,
                                            checkItem.jianchariqi,
                                            checkItem.beizhu,
                                            getItemsID(list),
                                            getItemsResultRecord(list),
                                            getRemoteFilePath(checkItem));
                                case "销售企业":
                                    return service.saveSaleDailyCheck(
                                            null,
                                            checkItem.checkMan,
                                            checkItem.beijiandanweiid,
                                            checkItem.jianchariqi,
                                            checkItem.beizhu,
                                            getItemsID(list),
                                            getItemsResult(list),
                                            getRemoteFilePath(checkItem));
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
                        showTipInfo(result.pushState + result.pushMsg);
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

    private String getItemsID(List<ApplianceCheckResultItem> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).jianchaxiangid).append(",");
        }
        return builder.substring(0, builder.length() - 1);
    }

    private String getItemsResult(List<ApplianceCheckResultItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }



    private String getItemsResultRecord(List<ApplianceCheckResultItem> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i).content)) {
                builder.append("").append(",");
            } else {
                builder.append(list.get(i).content).append(",");
            }
        }
        return builder.substring(0, builder.length() - 1);
    }

    private List<ApplianceCheckResultItem> queryResult() {
        return ApplianceCheckResultItem.getBox().query().equal(ApplianceCheckResultItem_.jianchaid, checkItem.c_id).build().find();
    }
}
