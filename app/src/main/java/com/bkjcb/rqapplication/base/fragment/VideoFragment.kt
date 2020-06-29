package com.bkjcb.rqapplication.base.fragment

import com.bkjcb.rqapplication.R
import kotlinx.android.synthetic.main.fragment_media_video.*

/**
 * Created by DengShuai on 2020/3/30.
 * Description :
 */
class VideoFragment : BaseSimpleFragment() {
    private var path: String? = null
    private fun setPath(path: String) {
        this.path = path
    }

    override fun initView() {
        video_view.setVideoPath(path)
    }

    override fun initResID(): Int {
        return R.layout.fragment_media_video
    }

    companion object {
        fun newInstance(s: String): VideoFragment {
            val pictureFragment = VideoFragment()
            pictureFragment.setPath(s)
            return pictureFragment
        }
    }
}