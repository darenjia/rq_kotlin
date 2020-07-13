package com.bkjcb.rqapplication.stationCheck;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.stationCheck.fragment.AlarmCheckItemDetailFragment;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckContentItem;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResult;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;
import com.bkjcb.rqapplication.stationCheck.retrofit.ApplianceCheckService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class AlarmCheckDetailActivity extends ApplianceCheckDetailActivity {


    @Override
    protected void getCheckContent() {
        disposable = NetworkApi.getService(ApplianceCheckService.class)
                .getReportCheckItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApplianceCheckResult>() {
                    @Override
                    public void accept(ApplianceCheckResult result) throws Exception {
                        if (result.pushState == 200 && result.getDatas() != null && result.getDatas().size() > 0) {
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
        return AlarmCheckItemDetailFragment.newInstance(item,id,checkItem.status==3);
    }

    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, AlarmCheckDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, AlarmCheckDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

}
