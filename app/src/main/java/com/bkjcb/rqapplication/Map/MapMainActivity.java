package com.bkjcb.rqapplication.Map;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class MapMainActivity extends SimpleBaseActivity {

    @Override
    protected int setLayoutID() {
        return R.layout.activity_map;
    }

    @Override
    protected void initView() {
        initTopbar("管线查看");
    }

    @Override
    protected void initData() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content,new MapFragment())
                .commit();
    }
    public static void ToActivity(Context context){
        context.startActivity(new Intent(context,MapMainActivity.class));
    }

}
