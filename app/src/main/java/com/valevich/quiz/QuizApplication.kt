package com.valevich.quiz

import android.app.Application
import com.valevich.quiz.base.dependencies.ApplicationComponent
import com.valevich.quiz.base.dependencies.DaggerApplicationComponent
import timber.log.Timber


class QuizApplication : Application() {

    private val applicationComponent : ApplicationComponent by unsafeLazy {
        DaggerApplicationComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun getAppComponent(): ApplicationComponent = applicationComponent

}