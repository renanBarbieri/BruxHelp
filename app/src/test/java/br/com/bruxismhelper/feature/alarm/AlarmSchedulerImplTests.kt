package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.feature.alarm.data.AlarmType
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTime
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import br.com.bruxismhelper.feature.alarm.repository.AlarmRepositoryImpl
import br.com.bruxismhelper.platform.notification.data.AppChannel
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AlarmSchedulerFacadeImplTest {

    @Mock
    private lateinit var appContext: Context
    @Mock
    private lateinit var dayAlarmTimeHelper: DayAlarmTimeHelper
    @Mock
    private lateinit var alarmSchedulerHelper: AlarmSchedulerHelper

    private lateinit var alarmSchedulerFacade: AlarmSchedulerFacadeImpl

    private lateinit var alarmRepository: AlarmRepositoryImpl


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        `when`(appContext.getString(anyInt())).thenReturn("Unit test string")

        alarmRepository = AlarmRepositoryImpl(mock())

        alarmSchedulerFacade =
            AlarmSchedulerFacadeImpl(appContext, alarmSchedulerHelper, dayAlarmTimeHelper, alarmRepository)
    }

    @Test
    fun scheduleNextAlarm_noCurrentAlarm_schedulesNextAvailableAlarm() = runBlocking {
        val calendarMock = getCalendarWithProperties(1, 9, 2024, 10, 0)

        val expectedNextAlarmTime = DayAlarmTime.THIRD
        val expectedTimeInMillis = getTimeInMillisOf(1, 9, 2024, 10, 40)
        val expectedAlarmItem = expectedAlarmItem(expectedNextAlarmTime, expectedTimeInMillis)

        `when`(dayAlarmTimeHelper.getDayAlarmNextTime(null, calendarMock)).thenReturn(
            expectedNextAlarmTime
        )

        `when`(
            dayAlarmTimeHelper.calendarAfterNow(
                expectedNextAlarmTime,
                calendarMock
            ).timeInMillis
        ).thenReturn(expectedTimeInMillis)


        alarmSchedulerFacade.scheduleNextAlarm(null, calendarMock)
        verify(alarmSchedulerHelper, times(1)).schedule(
            appContext,
            expectedAlarmItem,
            AlarmType.Exact
        )
    }

    @Test
    fun scheduleNextAlarm_withCurrentAlarm_schedulesAlarmAfterCurrent() = runBlocking {
        val calendarMock = getCalendarWithProperties(1, 9, 2024, 10, 41)

        val currentAlarmId = 2
        val currentDayAlarm = DayAlarmTime.THIRD
        val expectedNextAlarmTime = DayAlarmTime.FOURTH
        val expectedTimeInMillis = getTimeInMillisOf(1, 9, 2024, 11, 30)
        val expectedAlarmItem = expectedAlarmItem(expectedNextAlarmTime, expectedTimeInMillis)

        `when`(dayAlarmTimeHelper.getDayAlarmTimeByOrdinal(currentAlarmId)).thenReturn(
            currentDayAlarm
        )

        `when`(dayAlarmTimeHelper.getDayAlarmNextTime(currentDayAlarm, calendarMock)).thenReturn(
            expectedNextAlarmTime
        )

        `when`(dayAlarmTimeHelper.calendarAfterNow(expectedNextAlarmTime,calendarMock).timeInMillis).thenReturn(
            expectedTimeInMillis
        )


        alarmSchedulerFacade.scheduleNextAlarm(currentAlarmId, calendarMock)

        verify(alarmSchedulerHelper, times(1)).schedule(
            appContext,
            expectedAlarmItem,
            AlarmType.Exact
        )
    }

    @Suppress("SameParameterValue")
    private fun getTimeInMillisOf(day: Int, month: Int, year: Int, hour: Int, minute: Int): Long {
        return getCalendarWithProperties(day, month, year, hour, minute).timeInMillis
    }

    private fun expectedAlarmItem(expectedNextAlarmTime: DayAlarmTime, expectedTimeInMillis: Long) = AlarmItem(
        id = expectedNextAlarmTime.ordinal,
        channelProp = NotificationChannelProp(
            AppChannel.BRUXISM,
            NotificationManager.IMPORTANCE_DEFAULT
        ),
        timeInMillis = expectedTimeInMillis,
        title = "Unit test string",
        message = "Unit test string",
    )
}