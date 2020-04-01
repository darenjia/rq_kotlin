package com.bkjcb.rqapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;

/**
 * Created by DengShuai on 2020/3/30.
 * Description :
 */
public class FileFragment extends Fragment {
    private String path;

    private void setPath(String path) {
        this.path = path;
    }

    public static FileFragment newInstance(String s) {
        FileFragment pictureFragment = new FileFragment();
        pictureFragment.setPath(s);
        return pictureFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_file, null);
        TextView textView = view.findViewById(R.id.file_name);
        textView.setText(path.substring(path.lastIndexOf("/") + 1));
        return view;
    }
}
