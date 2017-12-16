package com.valevich.quiz.flows.questions.model

import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.questions.entity.Question
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class QuizModel(
        private val questions: Stack<Question>,
        private val category: QuizCategory,
        private val quizApi: QuizApi
) {

    private val answeredQuestions: MutableList<Question> = mutableListOf()

    fun countDown(): Observable<Long> = Observable.interval(100L, TimeUnit.MILLISECONDS).map { it * 100 }.take(310)

    fun getNextQuestion(): Observable<Question> = Observable.fromCallable {
        try {
            questions.pop().also { answeredQuestions.add(it) }
        } catch (ese: EmptyStackException) {
            Question(-1, "terminate")
        }
    }

    fun acceptAnswer(answer: Int): Observable<Question> =
            Observable.fromCallable {
                currentQuestion().let {
                    it.copy(hasAnsweredCorrect = it.correctAnswerPosition == answer)
                            .apply {
                                answeredQuestions.remove(it)
                                answeredQuestions.add(this)
                            }
                }
            }

    fun currentQuestion(): Question = answeredQuestions.last()

    fun getAnsweredQuestions(): Observable<List<Question>> = quizApi.updateAnswers(createCategory())
            .take(1)
            .flatMap { Observable.just(answeredQuestions.toList()) }

    private fun createCategory(): QuizCategory =
            category.copy(
                    answered = answeredQuestions.filter { it.hasAnsweredCorrect }.size,
                    questions = answeredQuestions
            )


}