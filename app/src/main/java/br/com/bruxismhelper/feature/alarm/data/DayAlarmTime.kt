package br.com.bruxismhelper.feature.alarm.data

enum class DayAlarmTime(val timeMillis: Int) {
    FIRST(28800000),// 08:00
    SECOND(33600000),// 09:20
    THIRD(38400000),// 10:40
    FOURTH(41400000),// 11:30
    FIFTH(48600000),// 13:30
    SIXTH(52800000),// 14:40
    SEVENTH(57600000),// 16:00
    EIGHTH(62400000),// 17:20
    NINTH(67200000),// 18:40
    TENTH(72000000),// 20:00
}