package com.valevich.diapro.flows.notes.builder

import com.valevich.diapro.flows.notes.model.add.DefaultNewNoteModel
import com.valevich.diapro.flows.notes.model.add.NewNoteModel
import com.valevich.diapro.flows.notes.model.all.DefaultNotesModel
import com.valevich.diapro.flows.notes.model.all.NotesModel
import dagger.Binds
import dagger.Module

@Module
abstract class NotesModule {

    @Binds
    abstract fun provideNotesModel(model: DefaultNotesModel): NotesModel

    @Binds
    abstract fun provideNewNoteModel(model: DefaultNewNoteModel): NewNoteModel

}
