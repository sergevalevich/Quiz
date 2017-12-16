package com.valevich.quiz.flows.categories.view

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.valevich.quiz.base.view.BaseView
import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.questions.entity.Question

@StateStrategyType(OneExecutionStateStrategy::class)
interface QuizCategoriesView : BaseView {

    fun showCategories(quizCategories: List<QuizCategory>)

    fun goToQuiz(category: QuizCategory)

}