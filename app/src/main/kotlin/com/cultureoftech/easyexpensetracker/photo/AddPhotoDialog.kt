package com.cultureoftech.easyexpensetracker.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.OnClick
import com.cultureoftech.easyexpensetracker.R
import com.cultureoftech.easyexpensetracker.ui.BaseController


class AddPhotoDialog : BaseController() {

    @BindView(R.id.fl_dialog_layout) lateinit var layout: View

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.dialog_take_or_choose_photo, container, false)
    }

    override fun onViewBound(view: View) {
        layout.setOnTouchListener { view, motionEvent -> true }
    }

    @OnClick(R.id.btn_take_photo) fun takePhoto() {
        (parentController as PhotoDialogClickListener).takePhoto()
        parentController.removeChildController(this)
    }

    @OnClick(R.id.btn_choose_existing) fun choosePhoto() {
        (parentController as PhotoDialogClickListener).choosePhoto()
        parentController.removeChildController(this)
    }

    interface PhotoDialogClickListener {

        fun takePhoto()

        fun choosePhoto()
    }
}