import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class NotchBottomBarShape(
    private val centerButtonWidth: Float = 64f, // Ширина выступающего элемента в пикселях
    private val notchDepth: Float = 20f,      // Глубина выреза в пикселях
    private val cornerRadius: Float = 20f    // Закругление боковых краев в пикселях
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()

        // Константы для удобства
        val halfWidth = size.width / 2f
        val leftInner = halfWidth - (centerButtonWidth / 2f)
        val rightInner = halfWidth + (centerButtonWidth / 2f)

        // 1. Начинаем с нижней левой точки
        path.moveTo(0f, size.height)
        path.lineTo(size.width, size.height) // Нижний правый угол

        // 2. Идем вверх по правому краю (до начала закругления)
        path.lineTo(size.width, 0f)

        // 3. Рисуем правый верхний угол (закругление)
        path.arcTo(
            rect = Rect(
                left = size.width - 2 * cornerRadius,
                top = 0f,
                right = size.width,
                bottom = 2 * cornerRadius
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        // 4. Идем влево до начала выреза
        path.lineTo(rightInner + cornerRadius, 0f)

        // 5. Рисуем линию к вырезу (спуск)
        path.lineTo(rightInner, notchDepth)

        // 6. Дно выреза (горизонтально)
        path.lineTo(leftInner, notchDepth)

        // 7. Возврат вверх к левому закруглению
        path.lineTo(leftInner - cornerRadius, 0f)

        // 8. Рисуем левый верхний угол (закругление)
        path.arcTo(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = 2 * cornerRadius,
                bottom = 2 * cornerRadius
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        // 9. Закрываем путь (возвращаемся в 0, size.height)
        path.close()

        return Outline.Generic(path)
    }
}