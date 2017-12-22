package com.valevich.diapro.api.notes

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.valevich.diapro.common.Prefs
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.tags.model.dto.Tag
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferenceRepository @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : NotesRepository {

    override fun loadNotes(): Observable<List<Note>> {
        return Observable.fromCallable { getNotesList() }
    }

    override fun loadTags(): Observable<List<Tag>> {
        return Observable.fromCallable { getTagsList() }
    }

    override fun loadNotesForce(): Observable<List<Note>> = loadNotes()

    override fun loadTagsForce(): Observable<List<Tag>> = loadTags()

    override fun createNote(note: Note): Completable = Completable.fromAction {
        createNoteSync(note)
    }

    override fun createNoteSync(note: Note) {
        val notes = getNotesList()
        try {
            notes.first { it.id == note.id }.also {
                setList(Prefs.NOTES, getNotesList().minus(it).plus(note))
            }
        } catch (e: Exception) {
            setList(Prefs.NOTES, notes.plus(note))
        }
    }

    override fun delete(note: Note): Completable = Completable.fromAction {
        setList(Prefs.NOTES, getNotesList().minus(note))
    }

    override fun update(tag: Tag): Completable = Completable.fromAction {
        val tags = getTagsList()
        try {
            tags.first { it.id == tag.id }.let {
                setList(Prefs.TAGS, getTagsList().minus(it).plus(tag))
            }
        } catch (e: Exception) {
            setList(Prefs.TAGS, getTagsList().plus(tag))
        }
    }

    override fun delete(tag: Tag): Completable = Completable.fromAction {
        setList(Prefs.TAGS, getTagsList().minus(tag))
    }

    private fun <T> setList(key: String, list: List<T>) {
        setString(key, gson.toJson(list))
    }

    private fun getNotesList(): List<Note> {
        return listOf(
                Note(sugarLevel = 15.4, date = "2017-04-01 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 8.4, date = "2017-04-02 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4, date = "2017-04-03 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 5.4, date = "2017-04-04 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 6.4, date = "2017-04-05 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 2.4,date = "2017-04-06 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 8.0,date = "2017-04-07 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 3.8,date = "2017-04-08 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-09 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 7.4,date = "2017-04-10 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-11 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 6.5,date = "2017-04-12 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-13 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 6.4,date = "2017-04-14 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-15 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 20.0,date = "2017-04-16 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 3.0,date = "2017-04-17 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-18 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 15.4,date = "2017-04-19 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 6.4,date = "2017-04-20 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-21 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 8.0,date = "2017-04-22 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 4.4,date = "2017-04-23 24:00", tags = listOf(Tag(name = "После работы"))),
                Note(sugarLevel = 3.4, tags = listOf(Tag(name = "После работы")))
        )
//        val type = object : TypeToken<ArrayList<Note>>() {}.type
//        return gson.fromJson(sharedPreferences.getString(Prefs.NOTES, ""), type) ?: listOf()
    }

    private fun getTagsList(): List<Tag> {
        return listOf(Tag(name = "После работы"))
//        val type = object : TypeToken<ArrayList<Tag>>() {}.type
//        return gson.fromJson(sharedPreferences.getString(Prefs.TAGS, ""), type) ?: listOf()
    }

    private fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }


}