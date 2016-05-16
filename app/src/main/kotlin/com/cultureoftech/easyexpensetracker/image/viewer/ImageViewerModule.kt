package com.cultureoftech.easyexpensetracker.image.viewer

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ImageViewerModule(val imagePath: String) {

    @Provides @Named("imagePath") fun provideImagePath(): String {
        return imagePath;
    }
}