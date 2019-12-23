package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;

public class CreateCheckTaskActivity extends SimpleBaseActivity {

    @Override
    protected int setLayoutID() {
        return R.layout.activity_create_check_task;
    }

    @Override
    protected void initView() {
        initTopbar("新建检查任务");
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, CreateCheckTaskActivity.class);
        context.startActivity(intent);
    }
}
