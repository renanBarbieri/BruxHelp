package br.com.bruxismhelper.feature.alarm.data

enum class DayAlarmTime(val timeMillis: Long) {
    FIRST(28800000),// 08:00
    SECOND(33600000),// 09:20
    THIRD(38400000),// 10:40
    FOURTH(43200000),// 12:00
    FIFTH(48000000),// 13:20
    SIXTH(52800000),// 14:40
    SEVENTH(57600000),// 16:00
    EIGHTH(62400000),// 17:20
    NINTH(67200000),// 18:40
    TENTH(72000000),// 20:00
}