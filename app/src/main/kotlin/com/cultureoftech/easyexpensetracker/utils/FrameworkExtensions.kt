package com.cultureoftech.easyexpensetracker.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import java.io.File

fun Intent.hasActivity(context: Context): Boolean {
    return context.packageManager.queryIntentActivities(this, PackageManager.MATCH_ALL).size > 0
}

fun getNewImageFileUri(context: Context): Uri {
    val appImageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "EasyExpenseTracker")
    imageDirectory.mkdirs()
    val imageFile = File(appImageDir, "IMG_" + System.currentTimeMillis() + ".jpg")
    return Uri.fromFile(imageFile)
}

fun alert(
        context: Context,
        @StringRes message: Int,
        @StringRes positiveText: Int,
        positiveClick: DialogInterface.OnClickListener
): Dialog {
    return AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(positiveText, { dialog, view ->
                run {
                    dialog.dismiss()
                    positiveClick.onClick(dialog, view)
                }
            })
            .setNegativeButton(android.R.string.cancel, { dialogInterface, i -> dialogInterface.dismiss() })
            .create()
}
