package com.valevich.diapro.flows.notes.builder

import com.valevich.diapro.base.dependencies.ScreenScope
import com.valevich.diapro.flows.notes.presenter.add.NewNotePresenter
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [(NotesModule::class)])
interface NewNoteComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : NewNoteComponent
    }

    fun newNotePresenter() : NewNotePresenter

}
