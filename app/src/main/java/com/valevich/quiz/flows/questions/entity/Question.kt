package com.valevich.quiz.flows.questions.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Question @JvmOverloads constructor(
        val categoryId: Int = -2,
        val title: String = "",
        val codeSnippet: String? = null,
        val answers: List<String> = listOf(),
        val correctAnswerPosition: Int = -1,
        val hasAnsweredCorrect: Boolean = false
) : Parcelable