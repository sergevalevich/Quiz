package com.valevich.diapro.flows.notes.view.add

import com.valevich.diapro.base.view.BaseView

interface NewNoteView : BaseView {

    fun disableSaveButton()

    fun enableSaveButton()

    fun close()

}