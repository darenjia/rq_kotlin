package com.bkjcb.rqapplication.util

import android.app.Activity
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
}