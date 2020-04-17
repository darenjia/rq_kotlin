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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.adapter.ImageListAdapter;
import com.bkjcb.rqapplication.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.fragment.CheckItemDetailFragment;
import com.bkjcb.rqapplication.fragment.CheckItemResultFragment;
import com.bkjcb.rqapplication.model.CheckContentItem;
import com.bkjcb.rqapplication.model.CheckContentItemResult;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.model.CheckResultItem;
import com.bkjcb.rqapplication.model.CheckResultItem_;
import com.bkjcb.rqapplication.retrofit.CheckService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.Box;
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
    @BindView(R.id.image_count)
    TextView mImageCount;
    @BindView(R.id.check_detail_pageNumber)
    TextView mPagerNumber;
    @BindView(R.id.image_file_view)
    RecyclerView mImageFiles;
    @BindView(R.id.check_detail_finish)
    Button mFinishButton;
    protected CheckItem checkItem;
    private List<String> mediaList;
    private ImageListAdapter imageAdapter;
    private int currentPage = 0;
    protected List<Fragment> fragmentList;
    private Box<CheckResultItem> checkResultItemBox;
    private QMUIListPopup listPopup;
    protected List<String> contents;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_detail_check;
    }

    @Override
    protected void initView() {
        initTopbar("检查详情", 0);
        initEmptyView();
        showLoadingView();

    }

    @Override
    protected void initData() {
        StyledDialog.init(this);
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
        if (checkItem.status == 3) {
            mFinishButton.setBackgroundColor(getResources().getColor(R.color.colorGreenGray));
            mFinishButton.setEnabled(false);
            mFinishButton.setText("检查已结束");
        } else if (checkItem.status == 0 || checkItem.status == 1) {
            checkItem.status = 1;
            saveDate();
        }
    }

    @Override
    protected void networkIsOk() {
        getCheckContent();
    }

    @OnClick({R.id.check_detail_previous, R.id.check_detail_next, R.id.check_detail_finish, R.id.check_detail_pageNumber})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_detail_previous:
                if (--currentPage >= 0) {
                    mViewPager.setCurrentItem(currentPage, true);
                } else {
                    currentPage = 0;
                }
                break;
            case R.id.check_detail_next:
                if (++currentPage < fragmentList.size()) {
                    mViewPager.setCurrentItem(currentPage, true);
                }
                break;
            case R.id.check_detail_finish:
                if (checkItem.status == 3) {
                    Toast.makeText(this, "当前检查已结束", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveResult();
                break;
            case R.id.check_detail_pageNumber:
                showPopList();
                break;
            default:
        }
    }

    protected void saveResult() {
        if (TextUtils.isEmpty(checkItem.jianchajieguo)) {
            if (mViewPager.getCurrentItem() == fragmentList.size() - 1) {
                showSnackbar(mPagerNumber, "请选择检查结果");
            } else {
                mViewPager.setCurrentItem(fragmentList.size() - 1, true);
            }
        } else {
            showFinishTipDialog();
        }
    }

    protected void closeActivity(boolean isSubmit) {
        if (isSubmit) {
            checkItem.status = 2;
        }
        if (currentPage == fragmentList.size()-1) {
            CheckItemResultFragment resultFragment = (CheckItemResultFragment) fragmentList.get(fragmentList.size() - 1);
            checkItem.beizhu = resultFragment.getRemark();
        }
        saveDate();
        Toast.makeText(this, "检查完成！", Toast.LENGTH_SHORT).show();
        finish();
    }

    protected void getCheckContent() {
        disposable = NetworkApi.getService(CheckService.class)
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

    protected void getDateFail(String detail) {
        showErrorView(detail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckContent();
            }
        });
    }

    protected void initCheckData(ArrayList<CheckContentItem> datas) {
        fragmentList = new ArrayList<>();
        contents = new ArrayList<>();
        checkResultItemBox = CheckResultItem.getBox();
        for (CheckContentItem item : datas) {
            fragmentList.add(createFragment(item, checkItem.c_id));
            saveResultItem(checkItem.c_id, item.getId());
            contents.add(item.getXuhao() + "." + item.getJianchaneirong());
        }
        fragmentList.add(CheckItemResultFragment.newInstances(checkItem));
        contents.add("检查结果");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        updateCurrentPageNumber();
    }

    protected Fragment createFragment(CheckContentItem item, String id) {
        return CheckItemDetailFragment.newInstances(item, id, checkItem.status == 3 ? 1 : 0);
    }

    protected void saveResultItem(String cid, String uid) {
        if (checkResultItemBox.query().equal(CheckResultItem_.jianchaid, cid).and().equal(CheckResultItem_.jianchaxiangid, uid).build().count() == 0) {
            CheckResultItem resultItem = new CheckResultItem(cid, uid);
            checkResultItemBox.put(resultItem);
        }
    }

    protected void initImageListView() {
        imageAdapter = new ImageListAdapter(R.layout.item_image, checkItem.status != 3);
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
                MediaPlayActivity.ToActivity(CheckDetailActivity.this, (String) adapter.getItem(position));
            }
        });
        imageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_grid_bt) {
                    mediaList.remove(position);
                    imageAdapter.replaceData(mediaList);
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

    protected void updateCurrentPageNumber() {
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
    protected void onStop() {
        super.onStop();
      /*  if (checkItem.status == 0 || checkItem.status == 1) {
            checkItem.status = 1;
            saveDate();
        }*/
    }

    protected void saveDate() {
        StringBuilder builder = new StringBuilder();
        if (mediaList != null && mediaList.size() > 0) {
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

    private void showPopList() {
        if (listPopup == null) {
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, contents);
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mViewPager.setCurrentItem(i);
                    if (listPopup != null) {
                        listPopup.dismiss();
                    }
                }
            };
            listPopup = new QMUIListPopup(this, QMUIListPopup.DIRECTION_TOP, adapter);
            listPopup.create(QMUIDisplayHelper.dp2px(this, 250),
                    QMUIDisplayHelper.dp2px(this, 300),
                    onItemClickListener);
            listPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        listPopup.show(mPagerNumber);
    }

    protected void showFinishTipDialog() {
        StyledDialog.buildIosAlert("提示", "是否结束当前检查？(结束后不可修改)", new MyDialogListener() {
            @Override
            public void onFirst() {
                closeActivity(true);
            }

            @Override
            public void onSecond() {
                closeActivity(false);
            }
        }).setBtnText("结束检查", "仅保存", "取消")
                .show();

    }
}
