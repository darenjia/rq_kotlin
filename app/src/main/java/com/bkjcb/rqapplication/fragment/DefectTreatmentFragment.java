package com.bkjcb.rqapplication.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.adapter.DefectTreatmentItemAdapter;
import com.bkjcb.rqapplication.model.DefectTreatmentModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class DefectTreatmentFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.defect_content_layout)
    RecyclerView mContentLayout;
    private DefectTreatmentItemAdapter adapter;
    private String keyWord;


    public static DefectTreatmentFragment newInstance() {
        return new DefectTreatmentFragment();
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_defect_treatment;
    }

    @Override
    protected void initView() {
        adapter = new DefectTreatmentItemAdapter(R.layout.item_defect_treatment);
        adapter.bindToRecyclerView(mContentLayout);
        mContentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentLayout.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 8; i++) {
            adapter.addData(createModel());
        }
        adapter.setOnItemClickListener(this);
    }

    private DefectTreatmentModel createModel() {
        DefectTreatmentModel model = new DefectTreatmentModel();
        model.time = "2020-12-12";
        model.name = "过桥米线";
        model.address = "龙华中路14号";
        model.type = "液化气严重隐患";
        return model;
    }

    private void initDisposable() {
      /*  disposable = Observable.merge(Observable.create(new ObservableOnSubscribe<ObservableSource<String>>() {
            @Override
            public void subscribe(ObservableEmitter<ObservableSource<String>> emitter) throws Exception {

            }
        })).flatMap(new Function<String, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(String s) throws Exception {
                return null;
            }
        });*/
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
