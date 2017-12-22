package com.valevich.diapro.api.notes

import com.google.firebase.database.DatabaseReference
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.tags.model.dto.Tag
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultNotesRepository @Inject constructor(
        private val databaseReference: DatabaseReference
) : NotesRepository {

    override fun loadNotes(): Observable<List<Note>> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                databaseReference.child("notes"), DataSnapshotMapper.listOf(Note::class.java))
                .toObservable()
    }

    override fun createNoteSync(note: Note) {

    }

    override fun loadTags(): Observable<List<Tag>> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                databaseReference.child("tags"), DataSnapshotMapper.listOf(Tag::class.java))
                .toObservable()
    }

    override fun loadNotesForce(): Observable<List<Note>> = loadNotes()

    override fun loadTagsForce(): Observable<List<Tag>> = loadTags()

    override fun createNote(note: Note): Completable {
        return RxFirebaseDatabase.setValue(databaseReference.child("notes/${note.id}"), note)
    }

    override fun delete(note: Note): Completable {
        return RxFirebaseDatabase.setValue(databaseReference.child("notes/${note.id}"), null)
    }

    override fun update(tag: Tag): Completable {
        return RxFirebaseDatabase.setValue(databaseReference.child("tags/${tag.id}"), tag)
    }

    override fun delete(tag: Tag): Completable {
        return RxFirebaseDatabase.setValue(databaseReference.child("tags/${tag.id}"), null)
    }


}