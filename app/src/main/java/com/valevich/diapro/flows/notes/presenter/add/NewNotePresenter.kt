package com.valevich.diapro.flows.notes.presenter.add

import com.arellomobile.mvp.InjectViewState
import com.valevich.diapro.base.BasePresenter
import com.valevich.diapro.base.ExecutionMode
import com.valevich.diapro.flows.notes.model.add.NewNoteModel
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.notes.view.add.NewNoteView
import com.valevich.diapro.rx.SchedulersSet
import javax.inject.Inject

@InjectViewState
class NewNotePresenter @Inject constructor(
        private val model: NewNoteModel,
        schedulersSet: SchedulersSet
) : BasePresenter<NewNoteView>(schedulersSet) {

    fun onSaveClicked(note: Note) {
        execute(
                ExecutionMode.COMPUTATION_DETACH,
                model.createNote(note),
                {
                    viewState.close()
                },
                {
                    viewState.showError(it.message ?: "")
                },
                {
                    c  -> c.doOnDispose { viewState.close() }
                }
        )
    }

}