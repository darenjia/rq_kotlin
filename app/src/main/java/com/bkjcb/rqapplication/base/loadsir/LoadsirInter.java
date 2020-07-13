package com.bkjcb.rqapplication.base.loadsir;

import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.kingja.loadsir.callback.Callback;

/**
 * Created by DengShuai on 2020/7/10.
 * Description :
 */
public interface LoadsirInter {
    void initLoadsir(int type, Object view, Callback.OnReloadListener listener);
    void showLoading();
    void showError();
    void showEmpty();
    void showResult(SimpleHttpResult<?> result);
    void showResult(boolean result);
    void showContent();
}
