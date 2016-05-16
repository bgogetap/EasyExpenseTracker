package com.cultureoftech.easyexpensetracker.image.viewer

import android.net.Uri
import com.cultureoftech.easyexpensetracker.base.Presenter
import com.cultureoftech.easyexpensetracker.dagger.ForImageViewer
import javax.inject.Inject
import javax.inject.Named

@ForImageViewer
class ImageViewerPresenter @Inject constructor(
        @Named("imagePath") val imagePath: String
): Presenter<ImageViewerView>(){

    override fun viewAttached() {
        view?.setImagePath(imagePath)
    }

    fun getImageUri(): Uri {
        return Uri.parse(imagePath)
    }
}