package com.example.educationalpractice.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educationalpractice.Data.Screens.HomeScreen
import com.example.educationalpractice.View.*
import com.example.educationalpractice.ui.theme.Background

//Навигация
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

    fun navigateToWithClearBackStack(route: String) {
        navController?.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Устанавливаем NavController в менеджер
    NavigationManager.setNavController(navController)

    NavHost(
        navController = navController,
        startDestination = Views.Home.route
    ) {
        composable(Views.Onboarding.route) {
            Onboard()
        }
        composable(Views.Home.route) {
            HomeScreen(Modifier.background(Background))
        }
        composable(Views.OnboardScreen2.route) {
            OnboardScreen2()
        }
        composable(Views.OnboardScreen3.route) {
            OnboardScreen3()
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
    object Onboarding : Views("onboarding")
    object Home : Views("home")
    object OnboardScreen2 : Views("onboardscreen2")
    object OnboardScreen3 : Views("onboardscreen3")
    object CreateNewPassword : Views("create_new_password")
    object RegisterAccount : Views("register_account")
    object SignIn : Views("sign_in")
    object Verification : Views("verification")
    object ForgotPassword : Views("forgot_password")
}