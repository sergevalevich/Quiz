package com.valevich.diapro.flows.stats.model

import com.valevich.diapro.flows.stats.model.dto.StatsData
import com.valevich.diapro.flows.tags.model.dto.Tag
import io.reactivex.Observable

interface StatsModel {

    fun getStatsData(tag: Tag? = null) : Observable<StatsData>

}