package com.bkjcb.rqapplication.stationCheck.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;

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
    @BindView(R.id.title)
    TextView mTitle;
    private OnChooseListener listener;
    private CheckItem checkItem;
    String[] typeString;

    public void setTypeString(String[] typeString) {
        this.typeString = typeString;
    }

    public CheckItem getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public void setListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public static ChooseCheckTypeFragment newInstance(OnChooseListener listener, String[] type) {
        ChooseCheckTypeFragment fragment = new ChooseCheckTypeFragment();
        fragment.setListener(listener);
        fragment.setTypeString(type);
        return fragment;
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_check_type_view;
    }

    @Override
    protected void initView() {
        if (checkItem.type==1){
            mTitle.setText("企业类型");
        }
        setTextString(mCheckType1, typeString[0]);
        setTextString(mCheckType2, typeString[1]);
        setTextString(mCheckType3, typeString[2]);
        setTextString(mCheckType4, typeString[3]);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkItem != null && !TextUtils.isEmpty(checkItem.zhandianleixing)) {
            updateInfo(checkItem.zhandianleixing);
        }

    }

    private void setTextString(TextView textView, String s) {
        if (TextUtils.isEmpty(s)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(s);
        }
    }

    private void changeTextColor(TextView textView, boolean checked) {
        if (textView != null) {
            textView.setTextColor(context.getResources().getColor(checked ? R.color.colorText : R.color.colorPrimary));
            textView.setBackgroundColor(context.getResources().getColor(checked ? R.color.colorPrimary : R.color.colorText));
        }
    }

    @OnClick({R.id.check_type1, R.id.check_type2, R.id.check_type3, R.id.check_type4})
    public void onClick(View v) {
        String name = "";
        switch (v.getId()) {
            default:
                break;
            case R.id.check_type1:
                name = typeString[0];
                break;
            case R.id.check_type2:
                name = typeString[1];
                break;
            case R.id.check_type3:
                name = typeString[2];
                break;
            case R.id.check_type4:
                name = typeString[3];
                break;
        }
        listener.choose(name);
    }

    private void updateInfo(String s) {
        if (s.equals(typeString[0])) {
            changeTextColor(mCheckType1, true);
        } else if (s.equals(typeString[1])) {
            changeTextColor(mCheckType2, true);
        } else if (s.equals(typeString[2])) {
            changeTextColor(mCheckType3, true);
        } else {
            changeTextColor(mCheckType4, true);
        }

    }
}

