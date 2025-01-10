package com.tb.nextstop.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val WPT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val WPT_TIME_FORMAT_NO_SECS = "yyyy-MM-dd'T'HH:mm"
const val WPT_LIVE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
const val HRS_MINS_TIME_FORMAT = "hh:mm a"

fun getCurrentTime(): Date = Date()

fun getHrsMinsFromWPTLiveFormat(time: String): String =
    convertTimeFormats(time, WPT_LIVE_TIME_FORMAT, HRS_MINS_TIME_FORMAT)

fun getHrsMinsFromWPTFormat(time: String): String =
    convertTimeFormats(time, WPT_TIME_FORMAT, HRS_MINS_TIME_FORMAT)

fun convertTimeFormats(time: String, inputFormat: String, outputFormat: String): String {
    val inputFormatter = SimpleDateFormat(inputFormat, Locale.US)
    val outputFormatter = SimpleDateFormat(outputFormat, Locale.US)

    val inputDate = inputFormatter.parse(time)
    return outputFormatter.format(inputDate ?: Date())
}

fun isTimeInThePast(time: String): Boolean {
    val currentTime = getCurrentTime()
    val formatter = SimpleDateFormat(WPT_LIVE_TIME_FORMAT, Locale.US)
    val inputTime = formatter.parse(time)
    return inputTime?.before(currentTime) ?: false
}

fun getTimingFromWPTFormat(expectedTime: String, scheduledTime: String): Timing {
    val formatter = SimpleDateFormat(WPT_TIME_FORMAT_NO_SECS, Locale.US)
    val expected = formatter.parse(expectedTime)
    val scheduled = formatter.parse(scheduledTime)
    return if (expected?.before(scheduled) == true) {
        Timing.EARLY
    } else if (expected?.after(scheduled) == true) {
        Timing.LATE
    } else {
        Timing.ON_TIME
    }
}

enum class Timing {
    EARLY,
    ON_TIME,
    LATE
}