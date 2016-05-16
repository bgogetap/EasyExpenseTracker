package com.cultureoftech.easyexpensetracker.ui

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import com.github.clans.fab.FloatingActionMenu


class FloatingActionMenuBehavior: CoordinatorLayout.Behavior<FloatingActionMenu> {

    constructor(): super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: FloatingActionMenu?, dependency: View?): Boolean {
        return dependency is Snackbar.SnackbarLayout || dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionMenu, dependency: View): Boolean {
        super.onDependentViewChanged(parent, child, dependency)

        if (dependency is Snackbar.SnackbarLayout) {
            val translationY = Math.min(0F, dependency.translationY - dependency.height)
            child.translationY = translationY
        }

        return true
    }
}