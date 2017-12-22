package com.valevich.diapro.flows.notes.model.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.valevich.diapro.R
import com.valevich.diapro.common.DEFAULT_VALUE
import com.valevich.diapro.common.TEMP_DEFAULT
import com.valevich.diapro.flows.tags.model.dto.Tag
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Note @JvmOverloads constructor(
        @SerializedName("id") val id: Long = -1,//
        @SerializedName("sugarLevel") val sugarLevel: Double = DEFAULT_VALUE,//
        @SerializedName("date") val date: String = "",//
        @SerializedName("shortInsulin") val shortInsulin: Double = DEFAULT_VALUE,//
        @SerializedName("longInsulin") val longInsulin: Double = DEFAULT_VALUE,//
        @SerializedName("xe") val xe: Double = DEFAULT_VALUE,//
        @SerializedName("bodyTemperature") val bodyTemperature: Double = TEMP_DEFAULT,//
        @SerializedName("mood") val mood: Mood = Mood.OK,//
        @SerializedName("tags") val tags: List<Tag> = listOf()//
) : Parcelable

enum class Mood(val iconId: Int) {
    SAD(R.drawable.emoticon_sad),
    OK(R.drawable.emoticon_neutral),
    HAPPY(R.drawable.emoticon_happy);
}


