package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.adapter.EmergencyItemAdapter;
import com.bkjcb.rqapplication.model.EmergencyItem;
import com.bkjcb.rqapplication.model.EmergencyItem_;

import java.util.List;

/**
 * Created by DengShuai on 2020/3/5.
 * Description :
 */
public class EmergencyActivity extends ActionRegisterActivity {
    private EmergencyItemAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check;
    }

    @Override
    protected void createAdapter() {
        adapter = new EmergencyItemAdapter(R.layout.item_checkadapter_view);
        mRegisterList.setAdapter(adapter);
        adapter.bindToRecyclerView(mRegisterList);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected String getTitleString() {
        return "事故现场";
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, EmergencyActivity.class);
        context.startActivity(intent);
    }

    private List<EmergencyItem> queryLocalData() {
        if (isShowAll) {
            return EmergencyItem.getBox().getAll();
        } else {
            return EmergencyItem.getBox().query().notEqual(EmergencyItem_.status, 2).build().find();
        }
    }


    protected void showCheckList() {
        List<EmergencyItem> list = queryLocalData();
        //Toast.makeText(this, list.size()+"", Toast.LENGTH_SHORT).show();
        if (list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setNewData(null);
            adapter.setEmptyView(createEmptyView());
        }
    }

    @Override
    protected void createNew(int position) {
        if (position >= 0) {
            CreateEmergencyActivity.ToActivity(EmergencyActivity.this, (EmergencyItem) adapter.getItem(position));
        } else {
            CreateEmergencyActivity.ToActivity(EmergencyActivity.this, null);
        }
    }
}
