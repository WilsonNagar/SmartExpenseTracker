package com.wilson.smartexpensetracker.presentation.screens.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wilson.smartexpensetracker.domain.model.Expense
import com.wilson.smartexpensetracker.presentation.theme.LocalThemeState
import java.text.SimpleDateFormat
import java.util.*

enum class GroupingMode {
    CATEGORY, TIME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    state: ExpenseListState,
    onAddExpenseClick: () -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    onReload: () -> Unit,
    onViewReportClick: () -> Unit,
) {
    val context = LocalContext.current
    var groupingMode by remember { mutableStateOf(GroupingMode.TIME) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Filter expenses by selectedDate
    val filteredExpenses = remember(state.expenses, selectedDate) {
        val selectedDateStart = selectedDate.clone() as Calendar
        selectedDateStart.set(Calendar.HOUR_OF_DAY, 0)
        selectedDateStart.set(Calendar.MINUTE, 0)
        selectedDateStart.set(Calendar.SECOND, 0)
        selectedDateStart.set(Calendar.MILLISECOND, 0)

        val selectedDateEnd = selectedDate.clone() as Calendar
        selectedDateEnd.add(Calendar.DAY_OF_MONTH, 1)

        state.expenses.filter {
            val ts = it.timestamp
            ts in selectedDateStart.timeInMillis until selectedDateEnd.timeInMillis
        }
    }

    val totalAmount = filteredExpenses.sumOf { it.amount }
    val totalCount = filteredExpenses.size
    val themeState = LocalThemeState.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                actions = {
                    IconButton(onClick = themeState.toggleTheme) {
                        Icon(
                            imageVector = if (themeState.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                    IconButton(onClick = {
                        groupingMode = if (groupingMode == GroupingMode.TIME) GroupingMode.CATEGORY else GroupingMode.TIME
                    }) {
                        Icon(
                            imageVector = if (groupingMode == GroupingMode.TIME) Icons.AutoMirrored.Filled.List else Icons.Default.ViewAgenda,
                            contentDescription = "Toggle Grouping"
                        )
                    }
                    IconButton(onClick = onViewReportClick) {
                        Icon(
                            imageVector =  Icons.Default.BarChart,
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
                    text = "Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate.time)}",
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
                        onReload()
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
            ExpenseListItem(expense = expense, onDelete = { onDeleteExpense(expense) })
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
                ExpenseListItem(expense = expense, onDelete = { onDeleteExpense(expense) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListItem(expense: Expense, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* maybe open details later */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = expense.title, style = MaterialTheme.typography.titleMedium)
                Text(text = expense.category, style = MaterialTheme.typography.bodySmall)
                if (!expense.notes.isNullOrBlank()) {
                    Text(
                        text = expense.notes,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
            }
            Text(text = "₹${"%.2f".format(expense.amount)}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Expense"
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

    if (true) { // your condition to show dialog
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
}