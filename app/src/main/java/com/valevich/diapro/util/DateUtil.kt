package com.valevich.diapro.util

import java.text.SimpleDateFormat
import java.util.*

private const val FORMAT = "yyyy-MM-dd HH:mm"

fun toDateFrom(dateString: String): Date =
    try {
        SimpleDateFormat(FORMAT, Locale.getDefault()).parse(dateString)
    } catch (e : Exception) {
        Date()
    }

fun toStringFrom(date: Date) : String =
        try {
            SimpleDateFormat(FORMAT, Locale.getDefault()).format(date)
        } catch (e : Exception) {
            ""
        }

fun toMillisFrom(dateString: String): Long = toDateFrom(dateString).time
