package com.bkjcb.rqapplication.check;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.check.model.ApplianceCheckContentItem;
import com.bkjcb.rqapplication.check.model.ApplianceCheckResult;
import com.bkjcb.rqapplication.check.model.CheckItem;
import com.bkjcb.rqapplication.check.retrofit.ApplianceCheckService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class SellCheckDetailActivity extends ApplianceCheckDetailActivity {


    @Override
    protected void getDataFromNet() {
        disposable = NetworkApi.getService(ApplianceCheckService.class)
                .getSaleCheckItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApplianceCheckResult>() {
                    @Override
                    public void accept(ApplianceCheckResult result) throws Exception {
                        if (result.pushState == 200 && result.getDatas() != null && result.getDatas().size() > 0) {
                            saveCheckContent(result.getDatas());
                            initCheckData(result.getDatas());
                            initImageListView();
                            hideEmptyView();
                        } else {
                            getDateFail(result.pushMsg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getDateFail(throwable.getMessage());
                    }
                });
    }

    @Override
    protected Fragment createFragment(ApplianceCheckContentItem item, String id) {
        return super.createFragment(item, id);
    }

    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, SellCheckDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, SellCheckDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

}
