package com.valevich.diapro.flows.stats.model

import com.valevich.diapro.api.notes.NotesRepository
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.stats.model.dto.StatsData
import com.valevich.diapro.flows.tags.model.dto.Tag
import io.reactivex.Observable
import javax.inject.Inject

class DefaultStatsModel @Inject constructor(
        private val notesRepository: NotesRepository
) : StatsModel {

    override fun getStatsData(tag: Tag?): Observable<StatsData> = getNotes(tag)
            .flatMap { notes -> getTags().map { tags -> StatsData(notes, tags) } }


    private fun getNotes(tag: Tag?): Observable<List<Note>> =
            if (tag == null) notesRepository.loadNotes()
            else notesRepository.loadNotes().map { it.filter { it.tags.contains(tag) } }


    private fun getTags(): Observable<List<Tag>> = notesRepository.loadTags()
}