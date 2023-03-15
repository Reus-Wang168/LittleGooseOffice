package little.goose.home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import little.goose.design.system.theme.RoundedCorner16
import little.goose.home.data.CalendarModel
import little.goose.home.ui.component.IndexMemorialCard
import little.goose.home.ui.component.IndexScheduleCard
import little.goose.home.ui.component.IndexTransactionCard
import little.goose.schedule.data.entities.Schedule
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Stable
data class IndexScreenState(
    val today: LocalDate,
    val currentDay: LocalDate,
    val currentCalendarModel: State<CalendarModel>,
    val updateMonth: (YearMonth, YearMonth) -> Unit,
    val updateCurrentDay: (LocalDate) -> Unit,
    val checkSchedule: (Schedule, Boolean) -> Unit,
    val getCalendarModelState: (LocalDate) -> State<CalendarModel>
)

@Composable
fun IndexScreen(
    modifier: Modifier = Modifier,
    state: IndexScreenState
) {
    val initMonth = remember { YearMonth.now() }
    val startMonth = remember { initMonth.minusMonths(120) }
    val endMonth = remember { initMonth.plusMonths(120) }
    val dayOfWeek = remember { daysOfWeek() }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = initMonth,
        firstDayOfWeek = dayOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfRow
    )

    var visibleMonth by remember(calendarState) { mutableStateOf(calendarState.firstVisibleMonth) }
    LaunchedEffect(calendarState) {
        launch(Dispatchers.Default) {
            snapshotFlow { calendarState.isScrollInProgress }
                .filter { scrolling -> !scrolling }
                .collect { visibleMonth = calendarState.firstVisibleMonth }
        }
        launch(Dispatchers.Default) {
            snapshotFlow { calendarState.firstVisibleMonth }.collect {
                state.updateMonth(
                    calendarState.firstVisibleMonth.yearMonth,
                    calendarState.lastVisibleMonth.yearMonth
                )
            }
        }
    }
    val contentHeight = remember { 58.dp }

    Column(modifier = modifier) {

        // 星期
        Row(modifier = Modifier.fillMaxWidth()) {
            for (day in dayOfWeek) {
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                }
            }
        }

        // 日历
        Box(
            modifier = Modifier.animateContentSize()
        ) {
            HorizontalCalendar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(contentHeight * visibleMonth.weekDays.size),
                state = calendarState,
                userScrollEnabled = true,
                dayContent = { day ->
                    val model by state.getCalendarModelState(day.date)
                    MonthDay(
                        modifier = Modifier.height(contentHeight),
                        day = day,
                        isCurrentDay = day.date == state.currentDay,
                        isToday = day.date.isEqual(state.today),
                        model = model,
                        onClick = state.updateCurrentDay
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            if (state.currentCalendarModel.value.memorials.isNotEmpty()) {
                IndexMemorialCard(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    memorial = state.currentCalendarModel.value.memorials.first()
                )
            }
            IndexTransactionCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .heightIn(0.dp, 160.dp),
                transactions = state.currentCalendarModel.value.transactions,
                income = state.currentCalendarModel.value.income,
                expense = state.currentCalendarModel.value.expense
            )
            if (state.currentCalendarModel.value.transactions.isNotEmpty()) {
                IndexScheduleCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .fillMaxWidth()
                        .heightIn(0.dp, 120.dp),
                    schedules = state.currentCalendarModel.value.schedules,
                    onCheckChange = state.checkSchedule
                )
            }
        }
    }
}

@Composable
private fun MonthDay(
    modifier: Modifier,
    day: CalendarDay,
    isToday: Boolean,
    isCurrentDay: Boolean,
    model: CalendarModel,
    onClick: (LocalDate) -> Unit
) {
    DayContent(
        modifier = modifier,
        date = day.date,
        isCurrentMonth = day.position == DayPosition.MonthDate,
        isCurrentDay = isCurrentDay,
        isToday = isToday,
        model = model,
        onClick = onClick
    )
}

@Composable
private fun DayContent(
    modifier: Modifier,
    date: LocalDate,
    isCurrentMonth: Boolean,
    isCurrentDay: Boolean,
    isToday: Boolean,
    model: CalendarModel,
    onClick: (LocalDate) -> Unit
) {
    Column(
        modifier = modifier
            .padding(1.dp)
            .fillMaxWidth()
            .clip(RoundedCorner16)
            .run {
                if (isCurrentDay) background(MaterialTheme.colorScheme.primary) else this
            }
            .clickable { onClick(date) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val colorScheme = MaterialTheme.colorScheme
        if (model.containSomething) {
            Canvas(modifier = Modifier.size(14.dp)) {
                drawCircle(color = colorScheme.primaryContainer, radius = size.width / 4)
            }
        } else {
            Spacer(modifier = Modifier.height(14.dp))
        }
        Text(
            text = date.dayOfMonth.toString(),
            modifier = Modifier,
            color = if (isCurrentDay) {
                MaterialTheme.colorScheme.onPrimary
            } else if (isToday) {
                MaterialTheme.colorScheme.tertiary
            } else if (isCurrentMonth) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
        if (model.balance.signum() != 0) {
            Text(
                text = model.balance.toPlainString() ?: "",
                modifier = Modifier.height(14.dp),
                style = MaterialTheme.typography.bodySmall,
                color = if (isCurrentDay) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}