package com.valevich.diapro.base.dependencies

import com.valevich.diapro.api.notes.NotesApiModule
import com.valevich.diapro.api.prod.ApiModule
import com.valevich.diapro.flows.notes.builder.NewNoteComponent
import com.valevich.diapro.flows.notes.builder.NotesComponent
import com.valevich.diapro.flows.stats.builder.StatsComponent
import com.valevich.diapro.flows.tags.builder.TagsComponent
import com.valevich.diapro.rx.ShedulersModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ApiModule::class,
    NotesApiModule::class,
    ShedulersModule::class
])
interface ApplicationComponent {

    fun notesComponentBuilder() : NotesComponent.Builder

    fun newNoteComponentBuilder() : NewNoteComponent.Builder

    fun statsComponentBuilder() : StatsComponent.Builder

    fun tagsComponentBuilder() : TagsComponent.Builder

}

