package com.wilson.smartexpensetracker.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryPieChart(
    categoryTotals: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    if (categoryTotals.isEmpty()) return

    val total = categoryTotals.values.sum()

    // Generate distinct colors dynamically
    val colors = remember(categoryTotals.size) {
        List(categoryTotals.size) { index ->
            Color.hsv(
                hue = (index * 360f / categoryTotals.size) % 360f,
                saturation = 0.7f,
                value = 0.9f
            )
        }
    }

    val sweepAngles = categoryTotals.values.map { (it / total * 360f).toFloat() }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pie Chart
        Canvas(
            modifier = Modifier
                .size(180.dp)
                .padding(8.dp)
        ) {
            var startAngle = 0f
            sweepAngles.forEachIndexed { index, sweep ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }

        Spacer(Modifier.height(8.dp))

        // Legend: Color + Name + Amount + Percentage
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            categoryTotals.entries.forEachIndexed { index, (category, amount) ->
                val percentage = (amount / total * 100).toInt()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(colors[index], shape = CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "$category : ₹${amount.toInt()} (${percentage}%)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
