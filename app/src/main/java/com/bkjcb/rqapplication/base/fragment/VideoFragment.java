package com.bkjcb.rqapplication.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.bkjcb.rqapplication.R;

/**
 * Created by DengShuai on 2020/3/30.
 * Description :
 */
public class VideoFragment extends Fragment {
   private String path;

    private void setPath(String path) {
        this.path = path;
    }

    public static VideoFragment newInstance(String s) {
        VideoFragment pictureFragment = new VideoFragment();
        pictureFragment.setPath(s);
        return pictureFragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_media_video,null);
        VideoView imageView=view.findViewById(R.id.video_view);
        imageView.setVideoPath(path
        );
        return view;
    }
}
