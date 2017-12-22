package com.valevich.diapro.flows.tags.view

import com.valevich.diapro.base.view.BaseView
import com.valevich.diapro.flows.tags.model.dto.Tag

interface TagsView : BaseView {

    fun showAllTags(tags: List<Pair<Tag,Boolean>>)
    fun removeListItem(position: Int)
}
