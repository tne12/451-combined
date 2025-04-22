package com.example.networksignalapp.ui.screens

import android.content.Context
import android.widget.Toast
import java.io.File
import android.os.Environment  // <-- Add this import at the top

val file = File(
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
    "network_data.csv"
)

fun exportToCsv(
    context: Context,
    operator: String,
    signalStrength: String,
    networkType: String,
    sinr: String
) {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "network_data.csv"
    )

    val fileName = "network_data.csv"
//    val file = File(context.getExternalFilesDir(null), fileName)

    val csvHeader = "Operator,Signal Strength,Network Type,SINR\n"
    val csvData = "$operator,$signalStrength,$networkType,$sinr\n"



    try {
        if (file.length()==0L){
            file.appendText(csvHeader)
        }
        file.appendText(csvData)
        Toast.makeText(context, "✅ Data exported to $fileName", Toast.LENGTH_SHORT).show()


    } catch (e: Exception) {
        Toast.makeText(context, "❌ Failed to export CSV: ${e.message}", Toast.LENGTH_LONG).show()
    }
}