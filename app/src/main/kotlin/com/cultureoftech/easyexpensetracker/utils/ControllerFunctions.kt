package com.cultureoftech.easyexpensetracker.utils

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler

fun Router.pushNormal(controller: Controller) {
    this.pushController(RouterTransaction.builder(controller)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())
            .build())
}

fun Router.replaceNormal(controller: Controller) {
    this.replaceTopController(RouterTransaction.builder(controller)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())
            .build())
}