package com.example.dividir_cuentas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
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
import com.example.dividir_cuentas.ui.theme.DividirCuentasTheme


import kotlin.math.ceil
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DividirCuentasTheme {
                MyScreen()
            }
        }
    }
}

@Composable
fun MyScreen() {
    var textcan by remember { mutableStateOf("") }
    var textcom by remember { mutableStateOf("") }
    var incluirPropinaYRedondear by remember { mutableStateOf(false) }
    var tipPercentage by remember { mutableStateOf(10f) }
    var propina by remember { mutableStateOf(0.0) }
    var totalPorPersona by remember { mutableStateOf(0.0) }
    var totalCuenta by remember { mutableStateOf(0.0) }
    var showResults by remember { mutableStateOf(false) }


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
                onValueChange = {
                    textcan = it
                    showResults = false
                },
                label = { Text("Cantidad de la cuenta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = textcom,
                onValueChange = {
                    textcom = it
                    showResults = false
                },
                label = { Text("Número de comensales") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Incluir propina y redondear pago?")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = incluirPropinaYRedondear,
                    onCheckedChange = {
                        incluirPropinaYRedondear = it
                        showResults = false
                    }
                )
            }

            AnimatedVisibility(visible = incluirPropinaYRedondear) {
                Column {
                    Text("Porcentaje de propina: ${tipPercentage.roundToInt()}%")
                    Slider(
                        value = tipPercentage,
                        onValueChange = {
                            tipPercentage = it
                            showResults = false
                        },
                        valueRange = 0f..30f,
                        steps = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }


            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val cantidad = textcan.toDoubleOrNull() ?: 0.0
                    val comensales = textcom.toIntOrNull() ?: 1

                    if (comensales > 0) {
                        if (incluirPropinaYRedondear) {
                            val propinaSlider = cantidad * tipPercentage / 100
                            val cantidadConPropina = cantidad + propinaSlider

                            val totalPorPersonaSinRedondear = cantidadConPropina / comensales

                            val totalRedondeadoPorPersona = ceil(totalPorPersonaSinRedondear)

                            totalPorPersona = totalRedondeadoPorPersona
                            totalCuenta = totalRedondeadoPorPersona * comensales

                            propina = totalCuenta - cantidad

                        } else {
                            propina = 0.0
                            totalCuenta = cantidad
                            totalPorPersona = cantidad / comensales
                        }
                        showResults = true
                    } else {
                        showResults = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),

                ) {
                Text("CALCULAR")
            }

            Spacer(Modifier.height(24.dp))
            if (showResults) {
                val totalCuentaFormatted = String.format("%.2f", totalCuenta)
                val totalPorPersonaFormatted = String.format("%.2f", totalPorPersona)
                val propinaFormatted = String.format("%.2f", propina)

                Text(
                    "Total cuenta: $totalCuentaFormatted €",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "Total por persona: $totalPorPersonaFormatted €",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (propina > 0.009) {
                    Text(
                        "Propina incluida: $propinaFormatted €",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}