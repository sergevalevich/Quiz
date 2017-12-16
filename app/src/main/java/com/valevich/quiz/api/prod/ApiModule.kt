package com.valevich.quiz.api.prod

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.api.fake.FakeApiModule
import com.valevich.quiz.api.firebase.FirebaseQuizApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FakeApiModule::class])
class ApiModule {

    @Singleton
    @Provides
    internal fun provideProductionApi(
            databaseReference: DatabaseReference
    ): QuizApi = FirebaseQuizApi(databaseReference)

    @Singleton
    @Provides
    internal fun provideDataBaseReference(): DatabaseReference = FirebaseDatabase
            .getInstance()
            .reference

}