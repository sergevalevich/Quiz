package com.valevich.diapro.rx

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShedulersModule {

    @Singleton
    @Provides
    internal fun provideProdSchedulers() : SchedulersSet = SchedulersSets.Production

}