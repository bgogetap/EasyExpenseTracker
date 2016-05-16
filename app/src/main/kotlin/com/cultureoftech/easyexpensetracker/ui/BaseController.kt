package com.cultureoftech.easyexpensetracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller
import com.cultureoftech.easyexpensetracker.base.MainActivity


abstract class BaseController(args: Bundle) : Controller(args) {

    protected constructor() : this(Bundle())

    var unBinder: Unbinder? = null

    abstract fun onViewBound(view: View)

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflateView(inflater, container)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        unBinder = ButterKnife.bind(this, view)
        onViewBound(view)
        updateTitle(getTitle())
    }

    open protected fun getTitle(): String? {
        return null
    }

    protected fun updateTitle(title: String?) {
        (activity as MainActivity).updateTitle(title)
    }

    override fun onDestroyView(view: View?) {
        super.onDestroyView(view)
        unBinder?.unbind()
    }
}