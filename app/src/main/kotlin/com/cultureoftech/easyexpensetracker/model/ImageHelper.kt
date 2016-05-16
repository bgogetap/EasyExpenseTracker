package com.cultureoftech.easyexpensetracker.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.utils.getNewImageFileUri
import com.cultureoftech.easyexpensetracker.utils.hasActivity
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageHelper @Inject constructor(val context: Context) {

    var lastGeneratedUri = Uri.EMPTY
        private set

    /**
     * Will return null if activity to open is not available
     */
    fun getCameraIntent(): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        lastGeneratedUri = getNewImageFileUri(context)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, lastGeneratedUri)
        if (intent.hasActivity(context)) {
            return intent
        }
        return null
    }

    fun getImageChooserIntent(): Intent? {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        return Intent.createChooser(intent, context.getString(R.string.choose_image))
    }

    fun copyImageToCacheObservable(uri: Uri): Observable<String> {
        return Observable.fromCallable {
            val newUri = getNewImageFileUri(context)
            val newFile = newUri.path
            val outputStream = FileOutputStream(newFile)
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.copyTo(outputStream)
            outputStream?.close()
            inputStream?.close()
            return@fromCallable newFile
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun copyFromTempToAppStorage(imagePath: String): String {
        val tempFile = File(imagePath)
        val newUri = getNewImageFileUri(context)
        val outputStream = FileOutputStream(newUri.path)
        val inputStream = context.contentResolver.openInputStream(Uri.fromFile(tempFile))
        inputStream.copyTo(outputStream)
        tempFile.delete()
        return newUri.path
    }
}