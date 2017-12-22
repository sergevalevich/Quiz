package com.valevich.diapro.flows.tags.builder

import com.valevich.diapro.base.dependencies.ScreenScope
import com.valevich.diapro.flows.tags.presenter.TagsPresenter
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [(TagsModule::class)])
interface TagsComponent {

    @Subcomponent.Builder
    interface Builder {
        fun tagsModule(tagsModule: TagsModule) : Builder
        fun build() : TagsComponent
    }

    fun tagsPresenter() : TagsPresenter


}