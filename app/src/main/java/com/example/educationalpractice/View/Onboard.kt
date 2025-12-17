package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.Data.Components.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.CustomTypography
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.Splash
import com.example.educationalpractice.ui.theme.Text

@Composable
fun Onboard(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Splash
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.3f))
        Text(
            text = stringResource(R.string.welcome_mes),
            style = CustomTypography.headingBold30,
            color = Block
        )
        Image(
            painter = painterResource(id = R.drawable.image_1),
            contentDescription = "Обувь",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        )
        Spacer(Modifier.weight(0.1f))

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            CustomButton(
                onClick = {},
                text = stringResource(R.string.start),
                modifier = Modifier.fillMaxWidth(),
                containerColor = Block,
                contentColor = Text,
                cornerRadius = 13
            )
        }
        Spacer(Modifier.weight(0.1f))
    }
}

@Preview
@Composable
private fun OnboardPreview() {
    EducationalPracticeTheme {
        Onboard()
    }
}