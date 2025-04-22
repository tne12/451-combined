package com.example.networksignalapp.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.material3.MaterialTheme

@Composable
fun LineChartView(modifier: Modifier = Modifier) {
    // ✅ Extract theme colors at the top (inside @Composable scope)
    val axisColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val labelColor = MaterialTheme.colorScheme.onBackground.toArgb()

    val data = listOf(
        Pair("1/1", -110f),
        Pair("1/15", -105f),
        Pair("1/30", -106f)
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 50f
        val chartWidth = canvasWidth - (padding * 2)
        val chartHeight = canvasHeight - (padding * 2)

        // Axis paint
        val axisPaint = Paint().apply {
            color = axisColor
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }

        // Draw X and Y axis
        drawContext.canvas.nativeCanvas.drawLine(
            padding, canvasHeight - padding,
            canvasWidth - padding, canvasHeight - padding,
            axisPaint
        )

        drawContext.canvas.nativeCanvas.drawLine(
            padding, padding,
            padding, canvasHeight - padding,
            axisPaint
        )

        // Point & line paint
        val pointPaint = Paint().apply {
            color = primaryColor
            strokeWidth = 8f
            style = Paint.Style.FILL
        }

        val linePaint = Paint().apply {
            color = primaryColor
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }

        val textPaint = Paint().apply {
            color = labelColor
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }

        val xStep = chartWidth / (data.size - 1)
        val minValue = -120f
        val maxValue = -50f
        val valueRange = maxValue - minValue

        val points = data.mapIndexed { index, (label, value) ->
            val x = padding + (index * xStep)
            val normalizedValue = (value - minValue) / valueRange
            val y = canvasHeight - padding - (normalizedValue * chartHeight)

            drawContext.canvas.nativeCanvas.drawText(
                label,
                x,
                canvasHeight - padding + 30f,
                textPaint
            )

            Pair(x, y)
        }

        for (i in 0 until points.size - 1) {
            drawContext.canvas.nativeCanvas.drawLine(
                points[i].first, points[i].second,
                points[i + 1].first, points[i + 1].second,
                linePaint
            )
        }

        points.forEach { (x, y) ->
            drawContext.canvas.nativeCanvas.drawCircle(x, y, 6f, pointPaint)
        }
    }
}