package com.bkjcb.rqapplication.infoQuery;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.infoQuery.fragment.BusinessFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.model.SimpleBusinessFirmModel;

/**
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class BusinessFirmInfoDetailActivity extends SimpleBaseActivity {

    @Override
    protected int setLayoutID() {
        return R.layout.activity_create_check_task;
    }

    @Override
    protected void initView() {

        SimpleBusinessFirmModel model= (SimpleBusinessFirmModel) getIntent().getSerializableExtra("code");
        initTopbar(model.getQiyemingcheng());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.create_task_content, BusinessFirmInfoFragment.newInstance(model.getC_id()))
                .commit();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public static void ToActivity(Context context, SimpleBusinessFirmModel model){
        Intent intent=new Intent(context, BusinessFirmInfoDetailActivity.class);
        intent.putExtra("code",model);
        context.startActivity(intent);
    }
}
