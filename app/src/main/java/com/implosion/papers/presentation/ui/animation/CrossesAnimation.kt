package com.implosion.papers.presentation.ui.animation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random


// Рисование всех крестиков
fun DrawScope.drawCrosses(crosses: List<FloatingCross>) {
    for (cross in crosses) {
        drawCross(cross)
    }
}

// Обновление положения крестиков с учетом времени
fun animateCrosses(
    crosses: List<FloatingCross>,
    time: Float,
    maxWidth: Float = 1000f,
    maxHeight: Float = 1000f
) {
    for (cross in crosses) {
        cross.x += cross.velocityX * time
        cross.y += cross.velocityY * time

        // Отражение от краев экрана
        if (cross.x < 0 || cross.x > maxWidth) {
            cross.velocityX *= -1
        }
        if (cross.y < 0 || cross.y > maxHeight) {
            cross.velocityY *= -1
        }
    }
}

fun generateCrosses(count: Int): List<FloatingCross> {
    val random = Random.Default
    return List(count) {
        FloatingCross(
            x = random.nextFloat() * 1000, // Положение на оси X
            y = random.nextFloat() * 1000, // Положение на оси Y
            size = random.nextFloat() * 45 + 5, // Размер крестика
            velocityX = (random.nextFloat() - 0.5f) * 2, // Скорость X
            velocityY = (random.nextFloat() - 0.5f) * 2, // Скорость Y
            color = Color(
                red = random.nextFloat(),
                green = random.nextFloat(),
                blue = random.nextFloat(),
                alpha = 0.2f
            ) // Случайный цвет
        )
    }
}

// Рисование одного крестика
private fun DrawScope.drawCross(cross: FloatingCross) {
    val halfSize = cross.size / 2
    drawLine(
        color = cross.color,
        start = Offset(cross.x - halfSize, cross.y - halfSize),
        end = Offset(cross.x + halfSize, cross.y + halfSize),
        strokeWidth = 2f
    )
    drawLine(
        color = cross.color,
        start = Offset(cross.x - halfSize, cross.y + halfSize),
        end = Offset(cross.x + halfSize, cross.y - halfSize),
        strokeWidth = 2f
    )
}

data class FloatingCross(
    var x: Float,
    var y: Float,
    val size: Float,
    var velocityX: Float,
    var velocityY: Float,
    val color: Color
)