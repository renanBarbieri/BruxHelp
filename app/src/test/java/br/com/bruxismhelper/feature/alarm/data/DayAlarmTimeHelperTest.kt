package br.com.bruxismhelper.feature.alarm.data

import br.com.bruxismhelper.feature.alarm.getCalendarWithProperties
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.Calendar

class DayAlarmTimeHelperTest {

    private val dayAlarmTimeHelper = DayAlarmTimeHelper()

    @Test
    fun getDayAlarmNextTime_noCurrentAlarm_returnsFirstAlarm() {
        val mockCalendar = getCalendarWithProperties(hour = 0, minute = 0)

        val nextAlarm = dayAlarmTimeHelper.getDayAlarmNextTime(null, mockCalendar)

        assertEquals(DayAlarmTime.FIRST, nextAlarm)
    }

    @Test
    fun getDayAlarmNextTime_withCurrentAlarm_returnsNextAlarm() {
        val currentAlarm = DayAlarmTime.FIRST
        val mockCalendar = getCalendarWithProperties() // Time doesn't matter in this case

        val nextAlarm = dayAlarmTimeHelper.getDayAlarmNextTime(currentAlarm, mockCalendar)

        assertEquals(DayAlarmTime.SECOND, nextAlarm)
    }

    @Test
    fun getDayAlarmNextTime_lastAlarm_returnsFirstAlarm() {
        val lastAlarm = DayAlarmTime.TENTH
        val mockCalendar = getCalendarWithProperties() // Time doesn't matter in this case

        val nextAlarm = dayAlarmTimeHelper.getDayAlarmNextTime(lastAlarm, mockCalendar)

        assertEquals(DayAlarmTime.FIRST, nextAlarm)
    }

    @Test
    fun getDayAlarmNextTime_currentTimeBeforeAnyAlarm_returnsFirstAlarm() {
        val mockCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }

        val nextAlarm = dayAlarmTimeHelper.getDayAlarmNextTime(null, mockCalendar)

        assertEquals(DayAlarmTime.FIRST, nextAlarm)
    }

    @Test
    fun getDayAlarmNextTime_currentTimeAfterAllAlarms_returnsFirstAlarmForNextDay() {
        val mockCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
        }
        // Assuming all your alarms are before 23:59

        val nextAlarm = dayAlarmTimeHelper.getDayAlarmNextTime(null, mockCalendar)

        assertEquals(DayAlarmTime.FIRST, nextAlarm)
    }

    @Test
    fun timeInMillisAfterNow_alarmInNextDay_calculatesCorrectTime() {
        val alarmTime = DayAlarmTime.FIRST //Next alarm should be the first
        val mockCalendar = getCalendarWithProperties(day = 4, month = 9, year = 2024, hour = 23, minute = 0) //this date if after time of First alarm

        val timeInMillis = dayAlarmTimeHelper.calendarAfterNow(alarmTime, mockCalendar).timeInMillis

        val expectedTimeInMillis = getCalendarWithProperties(day = 5, month = 9, year = 2024, hour = 8, minute = 0).timeInMillis

        assertEquals(expectedTimeInMillis, timeInMillis)
    }

    @Test
    fun timeInMillisAfterNow_calculatesCorrectTime() {
        val alarmTime = DayAlarmTime.FIRST
        val mockCalendar = getCalendarWithProperties(hour = 7, minute = 0)

        val timeInMillis = dayAlarmTimeHelper.calendarAfterNow(alarmTime, mockCalendar).timeInMillis
        val expectedTimeInMillis = getCalendarWithProperties(hour = 8, minute = 0).timeInMillis

        assertEquals(expectedTimeInMillis, timeInMillis)
    }

    @Test
    fun getDayAlarmTimeByOrdinal_returnsCorrectAlarm() {
        val ordinal = 1
        val alarmTime = dayAlarmTimeHelper.getDayAlarmTimeByOrdinal(ordinal)

        assertNotNull(alarmTime)
        assertEquals(ordinal, alarmTime.ordinal)
    }

    @Test(expected = NoSuchElementException::class)
    fun getDayAlarmTimeByOrdinal_invalidOrdinal_throwsException() {
        val invalidOrdinal = -1 // Or any ordinal outside the range of your DayAlarmTime enum
        dayAlarmTimeHelper.getDayAlarmTimeByOrdinal(invalidOrdinal)
    }
}