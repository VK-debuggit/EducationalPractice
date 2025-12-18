package com.example.educationalpractice.Data.Components

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.ui.theme.SubTextDark
import com.example.educationalpractice.ui.theme.Text
import com.example.educationalpractice.R

//Компонент: диалоговое окно
//@Composable
//fun CustomAlertDialog(
//    onDismissRequest: () -> Unit,
//    dialogTitle: String,
//    dialogText: String,
//    iconResId: Int? = R.drawable.emailotp,
//    iconTint: Color = Accent,
//    confirmButtonText: String = "OK",
//    confirmButtonColor: Color = Accent,
//    dismissButtonText: String? = null,
//    onConfirmButtonClick: () -> Unit = onDismissRequest,
//    onDismissButtonClick: () -> Unit = onDismissRequest
//) {
//    AlertDialog(
//        onDismissRequest = onDismissRequest,
//        icon = iconResId?.let {
//            {
//                Icon(
//                    imageVector = ImageVector.vectorResource(id = it),
//                    contentDescription = "Dialog icon",
//                    modifier = Modifier.size(32.dp),
//                    tint = iconTint
//                )
//            }
//        },
//        title = {
//            Text(
//                text = dialogTitle,
//                color = Block,
//                fontSize = 18.sp,
//                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//            )
//        },
//        text = {
//            Text(
//                text = dialogText,
//                color = SubTextDark,
//                fontSize = 16.sp,
//                lineHeight = 20.sp
//            )
//        },
//        confirmButton = {
//            Text(
//                text = "ОК",
//                color = Accent,
//                fontSize = 16.sp,
//                lineHeight = 20.sp
//            )
//        },
//        dismissButton = {
//            Text(
//                text = "Отмена",
//                color = Hint,
//                fontSize = 16.sp,
//                lineHeight = 20.sp
//            )
//        }
//    )
//}
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.SubTextLight
import com.example.educationalpractice.ui.theme.Typography
import com.example.educationalpractice.ui.theme.Text as ThemeText

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    iconResId: Int? = null,
    confirmButtonText: String = "OK",
    dismissButtonText: String? = null,
    onConfirmButtonClick: () -> Unit = onDismissRequest,
    onDismissButtonClick: () -> Unit = onDismissRequest
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = dialogTitle,
                color = Text,
                style = Typography.labelLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = dialogText,
                color = SubTextDark,
                style = Typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            // Если есть обе кнопки - располагаем в Row
            if (dismissButtonText != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Кнопка Отмена слева
                    Button(
                        onClick = onDismissButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SubTextLight,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text(
                            text = dismissButtonText,
                            color = Text,
                            style = Typography.labelSmall
                        )
                    }

                    // Кнопка OK справа
                    Button(
                        onClick = onConfirmButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) {
                        Text(
                            text = confirmButtonText,
                            color = Block,
                            style = Typography.labelSmall
                        )
                    }
                }
            } else {
                // Если только одна кнопка OK - по центру
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onConfirmButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = confirmButtonText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        dismissButton = null, // Устанавливаем null, т.к. кнопки уже в confirmButton
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview
@Composable
private fun AlertDialogPreview() {
    EducationalPracticeTheme{
        CustomAlertDialog(
            onDismissRequest = { "" },
            dismissButtonText = "Отмена",
            dialogTitle = "Ошибка",
            dialogText = "Ошибка",
            iconResId = null,
            onConfirmButtonClick = { "fdsdsf" }
        )
    }
}