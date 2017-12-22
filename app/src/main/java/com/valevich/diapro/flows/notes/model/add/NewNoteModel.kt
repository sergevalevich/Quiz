package com.valevich.diapro.flows.notes.model.add

import com.valevich.diapro.flows.notes.model.dto.Note
import io.reactivex.Completable

interface NewNoteModel {

    fun createNote(note: Note) : Completable

    fun createNoteSync(note: Note)

}
