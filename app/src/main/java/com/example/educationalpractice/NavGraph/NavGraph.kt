// NavGraph.kt
package com.example.educationalpractice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educationalpractice.View.*

// Объект для доступа к NavController из любого места
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
        startDestination = Views.RegisterAccount.route
    ) {
        composable(Views.CreateNewPassword.route) {
            CreateNewPassword()
        }
        composable(Views.RegisterAccount.route) {
            RegisterAccount()
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
    object CreateNewPassword : Views("create_new_password")
    object RegisterAccount : Views("register_account")
    object SignIn : Views("sign_in")
    object Verification : Views("verification")
    object ForgotPassword : Views("forgot_password")
}