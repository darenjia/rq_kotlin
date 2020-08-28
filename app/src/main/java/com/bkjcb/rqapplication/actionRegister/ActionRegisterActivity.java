package com.bkjcb.rqapplication.actionRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.actionRegister.adapter.ActionRegisterItemAdapter;
import com.bkjcb.rqapplication.actionRegister.model.ActionRegisterItem;
import com.bkjcb.rqapplication.actionRegister.model.ActionRegisterItem_;
import com.bkjcb.rqapplication.actionRegister.retrofit.ActionRegisterService;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.interfaces.OnTextChangeListener;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/3/5.
 * Description :
 */
public class ActionRegisterActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.check_list)
    public RecyclerView mRegisterList;
    @BindView(R.id.station_search_close)
    Button mSearchButton;
    @BindView(R.id.station_name)
    EditText mSearchKey;
    private ActionRegisterItemAdapter adapter;
    protected boolean isShowAll = false;
    private int start = 0;
    private boolean hasMore = true;
    private boolean isLoadMore = false;
    private OnTextChangeListener changeListener;
    private List<ActionRegisterItem> result = new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check;
    }

    @Override
    protected void initView() {
        super.initView();
        getHideSetting();
        QMUITopBarLayout mAppbar = initTopbar(getTitleString());
        mAppbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createNew(-1);
                    }
                });
        mAppbar.addRightImageButton(getButtonDrawableResId(), R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIAlphaImageButton button = (QMUIAlphaImageButton) v;
                        isShowAll = !isShowAll;
                        button.setImageResource(getButtonDrawableResId());
                        if (isShowAll) {
                            Toast.makeText(ActionRegisterActivity.this, "显示全部", Toast.LENGTH_SHORT).show();
                        }
                        loadData();
                    }
                });
        mRegisterList.setLayoutManager(new LinearLayoutManager(this));
        createAdapter();
    }

    private int getButtonDrawableResId() {
        return isShowAll ? R.drawable.vector_drawable_all : R.drawable.vector_drawable_sub;
    }

    protected void createAdapter() {
        adapter = new ActionRegisterItemAdapter(R.layout.item_checkadapter_view);
        mRegisterList.setAdapter(adapter);
        adapter.bindToRecyclerView(mRegisterList);
        adapter.setOnItemClickListener(this);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setNewData(null);
    }

    protected void getHideSetting() {
        isShowAll = !getSharedPreferences().getBoolean("hide_finished", false);
    }

    protected String getTitleString() {
        return "立案";
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData() {
        if (isShowAll) {
            getData();
        } else {
            changeListener = null;
            adapter.setEnableLoadMore(false);
            adapter.setOnLoadMoreListener(null);
            showCheckList(null);
        }
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, ActionRegisterActivity.class);
        context.startActivity(intent);
    }

    private List<ActionRegisterItem> queryLocalData() {
        String key = mSearchKey.getText().toString();
        if (TextUtils.isEmpty(key)) {
            return ActionRegisterItem.getBox().getAll();
        } else {
            return ActionRegisterItem.getBox().query().contains(ActionRegisterItem_.crime_address, key).build().find();
        }
    }

    protected void showCheckList(List<ActionRegisterItem> list) {
        result.clear();
        result.addAll(queryLocalData());
        if (list != null) {
            result.addAll(list);
        }
        if (result.size() > 0) {
            if (isLoadMore) {
                adapter.addData(result);
                if (hasMore) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }
            } else {
                adapter.replaceData(result);
            }
        } else {
            if (isLoadMore) {
                adapter.loadMoreEnd();
            } else {
                adapter.showEmpty();
            }

        }
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(mSearchKey.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showErrorView() {
        if (isLoadMore) {
            adapter.loadMoreFail();
        } else {
            adapter.showError();
        }
    }

    protected View createEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNew(-1);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        createNew(position);
    }

    protected void createNew(int position) {
        if (position >= 0) {
            CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, (ActionRegisterItem) adapter.getItem(position));
        } else {
            CreateActionRegisterActivity.ToActivity(ActionRegisterActivity.this, null);
        }
    }

    private void getData() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = Observable.merge(Observable.create(emitter -> {
            mSearchButton.setOnClickListener(v -> {
                isLoadMore = false;
                emitter.onNext(mSearchKey.getText().toString());
            });
            adapter.setOnLoadMoreListener(() -> {
                isLoadMore = true;
                emitter.onNext(mSearchKey.getText().toString());
            }, mRegisterList);
            changeListener = value -> {
                isLoadMore = false;
                emitter.onNext(mSearchKey.getText().toString());
            };
        }), Observable.just(""))
                .doAfterNext(s -> {
                    hideSoftInput();
                    if (!isLoadMore) {
                        start = 0;
                        adapter.showLoading();
                    } else {
                        start = adapter.getItemCount();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap((Function<String, ObservableSource<SimpleHttpResult<List<ActionRegisterItem>>>>) s -> NetworkApi.getService(ActionRegisterService.class)
                        .queryList(start, 20, "", s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result.pushState == 200) {
                        hasMore = result.getTotalCount() > adapter.getData().size() + 20;
                        adapter.setEnableLoadMore(hasMore);
                        showCheckList(result.getDatas());
                    } else {
                        showErrorView();
                    }

                }, throwable -> showErrorView());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            if (changeListener != null) {
                changeListener.textChange("");
            } else {
                loadData();
            }
        }
    }
}
