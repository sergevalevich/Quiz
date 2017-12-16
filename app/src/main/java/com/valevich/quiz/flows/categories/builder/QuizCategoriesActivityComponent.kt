package com.valevich.quiz.flows.categories.builder

import com.valevich.quiz.base.dependencies.ActivityScope
import com.valevich.quiz.flows.categories.presenter.QuizCategoriesPresenter
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(QuizCategoriesActivityModule::class)])
interface QuizCategoriesActivityComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : QuizCategoriesActivityComponent
    }

    fun presenter() : QuizCategoriesPresenter


}