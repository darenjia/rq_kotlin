package com.bkjcb.rqapplication.base.interfaces;

import com.bkjcb.rqapplication.base.model.MediaFile;

import java.util.List;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public interface OnPageButtonClickListener<T> {
    void onClick(T userInfo);

    void onNext(List<MediaFile> list);
}
