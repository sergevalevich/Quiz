package com.valevich.diapro.flows.tags.model

import com.valevich.diapro.api.notes.NotesRepository
import com.valevich.diapro.flows.tags.model.dto.Tag
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class DefaultTagsModel @Inject constructor(
        private val notesRepository: NotesRepository,
        private val selectedTags: List<Tag>
) : TagsModel {

    private var selectedTag: Tag? = null
    private var selectedTagPos: Int = -1

    override fun getTags(): Observable<List<Pair<Tag, Boolean>>> =
            notesRepository.loadTags()
                    .map { it.map { Pair(it, selectedTags.contains(it)) } }

    override fun update(tag: Tag): Observable<List<Pair<Tag, Boolean>>> = createTagWithId(tag).andThen(getTags())

    override fun setSelectedItem(tag: Tag?, pos: Int) {
        selectedTag = tag
        selectedTagPos = pos
    }

    override fun deleteSelectedTag() : Observable<List<Pair<Tag, Boolean>>> {
        selectedTag?.let {
            return notesRepository.delete(it).andThen(getTags())
        }
        return getTags()
    }

    private fun createTagWithId(tag: Tag) : Completable =
            if (tag.id < 0) {
                notesRepository.update(tag.copy(id = UUID.randomUUID().mostSignificantBits))
            } else {
                notesRepository.update(tag)
            }


}