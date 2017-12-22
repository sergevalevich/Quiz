package com.valevich.diapro.flows.stats.builder

import com.valevich.diapro.base.dependencies.ScreenScope
import com.valevich.diapro.flows.stats.presenter.StatsPresenter
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [(StatsModule::class)])
interface StatsComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : StatsComponent
    }

    fun statsPresenter() : StatsPresenter


}
