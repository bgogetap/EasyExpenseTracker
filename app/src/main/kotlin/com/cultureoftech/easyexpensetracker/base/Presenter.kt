package com.cultureoftech.easyexpensetracker.base

abstract class Presenter<V> {

    var view : V? = null

    fun attach(view: V) {
        this.view = view
        viewAttached()
    }

    fun detach() {
        this.view = null
    }

    abstract fun viewAttached()
}