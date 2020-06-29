package com.bkjcb.rqapplication.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.fragment.FileFragment
import com.bkjcb.rqapplication.base.fragment.PictureFragment
import com.bkjcb.rqapplication.base.fragment.VideoFragment
import com.bkjcb.rqapplication.base.util.OpenFileUtil.openFile
import java.util.*

/**
 * Created by DengShuai on 2020/3/18.
 * Description :
 */
class MediaPlayActivity : AppCompatActivity() {
    private var path: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_media)
        initView()
    }

    private fun initView() {
        val fragment: Fragment
        path = intent.getStringExtra("Path")
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "文件路径出错！", Toast.LENGTH_SHORT).show()
            return
        }
        fragment = if (path!!.toLowerCase(Locale.ROOT).endsWith(".png") || path!!.toLowerCase(Locale.ROOT).endsWith(".jpg")) {
            PictureFragment.newInstance(path!!)
        } else if (path!!.toLowerCase(Locale.ROOT).endsWith(".mp4")) {
            VideoFragment.newInstance(path!!)
        } else {
            FileFragment.newInstance(path)
        }
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, fragment).commit()
        val view = findViewById<TextView>(R.id.open_file)
        if (!path!!.contains("http")) {
            view.setOnClickListener { startActivity(openFile(path)) }
        } else {
            view.visibility = View.GONE
        }
    }

    companion object {
        fun toActivity(context: Context, path: String?) {
            val intent = Intent(context, MediaPlayActivity::class.java)
            intent.putExtra("Path", path)
            context.startActivity(intent)
        }
    }
}