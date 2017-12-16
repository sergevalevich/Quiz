package com.valevich.quiz.flows.categories.builder

import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.questions.entity.Question
import com.valevich.quiz.flows.questions.model.QuizModel
import dagger.Module
import dagger.Provides
import java.util.*


@Module
class QuizActivityModule(
        private val category: QuizCategory
) {

    @Provides
    fun provideQuizModel(
            quizApi: QuizApi
    ): QuizModel = QuizModel(
            stack(),
            category,
            quizApi
    )

    private fun stack(): Stack<Question> {
        val questionsList: List<Question> = category.questions
        val questionsStack: Stack<Question> = Stack()
        questionsStack.addAll(questionsList)
        return questionsStack
    }

}

