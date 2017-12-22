package com.valevich.diapro.api.notes

import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.tags.model.dto.Tag
import io.reactivex.Completable
import io.reactivex.Observable

interface NotesRepository {

    fun loadNotes() : Observable<List<Note>>

    fun loadNotesForce() : Observable<List<Note>>

    fun loadTagsForce() : Observable<List<Tag>>

    fun createNote(note: Note) : Completable

    fun createNoteSync(note: Note)

    fun delete(note: Note): Completable

    fun update(tag: Tag): Completable

    fun delete(tag: Tag): Completable

    fun loadTags(): Observable<List<Tag>>

}