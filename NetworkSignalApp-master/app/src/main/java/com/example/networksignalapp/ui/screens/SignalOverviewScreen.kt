package com.example.networksignalapp.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit

@Composable
fun SignalOverviewScreen(
    onNavigateToServer: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    // 🔁 Submit test data once when screen appears
    LaunchedEffect(Unit) {
//        val api = ApiClient.retrofit.create(ApiService::class.java)
        val api= ApiClient.apiService

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
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
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

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "overview",
                onOverviewSelected = {},
                onServerSelected = onNavigateToServer,
                onStatisticsSelected = onNavigateToStatistics
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Signal",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

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

            SanFranciscoMap()

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Network Information", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    NetworkInfoItem("Operator", "T-Mobile")
                    NetworkInfoItem("Signal Power", "-95dBm")
                    NetworkInfoItem("SINR/SNR", "20dB")
                    NetworkInfoItem("Network Type", "LTE")
                    NetworkInfoItem("Frequency Band", "Band 66")
                    NetworkInfoItem("Cell ID", "1234567")
                    NetworkInfoItem("Time Stamp", "2022-02-13 12:34:56")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Speed test", style = MaterialTheme.typography.titleMedium, color = Color.White)
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
                colors = CardDefaults.cardColors(containerColor = DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Signal strength", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Column(horizontalAlignment = Alignment.End) {
                            Text("-106dBm", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            Text("This month -2%", style = MaterialTheme.typography.labelSmall, color = Red)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LineChartView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
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
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = LightGray.copy(alpha = 0.5f)
    )
}
