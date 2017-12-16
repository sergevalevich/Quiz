package com.valevich.quiz.flows.categories.builder

import com.valevich.quiz.base.dependencies.ActivityScope
import com.valevich.quiz.flows.questions.presenter.QuizPresenter
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(QuizActivityModule::class)])
interface QuizActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        fun quizActivityModule(module: QuizActivityModule) : Builder

        fun build() : QuizActivityComponent
    }

    fun presenter() : QuizPresenter


}