package com.valevich.diapro.flows.tags.model

import com.valevich.diapro.flows.tags.model.dto.Tag
import io.reactivex.Completable
import io.reactivex.Observable

interface TagsModel {

    fun getTags(): Observable<List<Pair<Tag, Boolean>>>

    fun update(tag: Tag): Observable<List<Pair<Tag, Boolean>>>

    fun setSelectedItem(tag: Tag?, pos: Int)

    fun deleteSelectedTag() : Observable<List<Pair<Tag, Boolean>>>
}