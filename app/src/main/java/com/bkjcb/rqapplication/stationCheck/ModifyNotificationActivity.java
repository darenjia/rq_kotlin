package com.bkjcb.rqapplication.stationCheck;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.util.OpenFileUtil;
import com.bkjcb.rqapplication.stationCheck.fragment.EditNotificationFragment;
import com.bkjcb.rqapplication.stationCheck.fragment.NotificationFragment;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;
import com.bkjcb.rqapplication.stationCheck.model.ModifyNotificationModel;

import java.io.File;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DengShuai on 2020/12/31.
 * Description :
 */
public class ModifyNotificationActivity extends SimpleBaseActivity {
    @BindView(R.id.info_operation)
    Button mInfoOperation;
    @BindView(R.id.info_export)
    Button mInfoExport;
    ModifyNotificationModel model;
    boolean hasFile = true;
    private EditNotificationFragment editNotificationFragment;
    private NotificationFragment notificationFragment;
    private String fileName;
    private String filePath;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_modify_notification;
    }

    @Override
    protected void initView() {
        initTopbar("责令整改通知书");
    }

    @Override
    protected void initData() {
        CheckItem checkItem = (CheckItem) getIntent().getSerializableExtra("data");
        if (checkItem != null) {
            model = ModifyNotificationModel.query(checkItem.c_id);
            fileName =  "责令整改通知书_" + checkItem.c_id + ".pdf";
            if (model != null) {
                initNotifyView();
            } else {
                editNotificationFragment = EditNotificationFragment.newInstance(checkItem);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.content, editNotificationFragment)
                        .commit();
                mInfoExport.setVisibility(View.GONE);
                mInfoOperation.setVisibility(View.VISIBLE);
            }

        }

    }

    private void initNotifyView() {
        notificationFragment = NotificationFragment.newInstance(model);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, notificationFragment)
                .commit();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),fileName);
        filePath=file.getAbsolutePath();
        mInfoExport.setVisibility(View.VISIBLE);
        mInfoOperation.setVisibility(View.GONE);
        if (file.exists() && file.length() > 0) {
            hasFile = true;
            mInfoExport.setText("已生成PDF文件，点击打开");
        } else {
            hasFile = false;
            mInfoExport.setText("生成PDF文件");
        }
    }

    @OnClick({R.id.info_export, R.id.info_operation})
    public void onClick(View view) {
        if (view.getId() == R.id.info_export) {
            if (hasFile) {
                startActivity(OpenFileUtil.openFile(filePath));
            } else {
                if (notificationFragment.buildPDF()) {
                    hasFile = true;
                    mInfoExport.setText("已生成PDF文件，点击打开");
                } else {
                    showSnackbar(mInfoExport, "生成文件错误");
                }
            }

        } else {
            model = editNotificationFragment.saveData();
            initNotifyView();
        }
    }

    public static void ToActivity(Context context, Serializable data) {
        Intent intent = new Intent(context, ModifyNotificationActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

}
