package com.digicolor.rahuldemo.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.digicolor.rahuldemo.presentation.theme.BorderColor

fun Modifier.topBorder(width: Dp = 1.dp, color: Color = BorderColor, shape: RoundedCornerShape) =
    this.drawBehind {
        val borderWidth = width.toPx()
        val outline = shape.createOutline(size, layoutDirection, this)

        if (outline is Outline.Rounded) {
            val roundRect = outline.roundRect
            val topLeftRadius = roundRect.topLeftCornerRadius
            val topRightRadius = roundRect.topRightCornerRadius

            val path = Path().apply {
                moveTo(0f, topLeftRadius.y)

                // Top-left corner
                arcTo(
                    rect = Rect(0f, 0f, topLeftRadius.x * 2, topLeftRadius.y * 2),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Top line
                lineTo(size.width - topRightRadius.x, 0f)

                // Top-right corner
                arcTo(
                    rect = Rect(
                        size.width - topRightRadius.x * 2, 0f,
                        size.width, topRightRadius.y * 2
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            }

            drawPath(path, color, style = Stroke(borderWidth))
        }
    }