package com.bkjcb.rqapplication.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bkjcb.rqapplication.base.loadsir.LoadsirInter;
import com.bkjcb.rqapplication.base.loadsir.LoadsirUtil;
import com.bkjcb.rqapplication.base.loadsir.callback.EmptyCallback;
import com.bkjcb.rqapplication.base.loadsir.callback.ErrorCallback;
import com.bkjcb.rqapplication.base.loadsir.callback.LoadingCallback;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public abstract class BaseSimpleFragment extends BaseFragment implements LoadsirInter {
    protected View view;
    protected Context context;
    protected int resId;
    protected Unbinder unbinder;
    protected LoadService loadService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        setResId();
        view = View.inflate(getActivity(), resId, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return loadService==null?view:loadService.getLoadLayout();
    }

    public abstract void setResId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void initLoadsir(int type, Object view, Callback.OnReloadListener listener) {
        if (type == 0) {
            loadService = LoadsirUtil.register(view, listener);
        } else {
            loadService = LoadsirUtil.registerNormal(view, listener);
        }
    }

    public void showLoading() {
        if (loadService != null) {
            loadService.showCallback(LoadingCallback.class);
        }
    }
    public void showError() {
        if (loadService != null) {
            loadService.showCallback(ErrorCallback.class);
        }
    }
    public void showEmpty() {
        if (loadService != null) {
            loadService.showCallback(EmptyCallback.class);
        }
    }

    public void showResult(SimpleHttpResult<?> result){
        if (loadService != null) {
            loadService.showWithConvertor(result);
        }
    }
    public void showResult(boolean result){
        if (loadService != null) {
            loadService.showWithConvertor(result);
        }
    }

    @Override
    public void showContent() {
        if (loadService!=null){
            loadService.showSuccess();
        }
    }
}
