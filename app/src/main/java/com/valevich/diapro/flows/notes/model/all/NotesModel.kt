package com.valevich.diapro.flows.notes.model.all

import com.valevich.diapro.flows.notes.model.dto.Note
import io.reactivex.Completable
import io.reactivex.Observable

interface NotesModel {

    fun getNotes() : Observable<List<Note>>

    fun setSelectedItem(note: Note?, position: Int)

    fun deleteSelectedNote() : Observable<List<Note>>
}