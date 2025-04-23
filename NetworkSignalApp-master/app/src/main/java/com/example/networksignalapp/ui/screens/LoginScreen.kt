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

@Composable
fun LoginScreen(navController: NavController, context: Context) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val authService = RetrofitClient.create(AuthService::class.java)
    val session = UserSessionManager(context)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
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
        }) {
            Text("Login")
        }
    }
}
