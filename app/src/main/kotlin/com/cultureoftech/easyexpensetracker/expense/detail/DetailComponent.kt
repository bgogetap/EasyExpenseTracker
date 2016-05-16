package com.cultureoftech.easyexpensetracker.expense.detail

import com.cultureoftech.easyexpensetracker.dagger.ForDetail
import com.cultureoftech.easyexpensetracker.photo.AddPhotoDialog
import dagger.Subcomponent

@ForDetail
@Subcomponent(modules = arrayOf(DetailModule::class))
interface DetailComponent {

    fun inject(detailController: DetailController)

    fun inject(addPhotoDialog: AddPhotoDialog)
}