package com.tb.nextstop.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val WPT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val WPT_TIME_FORMAT_NO_SECS = "yyyy-MM-dd'T'HH:mm"
const val WPT_LIVE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
const val HRS_MINS_TIME_FORMAT = "hh:mm a"
const val CACHE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val CACHE_EXPIRY_DELAY_HRS = 72

fun getCurrentTimeAsDate(): Date = Date()

fun getCacheExpiryTime(): String {
    val formatter = SimpleDateFormat(CACHE_TIME_FORMAT, Locale.US)
    val current = getCurrentTimeAsDate()
    val calendar = Calendar.getInstance()
    calendar.time = current
    calendar.add(Calendar.HOUR_OF_DAY, CACHE_EXPIRY_DELAY_HRS)
    return formatter.format(calendar.time)
}

fun isCacheExpired(time: String): Boolean {
    val current = getCurrentTimeAsDate()
    val formatter = SimpleDateFormat(CACHE_TIME_FORMAT, Locale.US)
    val inputDate = formatter.parse(time)
    return inputDate?.before(current) == true
}

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

fun getBusTimingFromWPTFormat(expectedTime: String, scheduledTime: String): BusTiming {
    val formatter = SimpleDateFormat(WPT_TIME_FORMAT_NO_SECS, Locale.US)
    val expected = formatter.parse(expectedTime)
    val scheduled = formatter.parse(scheduledTime)
    return if (expected?.before(scheduled) == true) {
        BusTiming.EARLY
    } else if (expected?.after(scheduled) == true) {
        BusTiming.LATE
    } else {
        BusTiming.ON_TIME
    }
}

fun getStopTimingFromWPTLiveFormat(expectedTime: String): StopTiming {
    val formatter = SimpleDateFormat(WPT_LIVE_TIME_FORMAT, Locale.US)
    val expected = formatter.parse(expectedTime)
    val current = getCurrentTimeAsDate()
    return if (expected?.before(current) == true) {
        StopTiming.PAST
    } else if (expected?.after(current) == true) {
        StopTiming.FUTURE
    } else {
        StopTiming.CURRENT
    }
}

enum class BusTiming {
    EARLY,
    ON_TIME,
    LATE
}

enum class StopTiming {
    PAST,
    CURRENT,
    FUTURE
}