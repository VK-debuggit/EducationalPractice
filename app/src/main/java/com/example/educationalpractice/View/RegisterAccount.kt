package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.Data.CustomButton
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Background
import com.example.educationalpractice.ui.theme.Disable
import com.example.educationalpractice.ui.theme.Text
import com.example.educationalpractice.ui.theme.SubTextDark

@Composable
fun RegisterAccount() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Spacer(Modifier.weight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.iconback),
            contentDescription = "Назад",
            modifier = Modifier
        )
        Spacer(Modifier.weight(0.1f))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_mes),
                color = Text,
                fontSize = 32.sp
            )
            Text(
                text = stringResource(R.string.data),
                color = SubTextDark,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.weight(0.1f))
        Text(
            text = stringResource(R.string.Your),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp),
            value = name,
            placeholder = { Text("xxxxxxxx") },
            onValueChange = { name = it },
            enabled = !isLoading
        )

        Spacer(Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.email),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            value = email,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            onValueChange = { email = it },
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("xyz@gmail.com") },
            enabled = !isLoading
        )

        Spacer(Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.password),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(17.dp, 13.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(id = R.drawable.eyeclose),
                        contentDescription = null
                    )
                }
            },
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(14.dp),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.agree),
                contentDescription = "Назад",
                modifier = Modifier
                    .size(10.dp, 10.dp)
            )
            Spacer(Modifier.width(15.dp))
            Text(
                text = stringResource(R.string.Agree),
                color = SubTextDark,
                fontSize = 16.sp
            )
        }
        CustomButton(
            onClick = {},
            text = stringResource(R.string.Sign),
            enabled = !isLoading,
            disabledContainerColor = Disable
        )
        Spacer(Modifier.weight(0.5f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Already),
                color = SubTextDark,
                fontSize = 16.sp
            )
        }
    }

}

@Preview
@Composable
private fun RegisterAccountPreview() {
    EducationalPracticeTheme() {
        RegisterAccount()
    }
}