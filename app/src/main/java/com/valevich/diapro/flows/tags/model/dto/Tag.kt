package com.valevich.diapro.flows.tags.model.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.plumillonforge.android.chipview.Chip
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Tag @JvmOverloads constructor(
        @SerializedName("id") val id: Long = -1L,
        @SerializedName("name") val name: String = ""
) : Chip , Parcelable{
    override fun getText(): String = name

    override fun toString(): String = name
}