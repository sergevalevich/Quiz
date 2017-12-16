package com.valevich.quiz.api.fake

import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.base.dependencies.Fake
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class FakeApiModule {

    @Fake
    @Singleton
    @Provides
    internal fun provideFakeApi() : QuizApi = FakeQuizApi()

}