package com.example.educationalpractice.View

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.Data.Components.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.utils.OnboardingManager

@Composable
fun OnboardScreen3(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val onboardingManager = remember { OnboardingManager(context) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.Accent),
                        colorResource(R.color.Disable),
                        colorResource(id = R.color.Disable).copy(alpha = 0.8f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.15f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(400),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(400)),
                exit = slideOutHorizontally(
                    animationSpec = tween(400),
                    targetOffsetX = { -it }
                ) + fadeOut(animationSpec = tween(400))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_3),
                    contentDescription = "Комнатные растения",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.we_have_power),
                style = Typography.displayLarge,
                color = Block,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.flowers),
                style = Typography.titleLarge,
                color = SubTextLight,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.05f))

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(4.dp)
                    .background(SubTextLight.copy(alpha = 0.3f), androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(4.dp)
                    .background(SubTextLight.copy(alpha = 0.3f), androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
            Spacer(
                modifier = Modifier
                    .width(43.dp)
                    .height(4.dp)
                    .background(Block, androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
        }

        Spacer(Modifier.weight(0.05f))

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            CustomButton(
                onClick = {
                    onboardingManager.setOnboardingCompleted()
                    NavigationManager.navigateTo(Views.RegisterAccount.route)
                },
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
private fun OnboardScreen3Preview() {
    EducationalPracticeTheme {
        OnboardScreen3()
    }
}