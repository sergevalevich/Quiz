package com.valevich.quiz.flows.categories.presenter

import com.arellomobile.mvp.InjectViewState
import com.valevich.quiz.base.BasePresenter
import com.valevich.quiz.base.ExecutionMode
import com.valevich.quiz.flows.categories.model.QuizCategoriesModel
import com.valevich.quiz.flows.categories.view.QuizCategoriesView
import com.valevich.quiz.rx.SchedulersSet
import javax.inject.Inject

@InjectViewState
class QuizCategoriesPresenter @Inject constructor(
        schedulersSet: SchedulersSet,
        private val model: QuizCategoriesModel
) : BasePresenter<QuizCategoriesView>(schedulersSet) {

    override fun attachView(view: QuizCategoriesView?) {
        super.attachView(view)
        loadCategories()
    }

    fun retry() {
        loadCategories()
    }

    private fun loadCategories() {
        viewState.showProgress()
        execute(
                ExecutionMode.COMPUTATION_DETACH,
                model.getQuizCategories(),
                { categories ->
                    viewState.apply {
                        hideProgress()
                        hideError()
                        showCategories(categories)
                    }
                },
                { throwable ->
                    viewState.apply {
                        hideProgress()
                        showError(throwable.message ?: "")
                    }
                }
        )
    }

}