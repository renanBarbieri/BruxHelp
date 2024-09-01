package br.com.bruxismhelper.feature.alarm.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Ignore
import org.junit.Test
import java.util.Calendar

class DayAlarmTimeHelperTest {

    private val dayAlarmTimeHelper = DayAlarmTimeHelper()

    @Test
    fun getDayAlarmNextTime_noCurrentAlarm_returnsFirstAlarm() {
        val mockCalendar = Calendar.getInstance().apply {set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }

        val nextAlarm = dayAlarmTimeHelper.getDayAlarmNextTime(null, mockCalendar)

        assertEquals(DayAlarmTime.FIRST, nextAlarm)
    }

    @Ignore("Not implemented yet")
    @Test
    fun getDayAlarmNextTime_withCurrentAlarm_returnsNextAlarm() {
        val currentAlarm = DayAlarmTime.FIRST // Replace with an actual value
        val mockCalendar = Calendar.getInstance() // Time doesn't matter in this case

        val nextAlarm= dayAlarmTimeHelper.getDayAlarmNextTime(currentAlarm, mockCalendar)

        // Assert that nextAlarm is the expected value after currentAlarm in the DayAlarmTime enum

        TODO("Not implemented")
    }

    @Test
    fun getDayAlarmNextTime_lastAlarm_returnsFirstAlarm() {
        val lastAlarm = DayAlarmTime.TENTH
        val mockCalendar = Calendar.getInstance() // Time doesn't matter in this case

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

    @Ignore("Not implemented yet")
    @Test
    fun timeInMillisAfterNow_calculatesCorrectTime() {
        val alarmTime = DayAlarmTime.FIRST // Replace with an actual value
        val mockCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
        }

        val timeInMillis = dayAlarmTimeHelper.timeInMillisAfterNow(alarmTime, mockCalendar)

        // Calculate the expected time in milliseconds based on alarmTime and mockCalendar
        // and assert that timeInMillis matches the expected value
        TODO("Not implemented")
    }

    @Ignore("Not implemented yet")
    @Test
    fun timeInMillisAfterNow_alarmInNextDay_calculatesCorrectTime() {
        val alarmTime = DayAlarmTime.FIRST // Replace with an actual value
        val mockCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
        }
        // Assuming alarmTime is earlier than 23:59

        val timeInMillis = dayAlarmTimeHelper.timeInMillisAfterNow(alarmTime, mockCalendar)

        // Calculate the expected time in milliseconds (including the time until the next day)
        // and assert that timeInMillis matches the expected value
        TODO("Not implemented")
    }

    @Test
    fun getDayAlarmTimeByOrdinal_returnsCorrectAlarm() {
        val ordinal = 1 // Replace with a valid ordinal within your DayAlarmTime enum

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