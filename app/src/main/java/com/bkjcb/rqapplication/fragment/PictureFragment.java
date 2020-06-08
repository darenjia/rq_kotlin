package com.bkjcb.rqapplication.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.util.Utils;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;

/**
 * Created by DengShuai on 2020/3/30.
 * Description :
 */
public class PictureFragment extends Fragment {
   private String path;

    private void setPath(String path) {
        this.path = path;
    }

    public static PictureFragment newInstance(String s) {
        PictureFragment pictureFragment = new PictureFragment();
        pictureFragment.setPath(s);
        return pictureFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BigImageViewer.initialize(GlideImageLoader.with(MyApplication.getContext()));
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_media_image,null);
        BigImageView imageView=view.findViewById(R.id.big_image_view);
        if (!TextUtils.isEmpty(path)&&path.contains("http")){
            imageView.showImage(Uri.parse(path));
        }else {
            imageView.showImage(Utils.getImageContentUri(getContext(),path));
        }

        return view;
    }
}
