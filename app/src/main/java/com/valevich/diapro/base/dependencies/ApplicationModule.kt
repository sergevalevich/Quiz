package com.valevich.diapro.base.dependencies

import com.valevich.diapro.DiaApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application : DiaApplication) {

    @Provides
    internal fun provideApplication() = application

}