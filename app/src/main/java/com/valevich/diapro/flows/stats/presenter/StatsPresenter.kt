package com.valevich.diapro.flows.stats.presenter

import com.arellomobile.mvp.InjectViewState
import com.valevich.diapro.base.BasePresenter
import com.valevich.diapro.base.ExecutionMode
import com.valevich.diapro.flows.stats.model.StatsModel
import com.valevich.diapro.flows.stats.view.StatsView
import com.valevich.diapro.flows.tags.model.dto.Tag
import com.valevich.diapro.rx.SchedulersSet
import javax.inject.Inject

@InjectViewState
class StatsPresenter @Inject constructor(
        private val model: StatsModel,
        schedulersSet: SchedulersSet
) : BasePresenter<StatsView>(schedulersSet) {

    override fun attachView(view: StatsView?) {
        super.attachView(view)
        loadStats()
    }

    fun loadStats(tag : Tag? = null) {
        execute(
                ExecutionMode.IO_DETACH,
                model.getStatsData(tag),
                { stats ->
                    viewState.hideProgress()
                    viewState.showTags(stats.tags)
                    viewState.showStatsByTag(stats.notes, tag)
                },
                {
                    viewState.showError(it.message ?: "")
                },
                {},
                {
                    it.doOnSubscribe {
                        viewState.showProgress()
                    }.doOnDispose {
                        viewState.hideProgress()
                    }
                }
        )
    }

}