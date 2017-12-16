package com.valevich.quiz.base.dependencies

import com.valevich.quiz.QuizApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application : QuizApplication) {

    @Provides
    internal fun provideApplication() = application

}