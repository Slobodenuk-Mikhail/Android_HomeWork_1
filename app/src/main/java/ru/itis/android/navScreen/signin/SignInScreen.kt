package ru.itis.android.navScreen.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.itis.android.R
import ru.itis.android.data.users.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.navigation.CatalogObject
import ru.itis.android.utils.ResManager

@Composable
fun SignInScreen(
    navController: NavHostController,
    resManager: ResManager
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val userRepository = remember { ServiceLocator.getUserRepository() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.signin_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = ""
            },
            label = { Text(stringResource(R.string.login_hint)) },
            modifier = Modifier.fillMaxWidth(),
            isError = error.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
            },
            label = { Text(stringResource(R.string.password_hint)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = error.isNotEmpty()
        )

        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = Color.Red
            )
        }

        Button(
            onClick = {
                // Валидация на UI уровне
                if (username.isEmpty() || password.isEmpty()) {
                    error = resManager.getString(R.string.validation_empty)
                    return@Button
                }

                scope.launch {
                    loading = true
                    val userId = userRepository.login(username, password)

                    if (userId != null) {
                        // Успешный вход
                        UserSession.login(userId, username)

                        // Переходим на главный экран с очисткой стека
                        navController.navigate(CatalogObject.route) {
                            // Очищаем весь стек навигации
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        error = resManager.getString(R.string.invalid_credentials)
                    }
                    loading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(stringResource(R.string.signin_button))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("signUp")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.signup_button))
        }
    }
}