package com.valevich.quiz.base.dependencies

import com.valevich.quiz.api.prod.ApiModule
import com.valevich.quiz.flows.categories.builder.QuizActivityComponent
import com.valevich.quiz.flows.categories.builder.QuizCategoriesActivityComponent
import com.valevich.quiz.rx.ShedulersModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ApiModule::class,
    ShedulersModule::class
])
interface ApplicationComponent {

    fun categoriesActivityComponentBuilder() : QuizCategoriesActivityComponent.Builder

    fun quizActivityComponentBuilder() : QuizActivityComponent.Builder

}

