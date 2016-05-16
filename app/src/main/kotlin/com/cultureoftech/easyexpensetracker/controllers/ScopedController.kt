package com.cultureoftech.easyexpensetracker.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cultureoftech.easyexpensetracker.dagger.Injector
import com.cultureoftech.easyexpensetracker.ui.BaseController


abstract class ScopedController(bundle: Bundle): BaseController(bundle) {

    constructor(): this(Bundle())

    private var firstRun = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        if (firstRun) {
            Injector.put(router.activity, getScope(), initComponent())
            firstRun = false
        }
        return super.onCreateView(inflater, container)
    }

    abstract fun getScope(): String

    abstract fun initComponent(): Any

    protected fun getBundleWithScope(): Bundle {
        val bundle = Bundle()
        bundle.putString("scope", getScope())
        return bundle
    }
}