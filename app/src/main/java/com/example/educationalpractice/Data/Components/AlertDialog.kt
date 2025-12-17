package com.example.educationalpractice.Data.Components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.ui.theme.SubTextDark

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Text as ThemeText

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    iconResId: Int? = R.drawable.emailotp,
    iconTint: Color = Accent,
    confirmButtonText: String = "OK",
    dismissButtonText: String? = null,
    onConfirmButtonClick: () -> Unit = onDismissRequest,
    onDismissButtonClick: () -> Unit = onDismissRequest
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = iconResId?.let {
            {
                Icon(
                    imageVector = ImageVector.vectorResource(id = it),
                    contentDescription = "Dialog icon",
                    modifier = Modifier.size(32.dp),
                    tint = iconTint
                )
            }
        },
        title = {
            Text(
                text = dialogTitle,
                color = ThemeText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = dialogText,
                color = SubTextDark,
                fontSize = 16.sp,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text(
                    text = confirmButtonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = if (dismissButtonText != null) {
            {
                TextButton(
                    onClick = onDismissButtonClick
                ) {
                    Text(
                        text = dismissButtonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            null
        }
    )
}