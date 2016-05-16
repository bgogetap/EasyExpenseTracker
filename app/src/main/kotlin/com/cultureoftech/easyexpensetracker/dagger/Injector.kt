package com.cultureoftech.easyexpensetracker.dagger

import android.content.Context


class Injector {

    companion object {
        fun put(context: Context, scope: String, component: Any) {
            val cache = getCache(context)
            cache.put(scope, component)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> getComponent(context: Context, scope: String): T? {
            val cache = getCache(context)
            return cache.get(scope) as T
        }

        fun destroy(context: Context, scope: String) {
            val cache = getCache(context)
            cache.destroy(scope)
        }

        fun getCache(context: Context): ComponentCache {
            return context.applicationContext.getSystemService(ComponentCache.SERVICE_NAME) as ComponentCache

        }
    }
}