package com.valevich.diapro.flows.notes.presenter.all

import com.arellomobile.mvp.InjectViewState
import com.valevich.diapro.base.BasePresenter
import com.valevich.diapro.base.ExecutionMode
import com.valevich.diapro.flows.notes.model.all.NotesModel
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.notes.view.all.NotesView
import com.valevich.diapro.rx.SchedulersSet
import javax.inject.Inject

@InjectViewState
class NotesPresenter @Inject constructor(
        private val model: NotesModel,
        schedulersSet: SchedulersSet
) : BasePresenter<NotesView>(schedulersSet) {

    override fun attachView(view: NotesView?) {
        super.attachView(view)
        loadNotes()
    }

    fun loadNotes() {
        execute(
                ExecutionMode.IO_DETACH,
                model.getNotes(),
                { notes ->
                    viewState.hideProgress()
                    viewState.showNotes(notes)
                },
                {
                    viewState.hideProgress()
                    viewState.showError(it.message ?: "")
                },
                {},
                {
                    o -> o.doOnSubscribe { viewState.showProgress() }
                        .doOnDispose { viewState.hideProgress() }
                }
        )
    }

    fun onNoteItemClick(note: Note) {
        viewState.openEditNoteScreen(note)
    }

    fun onCreateNoteButtonClick() {
        viewState.openNewNoteScreen()
    }

    fun onNoteItemLongClick(note: Note, position: Int) {
        model.setSelectedItem(note, position)
    }

    fun removeSelectedItem() {
        execute(
                ExecutionMode.IO_DETACH,
                model.deleteSelectedNote(),
                {
                    notes ->
                    viewState.hideProgress()
                    viewState.showNotes(notes)
                },
                {
                    viewState.showError(it.message ?: "")
                },
                {},
                {
                    c -> c.doOnSubscribe { viewState.showProgress() }
                        .doOnDispose { viewState.hideProgress() }
                }
        )
    }

}