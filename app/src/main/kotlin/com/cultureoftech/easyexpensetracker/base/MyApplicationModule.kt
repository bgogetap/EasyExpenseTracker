package com.cultureoftech.easyexpensetracker.base

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.preference.PreferenceManager
import com.bluelinelabs.conductor.Router
import com.cultureoftech.easyexpensetracker.dao.DaoMaster
import com.cultureoftech.easyexpensetracker.dao.DaoSession
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MyApplicationModule(val application: MyApplication) {

    @Provides fun provideContext(): Context {
        return application
    }

    @Provides @Singleton fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides @Singleton fun provideRouter() : Router {
        return Router()
    }

    @Provides @Singleton fun provideDaoSession(context: Context) : DaoSession {
        val helper = object: DaoMaster.OpenHelper(context, "expense-db", null){
            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

            }
        }
        val db = helper.writableDatabase
        val daoMaster = DaoMaster(db)
        return daoMaster.newSession()
    }
}