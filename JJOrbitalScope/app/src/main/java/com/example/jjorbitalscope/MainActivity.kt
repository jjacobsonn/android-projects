package com.example.jjorbitalscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.net.URL

@Serializable
data class JJPlanet(
    val type: String,
    val name: String,
    val moonCount: String,
    val distanceFromSun: String,
    val diameterKm: String,
    val satellites: List<Satellite> = emptyList()
)

@Serializable
data class Satellite(
    val name: String,
    val diameterKm: String
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val satelliteInfoList = remember { mutableStateOf(listOf<Satellite>()) }

        LaunchedEffect(Unit) {
            val info = fetchSatelliteInfo()
            satelliteInfoList.value = info
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OrbitalScope",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            satelliteInfoList.value.forEach { satellite ->
                SatelliteCard(satellite)
            }
        }
    }

    @Composable
    fun SatelliteCard(satellite: Satellite) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Satellite:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF4A148C)
                )
                Text(
                    text = satellite.name,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Diameter:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF4A148C)
                )
                Text(
                    text = "${satellite.diameterKm} km",
                    fontSize = 16.sp
                )
            }
        }
    }

    suspend fun fetchSatelliteInfo(): List<Satellite> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = URL("https://roversgame.net/cs3680/planets.json").readText()
                val planets = Json.decodeFromString<List<JJPlanet>>(jsonString)
                planets.find { it.name == "Jupiter" }?.satellites ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}