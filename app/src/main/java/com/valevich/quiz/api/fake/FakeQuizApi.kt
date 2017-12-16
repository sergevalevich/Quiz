package com.valevich.quiz.api.fake

import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.questions.entity.Question
import com.valevich.quiz.unsafeLazy
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class FakeQuizApi : QuizApi {
    override fun updateAnswers(category: QuizCategory): Observable<QuizCategory> = Observable.just(
            QuizCategory()
    )

    private val fakeCategories: List<QuizCategory> by unsafeLazy {
        listOf(
                QuizCategory(
                        0,
                        "JAVA",
                        "https://goo.gl/c8HbCr",
                        fakeQuestions,
                        fakeQuestions.size - 1
                ),
                QuizCategory(
                        0,
                        "ANDROID",
                        "https://goo.gl/5Ps7o4",
                        fakeQuestions,
                        fakeQuestions.size - 1
                )
        )
    }

    private val fakeQuestions: List<Question> by unsafeLazy {
        listOf(
                Question(
                        categoryId = 0,
                        title = "How many access modifiers exist in JAVA ?",
                        answers = listOf("1", "2", "3", "4"),
                        correctAnswerPosition = 4
                ),
                Question(
                        categoryId = 0,
                        title = "How many access modifiers exist in JAVA ?",
                        answers = listOf("1", "2", "3", "4"),
                        correctAnswerPosition = 4
                )
        )
    }

    override fun quizCategories(): Observable<List<QuizCategory>> = Observable.timer(0, TimeUnit.SECONDS)
            .map({ fakeCategories })

}