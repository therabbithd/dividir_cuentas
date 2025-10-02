package com.example.dividir_cuentas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dividir_cuentas.ui.theme.Dividir_cuentasTheme
import kotlin.math.roundToInt

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
    var textcan by remember { mutableStateOf("") }
    var textcom by remember { mutableStateOf("") }
    var checksw by remember { mutableStateOf(true) }
    var sliderposition by remember { mutableFloatStateOf(0f) }

    var propinaCalculada by remember { mutableStateOf<Double?>(null) }
    var totalPorPersona by remember { mutableStateOf<Double?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = textcan,
                onValueChange = { textcan = it },
                label = { Text("Cantidad de la cuenta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = textcom,
                onValueChange = { textcom = it },
                label = { Text("Número de comensales") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Incluir propina?")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = checksw,
                    onCheckedChange = { checksw = it }
                )
            }

            if (checksw) {
                Slider(
                    value = sliderposition,
                    onValueChange = { sliderposition = it },
                    steps = 4,
                    valueRange = 0f..5f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val cantidad = textcan.toDoubleOrNull() ?: 0.0
                    val comensales = textcom.toIntOrNull() ?: 1

                    val propina = if (checksw) {
                        calcular_propina(cantidad, sliderposition)
                    } else {
                        0.0
                    }

                    val totalCuenta = cantidad + propina
                    val totalPorComensal = if (comensales > 0) totalCuenta / comensales else 0.0

                    propinaCalculada = propina
                    totalPorPersona = totalPorComensal
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CALCULAR")
            }

            Spacer(Modifier.height(24.dp))

            propinaCalculada?.let { propina ->
                totalPorPersona?.let { total ->
                    Text("Propina a añadir: ${"%.2f".format(propina)} €")
                    Text(
                        text = "Total por persona: ${"%.2f".format(total)} €",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

fun calcular_propina(cantidad: Double, sliderpos: Float): Double {
    return when (sliderpos.roundToInt()) {
        1 -> cantidad * 0.05
        2 -> cantidad * 0.10
        3 -> cantidad * 0.15
        4 -> cantidad * 0.20
        5 -> cantidad * 0.25
        else -> 0.0
    }
}
