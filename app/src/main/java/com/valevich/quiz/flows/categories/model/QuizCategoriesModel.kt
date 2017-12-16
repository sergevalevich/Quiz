package com.valevich.quiz.flows.categories.model

import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.flows.categories.entity.QuizCategory
import io.reactivex.Observable
import javax.inject.Inject


class QuizCategoriesModel @Inject constructor(private val quizApi : QuizApi) {

    fun getQuizCategories() : Observable<List<QuizCategory>> = quizApi.quizCategories()

}
