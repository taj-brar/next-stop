package com.tb.nextstop.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val WPT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val WPT_LIVE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
const val HRS_MINS_TIME_FORMAT = "hh:mm a"

fun getCurrentTime(): Date = Date()

fun getHrsMinsFromWPTFormat(time: String): String {
    val inputFormatter = SimpleDateFormat(WPT_LIVE_TIME_FORMAT, Locale.US)
    val outputFormatter = SimpleDateFormat(HRS_MINS_TIME_FORMAT, Locale.US)

    val inputDate = inputFormatter.parse(time)
    return outputFormatter.format(inputDate ?: Date())
}

fun isTimeInThePast(time: String): Boolean {
    val currentTime = getCurrentTime()
    val formatter = SimpleDateFormat(WPT_LIVE_TIME_FORMAT, Locale.US)
    val inputTime = formatter.parse(time)
    return inputTime?.before(currentTime) ?: false
}