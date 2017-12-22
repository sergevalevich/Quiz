package com.valevich.diapro.flows.stats.builder

import com.valevich.diapro.flows.stats.model.DefaultStatsModel
import com.valevich.diapro.flows.stats.model.StatsModel
import dagger.Binds
import dagger.Module

@Module
abstract class StatsModule {

    @Binds
    abstract fun provideStatsModel(model: DefaultStatsModel): StatsModel

}
