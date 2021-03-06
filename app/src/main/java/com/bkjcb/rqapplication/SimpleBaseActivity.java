package com.bkjcb.rqapplication;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by DengShuai on 2019/10/14.
 * Description :
 */
public class SimpleBaseActivity extends BaseActivity {

    protected QMUIEmptyView emptyView;
    protected Disposable disposable;

    public SwipeRefreshLayout refreshLayout;
    private QMUIDialog tipDialog;

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

    protected QMUITopBarLayout initTopbarNoBackButton(String title) {
        QMUITopBarLayout barLayout = findViewById(R.id.appbar);
        barLayout.setTitle(title);
        return barLayout;
    }

    protected QMUITopBarLayout initTopbar(String title, int alpha) {
        QMUITopBarLayout barLayout = findViewById(R.id.appbar);
        barLayout.setTitle(title);
        barLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        barLayout.setBackgroundAlpha(alpha);
        return barLayout;
    }

    protected void initEmptyView() {
        emptyView = findViewById(R.id.empty_view);
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
            networkIsOk();
        }
    }

    protected void networkIsOk() {

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

    protected void showErrorView(String detailText, View.OnClickListener listener) {
        if (emptyView != null) {
            if (listener != null) {
                emptyView.show(false, "失败", detailText, "重试", listener);
            } else {
                emptyView.show("失败", detailText);
            }
        }
    }

    protected void hideEmptyView() {
        if (emptyView != null) {
            emptyView.hide();
        }
    }

    protected void showLoadingView() {
        if (emptyView != null) {
            emptyView.show(true, "请稍等", "数据加载中", null, null);
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

    protected void showLoading(boolean isShow) {
        if (tipDialog == null) {
            tipDialog = new QMUIDialog.CustomDialogBuilder(this)
                    .setLayout(R.layout.dialog_view)
                    .setTitle("请稍等")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            if (disposable != null && !disposable.isDisposed()) {
                                disposable.dispose();
                            }
                            dialog.dismiss();
                        }
                    }).create();
            tipDialog.setCanceledOnTouchOutside(false);
            tipDialog.setCancelable(false);
        }
        if (isShow) {
            tipDialog.show();
        } else {
            tipDialog.dismiss();
        }
    }

    private View createDialogView(String tip) {
        View view = View.inflate(this, R.layout.dialog_view, null);
        TextView text = view.findViewById(R.id.dialog_text);
        text.setText(tip);
        return view;
    }
}
