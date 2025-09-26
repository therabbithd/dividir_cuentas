package com.example.dividir_cuentas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.tooling.preview.Preview
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
    var textcan by remember { mutableStateOf("") }
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
                label = { Text("Cantidad") }
            )
            OutlinedTextField(
                value = textcom,
                onValueChange = { newValue -> textcom = newValue },
                label = { Text("Comensales") }
            )
            Text("Redondear")
            Switch(
                checked = checksw,
                onCheckedChange = {
                    checksw = it
                }
            )
            Text("Valoracion")
            Slider(
                value = sliderposition,
                onValueChange = {
                    sliderposition = it
                },
                steps = 5,
                valueRange = (0f .. 5f)
            )
            if(checksw){
                Text("Propina: "+ calcular_propina(textcan,sliderposition))
            }

            
        }

    }
}
fun calcular_propina(textcan:String,sliderpos:Float):Int{
    val can = textcan.toInt()
    var prop = when(sliderpos){
        0f -> 0
        1f -> can * 0.05
        2f -> can * 0.1
        3f -> can * 0.15
        4f -> can * 0.2
        5f -> can * 0.25
        else -> 0
    }
    return prop.toInt()
}