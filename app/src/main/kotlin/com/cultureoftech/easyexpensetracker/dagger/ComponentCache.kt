package com.cultureoftech.easyexpensetracker.dagger

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComponentCache @Inject constructor() {

    companion object {
        val SERVICE_NAME = "component_cache"
    }

    private var componentMap = LinkedHashMap<String, Any>()

    fun put(scope: String, component: Any) = componentMap.put(scope, component)

    fun get(scope: String): Any? = componentMap[scope]

    fun destroy(scope: String) = componentMap.remove(scope)

    fun has(name: String?): Boolean = componentMap[name] != null
}