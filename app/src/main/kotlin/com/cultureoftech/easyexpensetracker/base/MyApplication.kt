package com.cultureoftech.easyexpensetracker.base

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.cultureoftech.easyexpensetracker.dagger.ComponentCache
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject


class MyApplication : Application() {

    lateinit @Inject var componentCache: ComponentCache

    val component = createComponent()

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        Fabric.with(this, Crashlytics())
        Timber.plant(Timber.DebugTree())
    }

    private fun createComponent(): MyApplicationComponent {
        return DaggerMyApplicationComponent.builder()
                .myApplicationModule(MyApplicationModule(this))
                .build()
    }

    override fun getSystemService(name: String): Any? {
        if (name.equals(ComponentCache.SERVICE_NAME)) {
            return componentCache
        }
        return if (componentCache.has(name))
            componentCache.get(name) else super.getSystemService(name)
    }
}