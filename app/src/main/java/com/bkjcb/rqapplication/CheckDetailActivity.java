package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkjcb.rqapplication.adapter.ImageListAdapter;
import com.bkjcb.rqapplication.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.fragment.CheckItemDetailFragment;
import com.bkjcb.rqapplication.model.CheckContentItem;
import com.bkjcb.rqapplication.model.CheckContentItemResult;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.retrofit.CheckService;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class CheckDetailActivity extends SimpleBaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.check_detail_pager)
    ViewPager mViewPager;
    @BindView(R.id.image_files)
    RecyclerView mImageFiles;
    @BindView(R.id.image_count)
    TextView mImageCount;
    @BindView(R.id.check_detail_pageNumber)
    TextView mPagerNumber;
    private CheckItem checkItem;
    private List<String> mediaList;
    private ImageListAdapter imageAdapter;
    private int currentPage = 0;
    private List<Fragment> fragmentList;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_detail_check;
    }

    @Override
    protected void initView() {
        initTopbar("检查详情");
        initEmptyView();
        showLoadingView();
    }

    @Override
    protected void initData() {
        initRetrofit();
        checkItem = (CheckItem) getIntent().getSerializableExtra("data");
        if (checkItem == null) {
            long id = getIntent().getLongExtra("id", 0);
            checkItem = CheckItem.getBox().get(id);
            if (checkItem == null) {
                showErrorView(null);
                return;
            }
        }
        checkNetwork();
    }

    @Override
    protected void networkIsOk() {
        getCheckContent();
    }

    @OnClick({R.id.check_detail_previous, R.id.check_detail_next, R.id.check_detail_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_detail_previous:
                if (currentPage > 0) {
                    mViewPager.setCurrentItem(--currentPage, true);
                }
                updateCurrentPageNumber();
                break;
            case R.id.check_detail_next:
                if (currentPage < fragmentList.size()) {
                    mViewPager.setCurrentItem(++currentPage, true);
                }
                updateCurrentPageNumber();
                break;
            case R.id.check_detail_finish:
                checkItem.status = 2;
                saveDate();
                break;
            default:
        }
    }

    private void getCheckContent() {
        disposable = retrofit.create(CheckService.class)
                .getCheckItem(checkItem.zhandianleixing)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckContentItemResult>() {
                    @Override
                    public void accept(CheckContentItemResult checkContentItemResult) throws Exception {
                        if (checkContentItemResult.pushState == 200 && checkContentItemResult.getDatas() != null && checkContentItemResult.getDatas().size() > 0) {
                            initCheckData(checkContentItemResult.getDatas());
                            initImageListView();
                            hideEmptyView();
                        } else {
                            getDateFail(checkContentItemResult.pushMsg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getDateFail(throwable.getMessage());
                    }
                });
    }

    private void getDateFail(String detail) {
        showErrorView(detail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckContent();
            }
        });
    }

    private void initCheckData(ArrayList<CheckContentItem> datas) {
        fragmentList = new ArrayList<>();
        for (CheckContentItem item : datas) {
            fragmentList.add(CheckItemDetailFragment.newInstances(item, checkItem.c_id));
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initImageListView() {
        imageAdapter = new ImageListAdapter(R.layout.item_image);
        mImageFiles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImageFiles.setAdapter(imageAdapter);
        mediaList = new ArrayList<>();
        if (!TextUtils.isEmpty(checkItem.filePath)) {
            mediaList.addAll(Arrays.asList(checkItem.filePath.split(",")));
        }
        updateCountSize();
        imageAdapter.setNewData(mediaList);
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        imageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_grid_bt) {
                    mediaList.remove(position);
                    imageAdapter.remove(position);
                    updateCountSize();
                }
            }
        });
        if (checkItem.status != 3) {
            imageAdapter.addFooterView(createFooterView(), -1, LinearLayout.HORIZONTAL);
        }
    }

    private void updateCountSize() {
        mImageCount.setText(String.valueOf(mediaList.size()));
    }

    private void updateCurrentPageNumber() {
        mPagerNumber.setText((currentPage + 1) + "/" + fragmentList.size());
    }

    public void showPickImg() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .setOutputCameraPath(Environment.getExternalStorageDirectory()
                        + "/RQApp/CheckImage/" + checkItem.c_id + "/")
                .compress(false)
                .imageFormat(PictureMimeType.PNG)
                //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, CheckDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, CheckDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private View createFooterView() {
        ImageView view = new ImageView(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(Utils.dip2px(this, 120), Utils.dip2px(this, 120)));
        int padding = Utils.dip2px(this, 5);
        view.setPadding(padding, padding, padding, padding);
        view.setImageResource(R.drawable.icon_add_pic);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickImg();
            }
        });
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            for (LocalMedia media : list) {
                mediaList.add(media.getPath());
            }
            imageAdapter.replaceData(mediaList);
            updateCountSize();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDate();
    }

    private void saveDate() {
        StringBuilder builder = new StringBuilder();
        if (mediaList.size() > 0) {
            for (String path : mediaList) {
                builder.append(",").append(path);
            }
            checkItem.filePath = builder.substring(1);
        }
        CheckItem.getBox().put(checkItem);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentPage = i;
        updateCurrentPageNumber();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
