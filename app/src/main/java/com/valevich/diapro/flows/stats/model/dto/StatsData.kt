package com.valevich.diapro.flows.stats.model.dto

import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.tags.model.dto.Tag

data class StatsData(
        val notes : List<Note>,
        val tags : List<Tag>
)