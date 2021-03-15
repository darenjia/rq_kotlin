package com.bkjcb.rqapplication.userRecord;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.userRecord.adapter.GasWorkRecordAdapter;
import com.bkjcb.rqapplication.base.interfaces.OnTextChangeListener;
import com.bkjcb.rqapplication.userRecord.model.GasStatisticData;
import com.bkjcb.rqapplication.userRecord.model.GasUserRecordResult;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.userRecord.retrofit.GasService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.DataUtil;
import com.bkjcb.rqapplication.base.util.RxJavaUtil;
import com.bkjcb.rqapplication.base.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zagum.expandicon.ExpandIconView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasUserRecordActivity extends SimpleBaseActivity {

    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    @BindView(R.id.station_name)
    EditText mSearchView;
    @BindView(R.id.station_search)
    ImageView mSearchImage;
    @BindView(R.id.station_search_close)
    Button mClearBtn;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tag_layout1)
    TagContainerLayout mTagLayout1;
    @BindView(R.id.tag_layout2)
    TagContainerLayout mTagLayout2;
    @BindView(R.id.tag_layout3)
    TagContainerLayout mTagLayout3;
    @BindView(R.id.tag_layout4)
    TagContainerLayout mTagLayout4;
    @BindView(R.id.tag_layout5)
    TagContainerLayout mTagLayout5;
    @BindView(R.id.filter_district)
    View mFilterDistrict;
    @BindView(R.id.filter_street)
    View mFilterStreet;
    @BindView(R.id.filter_more)
    View mFilterMore;
    @BindView(R.id.expand_icon1)
    ExpandIconView mExpandIcon1;
    @BindView(R.id.expand_icon2)
    ExpandIconView mExpandIcon2;
    @BindView(R.id.expand_icon3)
    ExpandIconView mExpandIcon3;
    @BindView(R.id.street_name)
    TextView mStreetName;
    @BindView(R.id.district_name)
    TextView mDistrictName;
    @BindView(R.id.search_layout)
    View mSearchLayout;
    @BindView(R.id.filter_more_layout)
    View mMoreLayout;
    private GasWorkRecordAdapter adapter;
    private int page = 0;
    private boolean isLoadMore = false;
    private String key = "";
    private int selectedID = 0;
    private int streetSelectedID = 0;
    private int selectedTyfId = 0;
    private int selectedXhId = 0;
    private int selectedLjgId = 0;
    private Disposable disposable1;
    private List<GasStatisticData> gasStatisticData;
    private OnTextChangeListener textChangeListener;
    private String districtName = "";
    private String streetName = "";
    private String tyfName = "";
    private String xhName = "";
    private int currentRole = 0;//0 市 1区 2街镇
    private String ljgName = "";

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_check;
    }

    @Override
    protected void initView() {
        QMUITopBarLayout mAppbar = initTopbar("一户一档");
        initUserRole();
        if (currentRole == 2) {
            mAppbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AddNewGasUserActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });
            mAppbar.addRightImageButton(R.drawable.vector_drawable_temp_list, R.id.top_right_button2)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TempRecordActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });
          /*  mAppbar.addRightImageButton(R.drawable.vector_drawable_setting, R.id.top_right_button1)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SettingActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });*/
        }
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        adapter = new GasWorkRecordAdapter(R.layout.item_record_adapter_view);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        adapter.bindToRecyclerView(mCheckList);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GasUserRecordResult.GasUserRecord item = (GasUserRecordResult.GasUserRecord) adapter.getItem(position);
                if (currentRole != 2) {
                    GasUserRecordDetailActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid());
                } else {
                    GasReviewActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid(), 1, item.getYonghuming());
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GasUserRecordResult.GasUserRecord item = (GasUserRecordResult.GasUserRecord) adapter.getItem(position);
                if (view.getId() == R.id.check_detail) {
                    GasUserRecordDetailActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid());
                } else {
                    ReviewRecordActivity.ToActivity(GasUserRecordActivity.this, item.getYihuyidangid(), item.getYonghuming());
                }


            }
        });
        adapter.setNewData(null);
        initTagView();
        initFilterMore();
    }

    private void initUserRole() {
        String type = MyApplication.getUser().getUserleixing();
        List<String> roles = MyApplication.getUser().getRoles();
        if (type.equals("区用户")) {
            currentRole = 1;
            districtName = MyApplication.getUser().getArea().getArea_name();
            mDistrictName.setText(districtName);
            mFilterDistrict.setClickable(false);
        } else if (type.equals("街镇用户")) {
            currentRole = 2;
            districtName = MyApplication.getUser().getArea().getArea_name();
            streetName = MyApplication.getUser().getAreacode().getStreet_jc();
            mDistrictName.setText(districtName);
            mFilterDistrict.setEnabled(false);
            mStreetName.setText(streetName);
            mFilterStreet.setClickable(false);
        } else {
            currentRole = 0;
            mFilterStreet.setClickable(false);
        }

    }

    private void initFilterMore() {
        mTagLayout3.setTags("全部", "可调节", "不可调节");
        mTagLayout3.selectTagView(0);
        mTagLayout4.setTags("全部", "是", "否");
        mTagLayout4.selectTagView(0);
        mTagLayout5.setTags("全部", "硬管连接", "橡胶管", "其他管");
        mTagLayout5.selectTagView(0);
        mTagLayout3.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (position != selectedTyfId) {
                    mTagLayout3.selectTagView(position);
                    if (position > 0) {
                        tyfName = text;
                    } else {
                        tyfName = "";
                    }
                    if (position >= 0) {
                        mTagLayout3.deselectTagView(selectedTyfId);
                    }
                    selectedTyfId = position;
                    textChangeListener.textChange("");
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        mTagLayout4.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (position != selectedXhId) {
                    mTagLayout4.selectTagView(position);
                    if (position > 0) {
                        xhName = text;
                    } else {
                        xhName = "";
                    }
                    if (position >= 0) {
                        mTagLayout4.deselectTagView(selectedXhId);
                    }
                    selectedXhId = position;
                    textChangeListener.textChange("");
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        mTagLayout5.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (position != selectedLjgId) {
                    mTagLayout5.selectTagView(position);
                    if (position > 0) {
                        ljgName = text;
                    } else {
                        ljgName = "";
                    }
                    if (position >= 0) {
                        mTagLayout5.deselectTagView(selectedLjgId);
                    }
                    selectedLjgId = position;
                    textChangeListener.textChange("");
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        queryRemoteData();
        if (currentRole != 2) {
            getListData();
        }
    }

    @OnClick({R.id.station_search, R.id.filter_street, R.id.filter_district, R.id.filter_more})
    public void onClick(View v) {
        if (v.getId() == R.id.filter_street) {
            if (currentRole != 2) {
                if (mTagLayout2.getVisibility() == View.VISIBLE) {
                    hideView(mTagLayout2);
                    mExpandIcon2.setState(ExpandIconView.MORE, true);
                } else {
                    showView(mTagLayout2);
                    mExpandIcon2.setState(ExpandIconView.LESS, true);
                }
                if (mTagLayout1.getVisibility() == View.VISIBLE) {
                    hideView(mTagLayout1);
                    mExpandIcon1.setState(ExpandIconView.MORE, true);
                }
                if (mMoreLayout.getVisibility() == View.VISIBLE) {
                    hideView(mMoreLayout);
                    mExpandIcon3.setState(ExpandIconView.MORE, true);
                }
            }
        } else if (v.getId() == R.id.filter_district) {
            if (currentRole == 0) {
                if (mTagLayout1.getVisibility() == View.VISIBLE) {
                    hideView(mTagLayout1);
                    mExpandIcon1.setState(ExpandIconView.MORE, true);
                } else {
                    showView(mTagLayout1);
                    mExpandIcon1.setState(ExpandIconView.LESS, true);
                }
                if (mTagLayout2.getVisibility() == View.VISIBLE) {
                    hideView(mTagLayout2);
                    mExpandIcon2.setState(ExpandIconView.MORE, true);
                }
                if (mMoreLayout.getVisibility() == View.VISIBLE) {
                    hideView(mMoreLayout);
                    mExpandIcon3.setState(ExpandIconView.MORE, true);
                }
            }
        } else if (v.getId() == R.id.filter_more) {
            if (mMoreLayout.getVisibility() == View.VISIBLE) {
                hideView(mMoreLayout);
                mExpandIcon3.setState(ExpandIconView.MORE, true);
            } else {
                showView(mMoreLayout);
                mExpandIcon3.setState(ExpandIconView.LESS, true);
            }
            if (mTagLayout1.getVisibility() == View.VISIBLE) {
                hideView(mTagLayout1);
                mExpandIcon1.setState(ExpandIconView.MORE, true);
            }
            if (mTagLayout2.getVisibility() == View.VISIBLE) {
                hideView(mTagLayout2);
                mExpandIcon2.setState(ExpandIconView.MORE, true);
            }
        } else {
            toggleVisibility(mSearchLayout);
        }
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            hideView(view);
        } else {
            showView(view);
        }
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    private void queryRemoteData() {
        Observable.merge(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mRefreshLayout.setOnRefreshListener(() -> {
                    isLoadMore = false;
                    emitter.onNext(mSearchView.getText().toString());
                });
                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        isLoadMore = true;
                        emitter.onNext(mSearchView.getText().toString());
                    }
                }, mCheckList);
              /*  mSearchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        isLoadMore = false;
                        emitter.onNext(s.toString());
                    }
                });*/
                mClearBtn.setOnClickListener(v -> {
                    isLoadMore = false;
                    emitter.onNext(mSearchView.getText().toString());
                });
                textChangeListener = new OnTextChangeListener() {
                    @Override
                    public void textChange(String value) {
                        isLoadMore = false;
                        emitter.onNext(mSearchView.getText().toString());
                    }
                };
            }
        }), Observable.just(""))
                .debounce(200, TimeUnit.MILLISECONDS)
                //.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!isLoadMore) {
                            mRefreshLayout.setRefreshing(true);
                            page = 0;
                        } else {
                            page = adapter.getData().size();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<GasUserRecordResult>>() {
                    @Override
                    public ObservableSource<GasUserRecordResult> apply(String s) throws Exception {
                        return NetworkApi.getService(GasService.class)
                                .getWorkRecords(page, 20, districtName, streetName, s, tyfName, xhName, ljgName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GasUserRecordResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(GasUserRecordResult result) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        if (result.pushState == 200) {
                            adapter.setEnableLoadMore(result.getTotalCount() > adapter.getData().size() + 20);
                            showCheckList(result.getDatas());
                        } else {
                            showErrorView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        showErrorView();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    protected void showCheckList(List<GasUserRecordResult.GasUserRecord> list) {
        if (list != null && list.size() > 0) {
            if (isLoadMore) {
                adapter.addData(list);
                adapter.loadMoreComplete();
            } else {
                adapter.replaceData(list);
                //adapter.loadMoreEnd();
            }
        } else {
            if (isLoadMore) {
                adapter.loadMoreEnd();
            } else {
                adapter.setNewData(null);
                adapter.setEmptyView(createEmptyView(createClickListener()));
            }
        }
    }

    protected View createEmptyView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        if (currentRole == 2) {
            view.findViewById(R.id.empty_button).setOnClickListener(listener);
        } else {
            view.findViewById(R.id.empty_button).setVisibility(View.GONE);
        }
        return view;
    }

    protected View.OnClickListener createClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewGasUserActivity.ToActivity(GasUserRecordActivity.this);
            }
        };

    }

    public static void ToActivity(Context context) {
      /*  if (MyApplication.user.getAreacode() != null) {
            Intent intent = new Intent(context, GasUserRecordActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "此功能暂只对街镇用户开放", Toast.LENGTH_SHORT).show();
        }*/
        Intent intent = new Intent(context, GasUserRecordActivity.class);
        context.startActivity(intent);
    }

    protected void showErrorView() {
        if (isLoadMore) {
            adapter.loadMoreFail();
        } else {
            showRefreshLayout(false);
            adapter.setNewData(null);
            adapter.setEmptyView(R.layout.error_view, (ViewGroup) mCheckList.getParent());
        }
    }

    private void initTagView() {
        int selectedColor = getResources().getColor(R.color.colorAccent);
        mTagLayout1.setSelectedTagBackgroundColor(selectedColor);
        mTagLayout2.setSelectedTagBackgroundColor(selectedColor);
        mTagLayout3.setSelectedTagBackgroundColor(selectedColor);
        mTagLayout4.setSelectedTagBackgroundColor(selectedColor);
        mTagLayout5.setSelectedTagBackgroundColor(selectedColor);
        mTagLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (position != selectedID) {
                    mTagLayout1.selectTagView(position);
                    if (position > 0) {
                        districtName = text;
                        mFilterStreet.setClickable(true);
                    } else {
                        districtName = "";
                        mFilterStreet.setClickable(false);
                    }
                    mDistrictName.setText(text);
                    if (selectedID >= 0) {
                        mTagLayout1.deselectTagView(selectedID);
                    }
                    selectedID = position;
                    initTagData(position - 1);
                    textChangeListener.textChange("");
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        mTagLayout2.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (position != streetSelectedID) {
                    mTagLayout2.selectTagView(position);
                    if (position > 0) {
                        streetName = mTagLayout2.getTagText(position);
                    } else {
                        streetName = "";
                    }
                    mStreetName.setText(mTagLayout2.getTagText(position));
                    if (streetSelectedID >= 0) {
                        mTagLayout2.deselectTagView(streetSelectedID);
                    }
                    streetSelectedID = position;
                    textChangeListener.textChange("");
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }

    private void getListData() {
        if (DataUtil.getInstance().getList() != null) {
            initTagData();
            return;
        }
        disposable1 = NetworkApi.getService(GasService.class)
                .getStatisticData(districtName)
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<List<GasStatisticData>>>() {
                    @Override
                    public void accept(SimpleHttpResult<List<GasStatisticData>> result) throws Exception {
                        if (result.pushState == 200) {
                            DataUtil.getInstance().setList(result.getDatas());
                            initTagData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void initTagData() {
        List<GasStatisticData> data = DataUtil.getInstance().getList();
        if (data != null) {
            gasStatisticData = data;
            if (data.size() > 1) {
                List<String> districts = new ArrayList<>();
                districts.add("全部");
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        districts.add(data.get(i).getName());
                    }
                }
                mTagLayout1.setTags(districts);
                mTagLayout1.selectTagView(0);
            } else {
                initTagData(0);
            }
        }
    }

    private void initTagData(int position) {
        List<String> districts = new ArrayList<>();
        districts.add("全部");
        if (position >= 0 && gasStatisticData.get(position).getChildrens() != null) {
            for (int i = 0; i < gasStatisticData.get(position).getChildrens().size(); i++) {
                districts.add(gasStatisticData.get(position).getChildrens().get(i).getName());
            }
        }
        mTagLayout2.setTags(districts);
        mTagLayout2.selectTagView(0);
        mStreetName.setText("街镇");
        streetName = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable1 != null && !disposable1.isDisposed()) {
            disposable1.dispose();
        }
    }
}
