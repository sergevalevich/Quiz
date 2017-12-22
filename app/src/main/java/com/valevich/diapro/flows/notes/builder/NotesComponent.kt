package com.valevich.diapro.flows.notes.builder

import com.valevich.diapro.base.dependencies.ScreenScope
import com.valevich.diapro.flows.notes.presenter.all.NotesPresenter
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [(NotesModule::class)])
interface NotesComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : NotesComponent
    }

    fun notesPresenter() : NotesPresenter

}
