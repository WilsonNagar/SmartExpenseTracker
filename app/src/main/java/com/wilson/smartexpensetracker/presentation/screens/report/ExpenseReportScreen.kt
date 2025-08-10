package com.wilson.smartexpensetracker.presentation.screens.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wilson.smartexpensetracker.presentation.components.CategoryPieChart

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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // 7-day chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Last 7 Days", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        ReportBarChart(state)
                    }
                }

                // Category Totals
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Category Totals", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        CategoryPieChart(state.categoryTotals)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Export buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.exportReport("csv") },
                        modifier = Modifier.weight(1f)
                    ) { Text("Export CSV") }

                    Button(
                        onClick = { viewModel.exportReport("pdf") },
                        modifier = Modifier.weight(1f)
                    ) { Text("Export PDF") }
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
private fun ReportBarChart(state: ExpenseReportState) {
    val barWidth = 40f
    val maxAmount = (state.dailyTotals.values.maxOrNull() ?: 0.0).toFloat()
    val days = state.dailyTotals.keys.toList()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color(0xFFF8F8F8), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        val space = (size.width - (state.dailyTotals.size * barWidth)) / (state.dailyTotals.size + 1)
        var x = space

        state.dailyTotals.toList().forEachIndexed { index, (_, amount) ->
            val barHeight = if (maxAmount > 0) (amount.toFloat() / maxAmount) * (size.height - 40f) else 0f

            // Draw bar
            drawRoundRect(
                color = Color(0xFF6200EE),
                topLeft = androidx.compose.ui.geometry.Offset(x, size.height - barHeight - 20f),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            // Draw day label
            drawContext.canvas.nativeCanvas.drawText(
                days[index],
                x + barWidth / 2,
                size.height - 5f,
                android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 24f
                    color = android.graphics.Color.DKGRAY
                }
            )

            // Draw amount label

            drawContext.canvas.nativeCanvas.drawText(
                "₹${amount.toInt()}",
                x + barWidth / 2,
                size.height - barHeight - 30f,
                android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 24f
                    color = android.graphics.Color.BLACK
                }
            )

            x += barWidth + space
        }
    }
}
