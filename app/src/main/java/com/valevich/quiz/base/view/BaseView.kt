package com.valevich.quiz.base.view

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun showProgress() {}

    fun hideProgress() {}

    fun showError() {}

    fun hideError() {}

    fun showError(message : String) {}
}