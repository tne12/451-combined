package com.example.networksignalapp.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.networksignalapp.network.AuthService
import com.example.networksignalapp.network.LoginRequest
import com.example.networksignalapp.network.UserSessionManager
import com.example.networksignalapp.network.RetrofitClient
import kotlinx.coroutines.launch
import com.example.networksignalapp.ui.components.BottomNavigationBar
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment



@Composable
fun LoginScreen(
    navController: NavController,
    context: Context,
    selectedTab: String = "login",
    onOverviewSelected: () -> Unit = {},
    onServerSelected: () -> Unit = {},
    onStatisticsSelected: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val authService = RetrofitClient.create(AuthService::class.java)
    val session = UserSessionManager(context)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onOverviewSelected = onOverviewSelected,
                onServerSelected = onServerSelected,
                onStatisticsSelected = onStatisticsSelected,
                navController = navController
            )
        }
    ) { innerPadding ->
        // Center the card vertically and horizontally
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .widthIn(min = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Login", style = MaterialTheme.typography.headlineMedium)

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                val response = authService.login(LoginRequest(username, password))
                                if (response.isSuccessful) {
                                    response.body()?.token?.let { token ->
                                        session.saveToken(token)
                                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home")
                                    }
                                } else {
                                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }

                    TextButton(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Don't have an account? Register")
                    }
                }
            }
        }
    }
}
