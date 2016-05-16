package com.cultureoftech.easyexpensetracker.image.viewer

import com.cultureoftech.easyexpensetracker.dagger.ForImageViewer
import dagger.Subcomponent

@ForImageViewer
@Subcomponent(modules = arrayOf(ImageViewerModule::class))
interface ImageViewerComponent {

    fun inject(controller: ImageViewerController)
}