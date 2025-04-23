package com.example.networksignalapp.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.networksignalapp.R
import com.example.networksignalapp.network.ApiClient
import com.example.networksignalapp.network.ApiService
import com.example.networksignalapp.network.SubmitRequest
import com.example.networksignalapp.ui.components.BottomNavigationBar
import com.example.networksignalapp.ui.components.InfoCard
import com.example.networksignalapp.ui.components.LineChartView
import com.example.networksignalapp.ui.components.SanFranciscoMap
import com.example.networksignalapp.ui.theme.DarkGray
import com.example.networksignalapp.ui.theme.LightGray
import com.example.networksignalapp.ui.theme.Red
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.platform.LocalContext
import com.example.networksignalapp.ui.screens.exportToCsv
import android.widget.Toast
import java.io.File
import com.example.networksignalapp.MainActivity
import androidx.navigation.NavController

@Composable
fun SignalOverviewScreen(
    navController: NavController,
    onNavigateToServer: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onToggleTheme: () -> Unit,
    darkMode: Boolean

) {
    val operator = remember { mutableStateOf("T-Mobile") }
    val signalStrength = remember { mutableStateOf("-95dBm") }
    val networkType = remember { mutableStateOf("LTE") }
    val sinr = remember { mutableStateOf("20dB") }

    // 🔁 Submit test data once when screen appears
    LaunchedEffect(Unit) {
        val api = ApiClient.apiService

        val request = SubmitRequest(
            timestamp = "2025-04-08 13:05:00",
            operator = "Alfa",
            signal_power = -88,
            snr = 20.5f,
            network_type = "4G",
            band = "20",
            cell_id = "37100-83938403",
            device_ip = "192.168.1.3",
            device_mac = "AA:BB:CC:DD:EE:01"
        )

        api.submitSignalData(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API", "✅ Data submitted successfully")
                } else {
                    Log.e("API", "❌ Submission failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "⚠️ Error: ${t.message}")
            }
        })
    }
    LaunchedEffect(Unit) {
        // You already have this block for submitting data...
        // Add this below or inside it:

        // TEMPORARY: Fill SignalCache with mock values (just once)
        SignalCache.operator = "Alfa"
        SignalCache.signalStrength = "-106 dBm"
        SignalCache.networkType = "4G"
        SignalCache.sinr = "20.5 dB"
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "overview",
                onOverviewSelected = {},
                onServerSelected = onNavigateToServer,
                onStatisticsSelected = onNavigateToStatistics,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .background(Color.Black)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Signal",
                style = MaterialTheme.typography.titleLarge,
//                color = Color.White
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (darkMode) "🌙 Dark Mode" else "☀️ Light Mode",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = darkMode,
                    onCheckedChange = { onToggleTheme() }
                )
            }

            InfoCard(
                icon = R.drawable.ic_signal,
                title = "Signal strength",
                value = "-106dBm"
            )

            InfoCard(
                icon = R.drawable.ic_wifi,
                title = "Network type",
                value = "3G"
            )

            SanFranciscoMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Network Information", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))
                    NetworkInfoItem("Operator", operator.value)
                    NetworkInfoItem("Signal Power", signalStrength.value)
                    NetworkInfoItem("SINR/SNR", sinr.value)
                    NetworkInfoItem("Network Type", networkType.value)
                    NetworkInfoItem("Frequency Band", "Band 66")
                    NetworkInfoItem("Cell ID", "1234567")
                    NetworkInfoItem("Time Stamp", "2022-02-13 12:34:56")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Speed test", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))
                    NetworkInfoItem("Download speed:", "12.4 Mbps")
                    NetworkInfoItem("Upload speed:", "8.3 Mbps")
                    NetworkInfoItem("Ping:", "109 ms")
                    NetworkInfoItem("Jitter:", "9 ms")
                    NetworkInfoItem("Packet loss:", "0%")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Signal strength", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                        Column(horizontalAlignment = Alignment.End) {
                            Text("-106dBm", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                            Text("This month -2%", style = MaterialTheme.typography.labelSmall, color = Red)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LineChartView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            exportToCsv(
                                context = context,
                                operator = SignalCache.operator,
                                signalStrength = SignalCache.signalStrength,
                                networkType = SignalCache.networkType,
                                sinr = SignalCache.sinr
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Export Data")
                    }
                    Button(
                        onClick = {
                            Toast.makeText(context, "📶 Weak signal detected: -106 dBm", Toast.LENGTH_SHORT).show()
                            (context as? MainActivity)?.let { it.showWeakSignalNotification() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("🔔 Test Weak Signal Notification")
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = LightGray.copy(alpha = 0.5f)
    )
}