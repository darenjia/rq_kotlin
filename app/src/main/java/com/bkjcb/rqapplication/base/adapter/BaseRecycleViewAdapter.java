package com.bkjcb.rqapplication.base.adapter;

import android.content.Context;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/7/8.
 * Description :
 */
public abstract class BaseRecycleViewAdapter<T, V extends BaseViewHolder> extends BaseQuickAdapter<T, V> {


    public BaseRecycleViewAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void showErrorView(Context context, View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.error_view, null);
        view.setOnClickListener(listener);
        setNewData(null);
        setEmptyView(view);
    }

    public void showErrorView() {
        setNewData(null);
        setEmptyView(R.layout.error_view);
    }

    public void showLoadingView() {
        setNewData(null);
        setEmptyView(R.layout.loading_view);
    }

    public void showEmptyView() {
        setNewData(null);
        setEmptyView(R.layout.empty_textview);
    }

    public void initLoadingMoreView() {
        setLoadMoreView(new CustomLoadMoreView());
    }
}
