package br.com.bruxismhelper.feature.alarm

import java.util.Calendar

 internal fun getCalendarWithProperties(
    day: Int? = null,
    month: Int? = null,
    year: Int? = null,
    hour: Int? = null,
    minute: Int? = null
) =
    Calendar.getInstance().apply {
        day?.let { set(Calendar.DAY_OF_MONTH, it) }
        month?.let { set(Calendar.MONTH, it) }
        year?.let { set(Calendar.YEAR, it) }
        hour?.let { set(Calendar.HOUR_OF_DAY, it) }
        minute?.let { set(Calendar.MINUTE, it) }
    }