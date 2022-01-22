package com.ku_stacks.ku_ring.ui.detail

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import java.net.URLDecoder

class DetailViewModel : ViewModel() {

    lateinit var downloadManager: DownloadManager
    private var downloadId = 0L

    private val _snackBarEvent = SingleLiveEvent<Int>()
    val snackBarEvent: SingleLiveEvent<Int>
        get() = _snackBarEvent

    val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1

            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                if (downloadId != id) {
                    return
                }

                val query: DownloadManager.Query = DownloadManager.Query().apply {
                    setFilterById(id)
                }
                val cursor = downloadManager.query(query)
                if (!cursor.moveToFirst()) {
                    return
                }

                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        _snackBarEvent.value = R.string.download_success
                    }
                    DownloadManager.STATUS_FAILED -> {
                        _snackBarEvent.value = R.string.download_fail
                    }
                }
            }
        }
    }

    fun startDownload(
        url: String,
        userAgent: String,
        contentDisposition: String,
        mimetype: String,
    ) {
        var fileName =
            URLDecoder.decode(contentDisposition, "UTF-8").replace("attachment; filename=", "")

        if (fileName.endsWith(";")) {
            fileName = fileName.substring(0, fileName.length - 1)
        }
        if (fileName.startsWith("\"")) {
            fileName = fileName.substring(1, fileName.length)
        }
        if (fileName.endsWith("\"")) {
            fileName = fileName.substring(0, fileName.length - 1)
        }

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimetype))
            addRequestHeader("User-Agent", userAgent)
            setDescription("Downloading File")
            setTitle(fileName)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }
        downloadId = downloadManager.enqueue(request)
    }
}