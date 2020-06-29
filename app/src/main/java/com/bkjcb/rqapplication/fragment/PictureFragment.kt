package com.bkjcb.rqapplication.fragment

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.util.Utils
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.github.piasy.biv.view.BigImageView
import kotlinx.android.synthetic.main.fragment_media_image.*

/**
 * Created by DengShuai on 2020/3/30.
 * Description :
 */
class PictureFragment : BaseSimpleFragment() {
    private var path: String? = null
    private fun setPath(path: String) {
        this.path = path
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BigImageViewer.initialize(GlideImageLoader.with(MyApplication.getContext()))
    }

    override fun initView() {
        if (path != null) {
            if (!TextUtils.isEmpty(path) && path!!.contains("http")) {
                big_image_view.showImage(Uri.parse(path))
            } else {
                big_image_view.showImage(Utils.getImageContentUri(context!!, path!!))
            }
        }
    }

    override fun initResID(): Int {
        return R.layout.fragment_media_image
    }

    companion object {
        fun newInstance(s: String): PictureFragment {
            val pictureFragment = PictureFragment()
            pictureFragment.setPath(s)
            return pictureFragment
        }
    }
}