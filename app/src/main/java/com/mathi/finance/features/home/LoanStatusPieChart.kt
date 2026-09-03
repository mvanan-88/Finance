package com.mathi.finance.features.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoanStatusPieChart(
    activeCount: Int,
    completedCount: Int,
    modifier: Modifier = Modifier
) {
    val total = (activeCount + completedCount).toFloat()
    val activeAngle = if (total > 0) (activeCount / total) * 360f else 0f

    val activeColor = MaterialTheme.colorScheme.primary
    val completedColor = MaterialTheme.colorScheme.tertiary

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Loan Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                StatusLegend(label = "Active", count = activeCount, color = activeColor)
                Spacer(modifier = Modifier.height(8.dp))
                StatusLegend(label = "Completed", count = completedCount, color = completedColor)
            }

            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    val strokeWidth = 15.dp.toPx()
                    // Draw completed (as the background circle)
                    drawArc(
                        color = completedColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(strokeWidth)
                    )
                    // Draw active (proportional part)
                    if (total > 0) {
                        drawArc(
                            color = activeColor,
                            startAngle = -90f,
                            sweepAngle = activeAngle,
                            useCenter = false,
                            style = Stroke(strokeWidth)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = total.toInt().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StatusLegend(label: String, count: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(12.dp),
            shape = RoundedCornerShape(4.dp),
            color = color
        ) {}
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: $count",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
