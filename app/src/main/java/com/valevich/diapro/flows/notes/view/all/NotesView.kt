package com.valevich.diapro.flows.notes.view.all

import com.valevich.diapro.base.view.BaseView
import com.valevich.diapro.flows.notes.model.dto.Note

interface NotesView : BaseView {

    fun showNotes(notes: List<Note>)

    fun openEditNoteScreen(note: Note)

    fun openNewNoteScreen()

    fun removeListItem(position: Int)

}