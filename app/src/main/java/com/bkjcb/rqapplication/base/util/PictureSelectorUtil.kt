package com.bkjcb.rqapplication.base.util

import android.app.Activity
import android.support.v4.app.Fragment
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType

object PictureSelectorUtil {

    fun selectPicture(activity: Activity, path: String) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .setOutputCameraPath(FileUtil.getFileOutputPath(path))
                .compress(false)
                .imageFormat(PictureMimeType.PNG) //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    fun selectPicture(fragment: Fragment, path: String, isCompress: Boolean) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .setOutputCameraPath(FileUtil.getFileOutputPath(path))
                .compress(isCompress) /*      .compressSavePath(Environment.getExternalStorageDirectory()
                              + "/RQApp/GasImage/compress")*/
                .minimumCompressSize(300)
                .imageFormat(PictureMimeType.PNG) //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }
}