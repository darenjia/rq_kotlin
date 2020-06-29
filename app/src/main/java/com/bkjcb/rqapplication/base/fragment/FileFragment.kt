package com.bkjcb.rqapplication.base.fragment

import com.bkjcb.rqapplication.R
import kotlinx.android.synthetic.main.fragment_media_file.*

/**
 * Created by DengShuai on 2020/3/30.
 * Description :
 */
class FileFragment : BaseSimpleFragment() {
    private var path: String? = null
    private fun setPath(path: String?) {
        this.path = path
    }

    override fun initResID(): Int = R.layout.fragment_media_file
    override fun initView() {
        if (path != null) {
            file_name.text = path!!.substring(path!!.lastIndexOf("/") + 1)
        }
    }

    companion object {
        fun newInstance(s: String?): FileFragment {
            val pictureFragment = FileFragment()
            pictureFragment.setPath(s)
            return pictureFragment
        }
    }
}