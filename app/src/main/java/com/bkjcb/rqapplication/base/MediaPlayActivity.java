package com.bkjcb.rqapplication.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.base.fragment.FileFragment;
import com.bkjcb.rqapplication.base.fragment.PictureFragment;
import com.bkjcb.rqapplication.base.fragment.VideoFragment;
import com.bkjcb.rqapplication.base.model.MediaFile;
import com.bkjcb.rqapplication.base.util.OpenFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengShuai on 2020/3/18.
 * Description :
 */
public class MediaPlayActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private String[] pathArray;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initView();
    }

    private void initView() {
        ViewPager viewPager = findViewById(R.id.frame_layout);
        List<Fragment> fragments = new ArrayList<>();

        Fragment fragment;
        String pathString = getIntent().getStringExtra("Path");
        if (TextUtils.isEmpty(pathString)) {
            return;
        }
        pathArray = pathString.split(",");
        path = pathArray[0];
        int position = getIntent().getIntExtra("position", 0);
        for (String s : pathArray) {
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(this, "文件路径出错！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (s.toLowerCase().endsWith(".png") || s.toLowerCase().endsWith(".jpg")) {
                fragment = PictureFragment.newInstance(s);
            } else if (s.toLowerCase().endsWith(".mp4")) {
                fragment = VideoFragment.newInstance(s);
            } else {
                fragment = FileFragment.newInstance(s);
            }
            fragments.add(fragment);
        }

        TextView openBtn = findViewById(R.id.open_file);
        if (path.contains("http")) {
            openBtn.setVisibility(View.GONE);
        }
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(OpenFileUtil.openFile(path));

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(position, false);
    }

    public static void ToActivity(Context context, String path) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra("Path", path);
        context.startActivity(intent);
    }

    public static void ToActivity(Context context, String path, int position) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra("Path", path);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public static void ToActivity(Context context, List<MediaFile> list, int position) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra("Path", listToString(list));
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    private static String listToString(List<MediaFile> list) {
        if (list != null && list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (MediaFile file : list) {
                builder.append(file.getPath()).append(",");
            }
            return builder.substring(0, builder.length() - 1);
        }
        return "";
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        path = pathArray[i];
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

