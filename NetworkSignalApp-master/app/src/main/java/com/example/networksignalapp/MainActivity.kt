package com.example.networksignalapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.networksignalapp.ui.screens.NetworkStatisticsScreen
import com.example.networksignalapp.ui.screens.ServerScreen
import com.example.networksignalapp.ui.screens.SignalCache
import com.example.networksignalapp.ui.screens.SignalOverviewScreen
import com.example.networksignalapp.ui.screens.RegisterScreen
import com.example.networksignalapp.ui.screens.LoginScreen
import com.example.networksignalapp.ui.theme.NetworkSignalAppTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {

    private lateinit var telephonyManager: TelephonyManager
    @Suppress("DEPRECATION")
    private lateinit var listener: PhoneStateListener

    private val CHANNEL_ID = "weak_signal_channel"
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startMonitoringSignal()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    2
                )
            }
        }

        setContent {
            var darkMode by remember { mutableStateOf(true) }

            NetworkSignalAppTheme(darkTheme = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NetworkSignalApp(
                        darkMode = darkMode,
                        onToggleTheme = { darkMode = !darkMode }
                    )
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun startMonitoringSignal() {
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // Debug: check permission state
        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        Toast.makeText(this, "📡 READ_PHONE_STATE permission = $permissionStatus", Toast.LENGTH_LONG).show()

        listener = object : PhoneStateListener() {
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)
                val dbm = try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        signalStrength.cellSignalStrengths.first().dbm
                    } else {
                        -120
                    }
                } catch (e: Exception) {
                    -120
                }
                SignalCache.signalStrength = "$dbm dBm"
                SignalCache.operator = "Alfa" // replace later if you can detect the real one
                SignalCache.networkType = "4G"
                SignalCache.sinr = "18.5 dB"

                Log.d("SignalDebug", "Signal level changed: $dbm dBm")

                if (dbm < -100) {
                    Toast.makeText(this@MainActivity, "📶 Weak signal detected: $dbm dBm", Toast.LENGTH_SHORT).show()
                    Log.d("SignalDebug", "📶 Current signal strength: $dbm dBm")
                    Toast.makeText(this@MainActivity, "📶 Signal = $dbm dBm", Toast.LENGTH_SHORT).show()
                    showWeakSignalNotification()
                }
            }
        }

        telephonyManager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
    }

    fun showWeakSignalNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Weak Signal Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("Weak Signal Detected")
            .setContentText("Your signal dropped below -100 dBm.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startMonitoringSignal()
        }
    }
}

@Composable
fun NetworkSignalApp(
    darkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "overview"
    ) {
        composable("overview") {
            SignalOverviewScreen(
                onNavigateToServer = { navController.navigate("server") },
                onNavigateToStatistics = { navController.navigate("statistics") },
                onToggleTheme = onToggleTheme,
                darkMode = darkMode,
                navController = navController
            )
        }
        composable("server") {
            ServerScreen(
                navController = navController,
                onNavigateToOverview = { navController.navigate("overview") },
                onNavigateToStatistics = { navController.navigate("statistics") }
            )
        }
        composable("statistics") {
            NetworkStatisticsScreen(
                navController = navController,
                onNavigateToOverview = { navController.navigate("overview") },
                onNavigateToServer = { navController.navigate("server") },
            )
        }
        composable("login") {
            LoginScreen(navController = navController, context = LocalContext.current)
        }
        composable("register") {
            RegisterScreen(navController = navController, context = LocalContext.current)
        }
    }
}
