package com.example.dividir_cuentas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dividir_cuentas.ui.theme.Dividir_cuentasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dividir_cuentasTheme {
                MyScreen()
            }
        }
    }
}

@Composable
fun MyScreen() {
    // Aquí se declara el estado del campo de texto
    var textcan by remember { mutableStateOf("")  }
    var textcom by remember { mutableStateOf("") }
    var checksw by remember { mutableStateOf(true) }
    var sliderposition by remember { mutableFloatStateOf(0f) }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textcan,
                onValueChange = { newValue -> textcan = newValue },
                label = { Text("Cantidad") },

            )
            OutlinedTextField(
                value = textcom,
                onValueChange = { newValue -> textcom = newValue },
                label = { Text("Comensales") },
            )
            Text("Redondear")
            Switch(
                checked = checksw,
                onCheckedChange = {
                    checksw = it
                }
            )
            Text("Valoración")
            Slider(
                value = sliderposition,
                onValueChange = {
                    sliderposition = it
                },
                steps = 4,
                valueRange = (0f..5f)
            )
            // Validación y cálculo de la propina
            if (checksw && textcan.isNotEmpty() && textcan.toIntOrNull() != null) {
                val propina = calcular_propina(textcan, sliderposition)
                Text("Propina: ${"%.2f".format(propina)}")
            }
        }
    }
}

// Función para calcular la propina
fun calcular_propina(textcan: String, sliderpos: Float): Double {
    val can = textcan.toIntOrNull() ?: 0  // Si no es un número válido, se asume 0
    val prop = when(sliderpos) {
        0f -> 0.0
        1f -> can * 0.05
        2f -> can * 0.1
        3f -> can * 0.15
        4f -> can * 0.20
        5f -> can * 0.25
        else -> 0.0
    }
    return prop
}
