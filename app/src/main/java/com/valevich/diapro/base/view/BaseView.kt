package com.valevich.diapro.base.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface BaseView : MvpView {

    fun showProgress() {}

    fun hideProgress() {}

    fun showError() {}

    fun hideError() {}

    fun showError(message : String) {}

    fun reset() {}
}