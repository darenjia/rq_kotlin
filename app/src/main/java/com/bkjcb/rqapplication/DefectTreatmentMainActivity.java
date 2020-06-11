package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.bkjcb.rqapplication.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.fragment.DefectTreatmentFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class DefectTreatmentMainActivity extends SimpleBaseActivity {
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.content_pager)
    ViewPager mContentPager;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_defect;
    }

    @Override
    protected void initView() {
        initTopbarNoBackButton("隐患处置");
    }

    @Override
    protected void initData() {
        initTab();
        addTab("待处置");
        addTab("已处置");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DefectTreatmentFragment());
        fragments.add(new DefectTreatmentFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mContentPager.setAdapter(adapter);
    }

    private void initTab() {
        int space = QMUIDisplayHelper.dp2px(this, 16);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setupWithViewPager(mContentPager, false);
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.color_type_0));
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.colorSecondDrayText));
    }

    private void addTab(String title) {
        QMUITabSegment.Tab tab = new QMUITabSegment.Tab(title);
        //tab.setTextColor(getResources().getColor(R.color.colorText), getResources().getColor(R.color.colorYellowWhite));
        mTabSegment.addTab(tab);
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, DefectTreatmentMainActivity.class);
        context.startActivity(intent);
    }
}
