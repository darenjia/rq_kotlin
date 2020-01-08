package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bkjcb.rqapplication.model.CheckItem;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class CheckResultDetailActivity extends SimpleBaseActivity {


    @BindView(R.id.info_type)
    TextView mInfoType;
    @BindView(R.id.info_station)
    TextView mInfoStation;
    @BindView(R.id.info_date)
    TextView mInfoDate;
    @BindView(R.id.info_year)
    TextView mInfoYear;
    @BindView(R.id.info_result)
    TextView mInfoResult;
    @BindView(R.id.info_remark)
    TextView mInfoRemark;
    @BindView(R.id.info_operation)
    Button mInfoOperation;
    private CheckItem checkItem;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_detail_result_check;
    }

    @Override
    protected void initView() {
        initTopbar("检查信息详情");
    }

    @Override
    protected void initData() {
        checkItem = (CheckItem) getIntent().getSerializableExtra("data");
        if (checkItem == null) {
            long id = getIntent().getLongExtra("data", 0);
            checkItem = CheckItem.getBox().get(id);
            if (checkItem == null) {
                return;
            }
        }
        mInfoType.setText(checkItem.zhandianleixing);
        mInfoStation.setText(checkItem.beijiandanwei);
        mInfoYear.setText(checkItem.year);
        mInfoDate.setText(checkItem.jianchariqi);
        mInfoRemark.setText(checkItem.beizhu);
        mInfoResult.setText(checkItem.jianchajieguo);
        mInfoOperation.setText(getOperation(checkItem.status));
    }

    @OnClick(R.id.info_operation)
    public void onClick(View v) {
        CheckDetailActivity.ToActivity(this, checkItem.id);
    }

    private List<CheckItem> queryLocalData() {
        return CheckItem.getBox().getAll();
    }


    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, CheckResultDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, CheckResultDetailActivity.class);
        intent.putExtra("data", id);
        context.startActivity(intent);
    }

    private String getOperation(int status) {
        String retString = "";
        switch (status) {
            case 0:
                retString = "开始检查";
                break;
            case 1:
                retString = "继续检查";
                break;
            case 2:
                retString = "开始上传";
                break;
            case 3:
                retString = "查看详情";
            default:
        }
        return retString;
    }
}
