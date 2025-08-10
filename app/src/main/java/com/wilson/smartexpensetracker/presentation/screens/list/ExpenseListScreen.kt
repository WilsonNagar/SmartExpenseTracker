package com.wilson.smartexpensetracker.presentation.screens.list

//import com.wilson.smartexpensetracker.presentation.components.ThemeToggleSwitch
import ExpenseListItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.presentation.components.ThemeToggleSwitch
import com.wilson.smartexpensetracker.presentation.theme.LocalThemeState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class GroupingMode {
    CATEGORY, TIME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    state: ExpenseListState,
    onAddExpenseClick: () -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    onReload: (Long) -> Unit,
    onViewReportClick: () -> Unit,
) {
    val context = LocalContext.current
    var groupingMode by remember { mutableStateOf(GroupingMode.TIME) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Filter expenses by selectedDate
    val filteredExpenses = remember(state.expenses, selectedDate) {
        state.expenses
        // Start of the selected date
//        val selectedDateStart = (selectedDate.clone() as Calendar).apply {
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }
//
//        // Start of the next day
//        val selectedDateEnd = (selectedDateStart.clone() as Calendar).apply {
//            add(Calendar.DAY_OF_MONTH, 1)
//        }
//
//        state.expenses.filter {
//            val ts = it.timestamp
//            ts in selectedDateStart.timeInMillis until selectedDateEnd.timeInMillis
//        }
    }

    val totalAmount = filteredExpenses.sumOf { it.amount }
    val totalCount = filteredExpenses.size
    val themeState = LocalThemeState.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                actions = {
                    ThemeToggleSwitch(
                        isDarkTheme = themeState.isDarkTheme,
                        onToggle = themeState.toggleTheme
                    )
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                    IconButton(onClick = {
                        groupingMode =
                            if (groupingMode == GroupingMode.TIME) GroupingMode.CATEGORY else GroupingMode.TIME
                    }) {
                        Icon(
                            imageVector = if (groupingMode == GroupingMode.TIME) Icons.AutoMirrored.Filled.List else Icons.Default.ViewAgenda,
                            contentDescription = "Toggle Grouping"
                        )
                    }
                    IconButton(onClick = onViewReportClick) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Toggle Grouping"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpenseClick) {
                Text("+")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Totals row
                Text(
                    text = "Date: ${
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                            selectedDate.time
                        )
                    }",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Total Spent: ₹${"%.2f".format(totalAmount)}")
                Text("Total Expenses: $totalCount")

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredExpenses.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No expenses found for this date.")
                    }
                } else {
                    // Expense list grouped accordingly
                    when (groupingMode) {
                        GroupingMode.TIME -> ExpenseListByTime(
                            expenses = filteredExpenses,
                            onDeleteExpense = onDeleteExpense
                        )

                        GroupingMode.CATEGORY -> ExpenseListByCategory(
                            expenses = filteredExpenses,
                            onDeleteExpense = onDeleteExpense
                        )
                    }
                }
            }

            if (showDatePicker) {
                DatePickerDialog(
                    initialDate = selectedDate,
                    onDismissRequest = { showDatePicker = false },
                    onDateSelected = {
                        selectedDate = it
                        showDatePicker = false
                        onReload(it.timeInMillis)
                    }
                )
            }
        }
    )
}

@Composable
fun ExpenseListByTime(expenses: List<Expense>, onDeleteExpense: (Expense) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(expenses) { index, expense ->
            ExpenseListItem(
                expense = expense, onDelete = { onDeleteExpense(expense) },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseListByCategory(expenses: List<Expense>, onDeleteExpense: (Expense) -> Unit) {
    val grouped = expenses.groupBy { it.category }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        grouped.forEach { (category, list) ->
            stickyHeader {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            itemsIndexed(list) { _, expense ->
                ExpenseListItem(
                    expense = expense, onDelete = { onDeleteExpense(expense) },
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDate: Calendar,
    onDismissRequest: () -> Unit,
    onDateSelected: (Calendar) -> Unit
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = initialDate.timeInMillis)

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    state.selectedDateMillis?.let {
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = it
                        onDateSelected(cal)
                    }
                    onDismissRequest()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}