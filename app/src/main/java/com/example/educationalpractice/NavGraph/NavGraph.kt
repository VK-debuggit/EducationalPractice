package com.example.educationalpractice.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educationalpractice.Data.Screens.HomeScreen
import com.example.educationalpractice.View.*
import com.example.educationalpractice.ui.screens.ProfileFormScreen
import com.example.educationalpractice.ui.theme.Background
import com.example.educationalpractice.utils.OnboardingManager

object NavigationManager {
    private var navController: NavHostController? = null

    fun setNavController(navController: NavHostController) {
        this.navController = navController
    }

    fun navigateTo(route: String) {
        navController?.navigate(route)
    }

    fun navigateBack() {
        navController?.popBackStack()
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val onboardingManager = remember { OnboardingManager(context) }

    LaunchedEffect(Unit) {
        println("DEBUG: AppNavigation started")
        println("DEBUG: onboarding completed = ${onboardingManager.isOnboardingCompleted()}")
        println("DEBUG: start destination = ${if (onboardingManager.isOnboardingCompleted()) Views.RegisterAccount.route else Views.Onboard.route}")
    }

    NavigationManager.setNavController(navController)

    NavHost(
        navController = navController,
        startDestination = if (onboardingManager.isOnboardingCompleted()) {
            Views.RegisterAccount.route
        } else {
            Views.Onboard.route
        }
    ) {
        composable(Views.Onboard.route) {
            Onboard()
        }
        composable(Views.OnboardScreen2.route) {
            OnboardScreen2()
        }
        composable(Views.OnboardScreen3.route) {
            OnboardScreen3()
        }
        composable(Views.Profile.route) {
            ProfileFormScreen()
        }
        composable(Views.Home.route) {
            HomeScreen(Modifier.background(Background))
        }
        composable(Views.RegisterAccount.route) {
            RegisterAccount()
        }
        composable(Views.CreateNewPassword.route) {
            CreateNewPassword()
        }
        composable(Views.SignIn.route) {
            SignIn()
        }
        composable(Views.Verification.route) {
            Verification()
        }
        composable(Views.ForgotPassword.route) {
            ForgotPassword()
        }
    }
}

sealed class Views(val route: String) {
    object Onboard : Views("onboard")
    object Home : Views("home")
    object Profile : Views("profile")
    object OnboardScreen2 : Views("onboardscreen2")
    object OnboardScreen3 : Views("onboardscreen3")
    object CreateNewPassword : Views("create_new_password")
    object RegisterAccount : Views("register_account")
    object SignIn : Views("sign_in")
    object Verification : Views("verification")
    object ForgotPassword : Views("forgot_password")
}