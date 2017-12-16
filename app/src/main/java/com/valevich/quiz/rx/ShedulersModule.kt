package com.valevich.quiz.rx

import com.valevich.quiz.base.dependencies.Fake
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShedulersModule {

    @Singleton
    @Provides
    internal fun provideProdSchedulers() : SchedulersSet = SchedulersSets.Production

    @Fake
    @Singleton
    @Provides
    internal fun provideTestSchedulers() : SchedulersSet = SchedulersSets.Test

}