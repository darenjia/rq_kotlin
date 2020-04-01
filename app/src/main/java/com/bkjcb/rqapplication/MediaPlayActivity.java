package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bkjcb.rqapplication.fragment.FileFragment;
import com.bkjcb.rqapplication.fragment.PictureFragment;
import com.bkjcb.rqapplication.fragment.VideoFragment;
import com.bkjcb.rqapplication.util.OpenFileUtil;

import static android.view.Window.FEATURE_NO_TITLE;

/**
 * Created by DengShuai on 2020/3/18.
 * Description :
 */
public class MediaPlayActivity extends AppCompatActivity {

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.activity_media);
        initView();
    }

    private void initView() {
        Fragment fragment;
        path = getIntent().getStringExtra("Path");
        if (path.endsWith(".png") || path.endsWith(".jpg")) {
            fragment = PictureFragment.newInstance(path);
        } else if (path.endsWith(".mp4")) {
            fragment = VideoFragment.newInstance(path);
        }else {
            fragment= FileFragment.newInstance(path);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
        findViewById(R.id.open_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OpenFileUtil.openFile(path));
            }
        });
    }

    public static void ToActivity(Context context, String path) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra("Path", path);
        context.startActivity(intent);
    }
}

