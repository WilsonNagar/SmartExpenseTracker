package com.wilson.smartexpensetracker.presentation.screens.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    onBack: () -> Unit,
    viewModel: ExpenseReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Expense Report") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Text("Last 7 Days", style = MaterialTheme.typography.titleMedium)
                ReportChart(state)

                Text("Category Totals", style = MaterialTheme.typography.titleMedium)
                state.categoryTotals.forEach { (category, total) ->
                    Text("$category: ₹$total")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.exportReport("csv") }) {
                        Text("Export CSV")
                    }
                    Button(onClick = { viewModel.exportReport("pdf") }) {
                        Text("Export PDF")
                    }
                }

                state.exportSuccess?.let { success ->
                    val message = if (success) "Export successful" else "Export failed"
                    LaunchedEffect(success) {
                        snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportChart(state: ExpenseReportState) {
    val barWidth = 40f
    val maxAmount = (state.dailyTotals.values.maxOrNull() ?: 0.0).toFloat()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFF8F8F8), RoundedCornerShape(8.dp))
    ) {
        val space = (size.width - (state.dailyTotals.size * barWidth)) / (state.dailyTotals.size + 1)
        var x = space

        state.dailyTotals.forEach { (_, amount) ->
            val barHeight = if (maxAmount > 0) (amount.toFloat() / maxAmount) * size.height else 0f
            drawRoundRect(
                color = Color(0xFF6200EE),
                topLeft = androidx.compose.ui.geometry.Offset(x, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            x += barWidth + space
        }
    }
}
