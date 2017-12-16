package com.valevich.quiz.flows.categories.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import com.valevich.quiz.flows.questions.entity.Question
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class QuizCategory @JvmOverloads constructor(
        val id: Int = -1,
        val name : String = "",
        val imageUrl : String = "",
        val questions: List<Question> = listOf(),
        val answered: Int = 0
) : Parcelable