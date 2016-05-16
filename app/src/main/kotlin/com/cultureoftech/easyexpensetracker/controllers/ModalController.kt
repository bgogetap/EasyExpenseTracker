package com.cultureoftech.easyexpensetracker.controllers

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.cultureoftech.easyexpensetracker.R

abstract class ModalController: ScopedController() {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_modal_base, container, false)
        val container = view.findViewById(R.id.fl_modal_content_container) as FrameLayout
        inflater.inflate(getContentView(), container, true)
        return view
    }

    @LayoutRes abstract fun getContentView(): Int
}
