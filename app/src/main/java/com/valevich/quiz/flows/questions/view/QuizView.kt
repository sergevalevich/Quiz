package com.valevich.quiz.flows.questions.view

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.valevich.quiz.base.view.BaseView
import com.valevich.quiz.flows.questions.entity.Question

@StateStrategyType(OneExecutionStateStrategy::class)
interface QuizView: BaseView {
    fun updateTimer(progressValue: Int)
    fun showRightAnswer(rightAnswerPosition: Int)
    fun showTitle(title: String)
    fun showSubTitle(subTitle: String)
    fun showCodeSnippet(code: String)
    fun showOptions(answers: List<String>)
    fun disableOptions()
    fun showWrongOption(answer: Int)
    fun animate()
    fun hideQuestion()
    fun showResults(questions: List<Question>)
}