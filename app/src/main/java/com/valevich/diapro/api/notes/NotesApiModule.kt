package com.valevich.diapro.api.notes

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class NotesApiModule {

//    @Binds
//    @Singleton
//    abstract fun provideRepo(repository: DefaultNotesRepository): NotesRepository

    @Binds
    @Singleton
    abstract fun provideRepo(
            repository: PreferenceRepository
    ): NotesRepository

}