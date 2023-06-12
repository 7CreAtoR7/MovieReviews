package ru.ilya.moviereviews.util

import java.text.SimpleDateFormat
import java.util.*

fun String.convertDateFormat(): String {
    val originalDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val targetDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    val date = originalDateFormat.parse(this)
    return if (date != null)
        targetDateFormat.format(date)
    else
        this

}