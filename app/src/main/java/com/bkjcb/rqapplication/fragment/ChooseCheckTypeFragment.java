package com.bkjcb.rqapplication.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckItem;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class ChooseCheckTypeFragment extends BaseSimpleFragment {


    public interface OnChooseListener {
        void choose(String type);
    }

    @BindView(R.id.check_type1)
    TextView mCheckType1;
    @BindView(R.id.check_type2)
    TextView mCheckType2;
    @BindView(R.id.check_type3)
    TextView mCheckType3;
    @BindView(R.id.check_type4)
    TextView mCheckType4;

    private OnChooseListener listener;
    private CheckItem checkItem;

    public CheckItem getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public void setListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public static ChooseCheckTypeFragment newInstance(OnChooseListener listener) {
        ChooseCheckTypeFragment fragment = new ChooseCheckTypeFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_check_type_view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        updateInfo();
    }

    private void changeTextColor(TextView textView, boolean checked) {
        if (textView != null) {
            textView.setTextColor(context.getResources().getColor(checked ? R.color.colorText : R.color.colorPrimary));
            textView.setBackgroundColor(context.getResources().getColor(checked ? R.color.colorBittersweet : R.color.colorText));
        }
    }

    @OnClick({R.id.check_type1, R.id.check_type2, R.id.check_type3, R.id.check_type4})
    public void onClick(View v) {
        String name = "";
        switch (v.getId()) {
            default:
                break;
            case R.id.check_type1:
                name = "气化站";
                break;
            case R.id.check_type2:
                name = "储配站";
                break;
            case R.id.check_type3:
                name = "供应站";
                break;
            case R.id.check_type4:
                name = "加气站";
                break;
        }
        listener.choose(name);
    }
    private void updateInfo(){
        if (checkItem != null&&!TextUtils.isEmpty(checkItem.zhandianleixing)) {
            switch (checkItem.zhandianleixing) {
                case "气化站":
                    changeTextColor(mCheckType1, true);
                    break;
                case "储配站":
                    changeTextColor(mCheckType2, true);
                    break;
                case "供应站":
                    changeTextColor(mCheckType3, true);
                    break;
                case "加气站":
                    changeTextColor(mCheckType4, true);
                    break;
                default:
            }
        }
    }
}

