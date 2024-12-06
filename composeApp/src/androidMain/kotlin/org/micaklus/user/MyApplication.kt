package org.micaklus.user

import android.app.Application
import android.content.Context
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.micaklus.user.cache.AndroidDatabaseDriverFactory

import org.micaklus.user.onlineuser.di.onlineUserModule

import org.micaklus.user.user.di.offlineUserModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin(applicationContext)
    }

    private fun initKoin(context: Context) {
        val androidModule = module {
            single { AndroidDatabaseDriverFactory(context).createDriver() }
        }
        startKoin {
            modules(androidModule, offlineUserModule, onlineUserModule)
        }
    }

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance(): MyApplication = instance
    }


}