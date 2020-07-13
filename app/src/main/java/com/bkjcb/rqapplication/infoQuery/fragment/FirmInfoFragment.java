package com.bkjcb.rqapplication.infoQuery.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.RxJavaUtil;
import com.bkjcb.rqapplication.infoQuery.retrofit.FirmService;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by DengShuai on 2020/7/6.
 * Description :
 */
abstract class FirmInfoFragment<T> extends BaseSimpleFragment {

    protected String code;
    protected QMUIEmptyView emptyView;

    protected void queryDetail() {
        disposable = createObservable().compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<T>>() {
                    @Override
                    public void accept(SimpleHttpResult<T> result) throws Exception {
                        if (result.pushState == 200) {
                            setInfo(result.getDatas());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showError();
                    }
                });
    }

    protected abstract void initEmptyView();

    @Override
    protected void initData() {
        code = getArguments() != null ? getArguments().getString("code") : null;
        if (TextUtils.isEmpty(code)) {
            showError();
        } else {
            queryDetail();
        }
    }


    public void showError() {
       if (emptyView!=null){
           emptyView.show(false, "错误", "获取企业信息失败", "重试", new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   queryDetail();
               }
           });
       }
    }

    @Override
    protected void initView() {
        initEmptyView();
        showLoadingView();
    }

    protected FirmService getService() {
        return NetworkApi.getService(FirmService.class);
    }

    protected abstract void setInfo(T t);

    protected abstract Observable<SimpleHttpResult<T>> createObservable();

    protected void showLoadingView() {
        if (emptyView!=null){
            emptyView.show(true, "请稍后", "正在加载数据", null, null);
        }
    }

    protected void setText(String s, TextView textView) {
        textView.setText(s);
    }

}
