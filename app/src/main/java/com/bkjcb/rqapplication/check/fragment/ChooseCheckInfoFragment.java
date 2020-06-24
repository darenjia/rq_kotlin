package com.bkjcb.rqapplication.check.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.check.model.CheckItem;
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.util.Utils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class ChooseCheckInfoFragment extends BaseSimpleFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.info_type)
    TextView mInfoType;
    @BindView(R.id.info_station)
    TextView mInfoStation;
    @BindView(R.id.info_date)
    TextView mInfoDate;
    @BindView(R.id.info_type_title)
    TextView mInfoTypeTitle;
    @BindView(R.id.info_name_title)
    TextView mInfoNameTitle;
    @BindView(R.id.info_year_layout)
    LinearLayout mInfoYearLayout;
    @BindView(R.id.info_year)
    MaterialSpinner mInfoYear;
    @BindView(R.id.info_confirm)
    Button mInfoConfirm;
    @BindView(R.id.info_name)
    TextView mInfoName;
    private CheckItem checkItem;
    private DatePickerDialog pickerDialog;
    private Calendar calendar;
    private ArrayList<String> strings;

    public CheckItem getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    @OnClick({R.id.info_date, R.id.info_confirm, R.id.info_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_date:
                createDatePicker();
                break;
            case R.id.info_confirm:
               /* if (mInfoName.getText().length() == 0) {
                    Toast.makeText(context, "请填写检查人员姓名", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                checkItem.checkMan = mInfoName.getText().toString();
                checkItem.jianchariqi = mInfoDate.getText().toString();
                checkItem.year = strings.get(mInfoYear.getSelectedIndex());
                checkItem.systime = calendar.getTimeInMillis();
                checkItem.c_id = UUID.randomUUID().toString().replace("-", "");
                listener.choose();
                break;
            case R.id.info_back:
                listener.back();
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mInfoDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public interface OnChooseListener {
        void choose();

        void back();
    }

    private OnChooseListener listener;

    public void setListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public static ChooseCheckInfoFragment newInstance(OnChooseListener listener) {
        ChooseCheckInfoFragment fragment = new ChooseCheckInfoFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void setResId() {
        this.resId = R.layout.fragment_check_info_view;
    }

    @Override
    protected void initView() {
        strings = new ArrayList<>();
        strings.add("2020");
        strings.add("2019");
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        mInfoYear.setAdapter(new MaterialSpinnerAdapter<String>(context, strings));
        mInfoDate.setText(Utils.dateFormat("", calendar.getTime()));
        mInfoName.setText(MyApplication.getUser().getReal_name());
        if (checkItem != null) {
            if (!TextUtils.isEmpty(checkItem.jianchariqi)) {
                mInfoDate.setText(checkItem.jianchariqi);
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).parse(checkItem.jianchariqi));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(checkItem.checkMan)){
                mInfoName.setText(checkItem.checkMan);
            }
            if (!TextUtils.isEmpty(checkItem.year)) {
                int positon = 0;
                for (int i = 0; i < strings.size(); i++) {
                    if (strings.get(i).equals(checkItem.year)) {
                        positon = i;
                        break;
                    }
                }
                mInfoYear.setSelectedIndex(positon);
            }
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        updateInfo();
    }

    private void updateInfo() {
        if (checkItem != null) {
            mInfoType.setText(checkItem.zhandianleixing);
            mInfoStation.setText(checkItem.beijiandanwei);
            if (checkItem.type == 1) {
                mInfoTypeTitle.setText("企业类型");
                mInfoNameTitle.setText("企业名称");
                mInfoYearLayout.setVisibility(View.GONE);
            }
        }
    }

    private void createDatePicker() {
        if (pickerDialog == null) {
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar.get(Calendar.YEAR), // Initial year selection
                    calendar.get(Calendar.MONTH), // Initial month selection
                    calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
        }
        if (!pickerDialog.isAdded()) {
            pickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }
    }
}
