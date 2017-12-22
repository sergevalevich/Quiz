package com.valevich.diapro

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.valevich.diapro.api.prod.ApiModule
import com.valevich.diapro.base.dependencies.ApplicationComponent
import com.valevich.diapro.base.dependencies.DaggerApplicationComponent
import timber.log.Timber


class DiaApplication : Application() {

    private val applicationComponent : ApplicationComponent by unsafeLazy {
        DaggerApplicationComponent.builder()
                .apiModule(ApiModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(false)
    }

    fun getAppComponent(): ApplicationComponent = applicationComponent

}