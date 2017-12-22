package com.valevich.diapro.flows.tags.builder

import com.valevich.diapro.flows.tags.model.DefaultTagsModel
import com.valevich.diapro.flows.tags.model.TagsModel
import com.valevich.diapro.flows.tags.model.dto.Tag
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [TagsModule.Declarations::class])
class TagsModule(private val selectedTags: List<Tag>) {

    @Provides
    internal fun provideSelectedTags() : List<Tag> = selectedTags

    @Module
    interface Declarations {
        @Binds
        fun provideTagsModel(defaultTagsModel: DefaultTagsModel) : TagsModel
    }


}