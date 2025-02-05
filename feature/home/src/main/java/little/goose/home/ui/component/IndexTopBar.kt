package little.goose.home.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Stable
data class IndexTopBarState(
    val currentDay: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now(),
    val navigateToToday: (LocalDate) -> Unit
)

@Composable
fun IndexTopBar(
    modifier: Modifier = Modifier,
    state: IndexTopBarState
) {
    // 日期
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = state.currentDay.month.getDisplayName(
                        TextStyle.SHORT, Locale.CHINA
                    ) + state.currentDay.dayOfMonth + "日",
                    style = MaterialTheme.typography.headlineMedium
                )
                Column {
                    Text(
                        text = state.currentDay.year.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp
                    )
                    Text(
                        text = state.currentDay.dayOfWeek.getDisplayName(
                            TextStyle.SHORT, Locale.CHINA
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { state.navigateToToday(state.today) }
            ) {
                Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarToday,
                        contentDescription = "Today"
                    )
                    Text(
                        text = state.today.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    )
}