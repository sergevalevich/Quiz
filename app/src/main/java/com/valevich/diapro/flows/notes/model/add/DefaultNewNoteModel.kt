package com.valevich.diapro.flows.notes.model.add

import com.valevich.diapro.api.notes.NotesRepository
import com.valevich.diapro.flows.notes.model.dto.Note
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class DefaultNewNoteModel @Inject constructor(
        private val notesRepository: NotesRepository
) : NewNoteModel {

    override fun createNote(note: Note): Completable = if (note.sugarLevel < 0)
        Completable.error(RuntimeException("Заполните уровень сахара!"))
    else createNoteWithId(note)

    override fun createNoteSync(note: Note) {
        createNoteWithIdSync(note)
    }

    private fun createNoteWithId(note: Note): Completable =
            if (note.id == -1L) {
                notesRepository.createNote(note.copy(id = UUID.randomUUID().leastSignificantBits))
            } else {
                notesRepository.createNote(note)
            }

    private fun createNoteWithIdSync(note: Note) {
        if (note.id == -1L) {
            notesRepository.createNote(note.copy(id = UUID.randomUUID().leastSignificantBits))
        } else {
            notesRepository.createNote(note)
        }
    }

}