package com.example.educationalpractice.Data

import android.graphics.Color
import android.widget.Button
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Disable
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import org.w3c.dom.Text

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
    text: Text,
    buttonColor: Color,
    cornerShape: RoundedCornerShape
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        shape = cornerShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Disable,
            contentColor = White,
            disabledContainerColor = Blue,
            disabledContentColor = White),
    ) {
    }
}