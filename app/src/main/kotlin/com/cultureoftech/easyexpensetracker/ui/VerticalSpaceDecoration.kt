package com.cultureoftech.easyexpensetracker.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class VerticalSpaceDecoration(val spaceDp: Float): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.bottom = spaceDp.toInt()
    }
}