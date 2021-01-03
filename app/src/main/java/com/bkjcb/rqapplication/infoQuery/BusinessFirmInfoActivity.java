package com.bkjcb.rqapplication.infoQuery;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.infoQuery.fragment.BusinessFirmSearchFragment;

/**
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class BusinessFirmInfoActivity extends SimpleBaseActivity {

    @Override
    protected int setLayoutID() {
        return R.layout.activity_create_check_task;
    }

    @Override
    protected void initView() {
        initTopbar("经营企业信息查询");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.create_task_content,new BusinessFirmSearchFragment())
                .commit();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public static void ToActivity(Context context){
        context.startActivity(new Intent(context,BusinessFirmInfoActivity.class));
    }
}
