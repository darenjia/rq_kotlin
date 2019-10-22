package com.bkjcb.rqapplication;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DengShuai on 2019/10/14.
 * Description :
 */
public class SimpleBaseActivity extends BaseActivity {

    protected QMUIEmptyView emptyView;
    protected Retrofit retrofit;
    protected Disposable disposable;
    private static final int PERMISSIONS_REQUEST = 0;
    private boolean useEventBus = false;
    public SwipeRefreshLayout refreshLayout;

    protected QMUITopBarLayout initTopbar(String title) {
        QMUITopBarLayout barLayout = findViewById(R.id.appbar);
        barLayout.setTitle(title);
        barLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //barLayout.setBackgroundAlpha(0);
        return barLayout;
    }

    protected void initTopbar(String title, View.OnClickListener listener) {
        QMUITopBarLayout barLayout = findViewById(R.id.appbar);
        barLayout.setTitle(title);
        barLayout.addLeftBackImageButton().setOnClickListener(listener);
        //barLayout.setBackgroundAlpha(0);
    }

    protected void showNoNetworkView(View.OnClickListener listener) {
        if (emptyView != null) {
            if (listener == null) {
                emptyView.show("加载失败", "请检查网络是否可用");
            } else {
                emptyView.show(false, "加载失败", "请检查网络是否可用", "重试", listener);
            }
        }
    }

    protected void checkNetwork() {
        if (!netIsEnable()) {
            if (emptyView != null) {
                emptyView.show(false, "加载失败", "请检查网络是否可用", "重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkNetwork();
                    }
                });
            }
        } else {
            emptyView.hide();
        }
    }

    protected void showEmptyView(View.OnClickListener listener) {
        if (emptyView != null) {
            if (listener != null) {
                emptyView.show(false, "无数据", "当前记录为空,请刷新试试", "刷新", listener);
            } else {
                emptyView.show("无数据", "当前记录为空,请刷新试试");
            }
        }
    }

    protected void showErrorView(View.OnClickListener listener) {
        if (emptyView != null) {
            if (listener != null) {
                emptyView.show(false, "失败", "获取数据错误", "重试", listener);
            } else {
                emptyView.show("失败", "获取数据错误");
            }
        }
    }

    protected void hasData(List list, boolean isError, View.OnClickListener listener) {
        if (emptyView != null) {
            if (list != null && list.size() > 0 && !isError) {
                emptyView.hide();
            } else if (!isError) {
                showEmptyView(listener);
            } else {
                showErrorView(listener);
            }
        }
    }

    public void initSwipeRefreshLayout(SwipeRefreshLayout.OnRefreshListener listener) {
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(listener);
    }

    public void showRefreshLayout(boolean isShow) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(isShow);
        }
    }

    protected void initRetrofit(String URL) {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    protected void initRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDisposable();
    }

    protected void cancelDisposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    protected SharedPreferences getSharedPreferences() {
        return getSharedPreferences("user", MODE_PRIVATE);
    }

    protected void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
