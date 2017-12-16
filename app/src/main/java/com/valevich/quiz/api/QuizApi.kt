package com.valevich.quiz.api

import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.questions.entity.Question
import io.reactivex.Completable
import io.reactivex.Observable

interface QuizApi {

    fun quizCategories() : Observable<List<QuizCategory>>

    fun updateAnswers(category: QuizCategory) : Observable<QuizCategory>

}