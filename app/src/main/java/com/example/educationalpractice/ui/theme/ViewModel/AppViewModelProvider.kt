// В том же файле ViewModel или отдельном
package com.example.educationalpractice.ui.theme.ViewModel

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SignUpViewModel()
        }
        initializer {
            ForgotPasswordViewModel()
        }
    }
}