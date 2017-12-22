package com.valevich.diapro.flows.tags.presenter

import com.arellomobile.mvp.InjectViewState
import com.valevich.diapro.base.BasePresenter
import com.valevich.diapro.base.ExecutionMode
import com.valevich.diapro.flows.tags.model.TagsModel
import com.valevich.diapro.flows.tags.model.dto.Tag
import com.valevich.diapro.flows.tags.view.TagsView
import com.valevich.diapro.rx.SchedulersSet
import javax.inject.Inject

@InjectViewState
class TagsPresenter @Inject constructor(
        private val model: TagsModel,
        schedulersSet: SchedulersSet
) : BasePresenter<TagsView>(schedulersSet) {

    override fun attachView(view: TagsView?) {
        super.attachView(view)
        loadTags()
    }

    fun loadTags() {
        execute(
                ExecutionMode.IO_DETACH,
                model.getTags(),
                { tags ->
                    viewState.showAllTags(tags)
                },
                {
                    viewState.showError(it.message ?: "")
                },
                {}
        )
    }

    fun saveTag(tag: Tag) {
        execute(
                ExecutionMode.IO_DETACH,
                model.update(tag),
                { tags ->
                    viewState.showAllTags(tags)
                },
                {
                    viewState.showError(it.message ?: "")
                }
        )
    }

    fun onNoteItemLongClick(tag: Tag, pos: Int) {
        model.setSelectedItem(tag, pos)
    }

    fun removeSelectedItem() {
        execute(
                ExecutionMode.COMPUTATION_DETACH,
                model.deleteSelectedTag(),
                { tags ->
                    viewState.showAllTags(tags)
                },
                {
                    viewState.showError(it.message ?: "")
                },
                {}
        )
    }

}
