package com.example.educationalpractice.Data.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Background
import com.example.educationalpractice.ui.theme.Disable

//Компонент: кнопка
@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color = Accent,
    contentColor: Color = Background,
    disabledContainerColor: Color = Disable,
    disabledContentColor: Color = Background,
    cornerRadius: Int = 13,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        enabled = enabled
    ) {
        Text(
            text,
            style = textStyle
        )
    }
}