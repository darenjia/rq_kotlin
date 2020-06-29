package com.bkjcb.rqapplication.base.ftp

import com.bkjcb.rqapplication.base.ftp.FtpUtils.UploadProgressListener
import io.reactivex.Observable
import java.io.File
import java.util.*

/**
 * Created by DengShuai on 2019/4/7.
 * Description :
 */
object UploadTask {
    fun createUploadTask(paths: List<String?>, remotepath: String?, listener: UploadProgressListener?): Observable<Boolean> {
        return Observable.create { emitter ->
            val files = ArrayList<File>()
            if (paths.isNotEmpty()) {
                for (path in paths) {
                    val file = File(path)
                    if (file.exists()) {
                        files.add(file)
                    }
                }
                val utils = FtpUtils()
                val isSuccess = utils.uploadMultiFile(files, remotepath, listener)
                if (isSuccess) {
                    emitter.onNext(true)
                } else {
                    emitter.onNext(false)
                }
            } else {
                emitter.onNext(true)
            }
        }
    }

    @JvmStatic
    fun createUploadTask(paths: List<String?>?, remotepath: String?): Observable<Boolean> {
        return Observable.create { emitter ->
            val files = ArrayList<File>()
            if (paths != null && paths.isNotEmpty()) {
                for (path in paths) {
                    val file = File(path)
                    if (file.exists()) {
                        files.add(file)
                    }
                }
                val utils = FtpUtils()
                try {
                    val isSuccess = utils.uploadMultiFile(files, remotepath)
                    if (isSuccess) {
                        emitter.onNext(true)
                    } else {
                        emitter.onNext(false)
                    }
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            } else {
                emitter.onNext(true)
            }
        }
    }
}