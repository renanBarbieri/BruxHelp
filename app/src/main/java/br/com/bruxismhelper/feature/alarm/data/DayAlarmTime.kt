package br.com.bruxismhelper.feature.alarm.data

enum class DayAlarmTime(val hour: Int, val minute: Int) {
    FIRST(8,0),
    SECOND(9,20),
    THIRD(10,40),
    FOURTH(11, 30),
    FIFTH(13,30),
    SIXTH(14,40),
    SEVENTH(16,0),
    EIGHTH(17,20),
    NINTH(18,40),
    TENTH(20,0);

    val alarmTimeInMinutes = hour * 60 + minute
}