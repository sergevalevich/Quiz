package com.valevich.diapro.api.prod

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule(val app: Application) {

    @Singleton
    @Provides
    internal fun provideDataBaseReference(): DatabaseReference = FirebaseDatabase
            .getInstance()
            .reference

    @Singleton
    @Provides
    internal fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Singleton
    @Provides
    internal fun provideGson(): Gson = Gson()

}