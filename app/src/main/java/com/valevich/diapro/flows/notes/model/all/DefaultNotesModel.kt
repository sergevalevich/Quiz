package com.valevich.diapro.flows.notes.model.all

import com.valevich.diapro.api.notes.NotesRepository
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.util.toMillisFrom
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class DefaultNotesModel @Inject constructor(
        private val repository: NotesRepository
) : NotesModel {

    private var selectedNote: Note? = null
    private var selectedNotePosition: Int = -1

    override fun getNotes(): Observable<List<Note>> = repository.loadNotes()
            .map {
                Collections.sort(it, { item1, item2 -> (toMillisFrom(item2.date) - toMillisFrom(item1.date)).toInt() })
                it
            }

    override fun setSelectedItem(note: Note?, position: Int) {
        selectedNote = note
        selectedNotePosition = position
    }

    override fun deleteSelectedNote(): Observable<List<Note>> {
        selectedNote?.let {
            return repository.delete(it).andThen(getNotes())
        }
        return getNotes()
    }

}