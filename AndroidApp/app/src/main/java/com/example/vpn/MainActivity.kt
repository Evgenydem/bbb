package com.example.vpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: VpnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VpnUtils.init(this)
        setContent {
            VpnApp(viewModel)
        }
    }
}

@Composable
fun VpnApp(viewModel: VpnViewModel = viewModel()) {
    val hostNames by viewModel.hostNames.collectAsState()
    var selectedIndex by remember { mutableStateOf(0) }
    var ovpnPath by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadHosts()
    }

    MaterialTheme {
        Column(Modifier.padding(16.dp)) {
            Box {
                TextField(
                    value = hostNames.getOrNull(selectedIndex) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    label = { Text("Host") }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    hostNames.forEachIndexed { index, name ->
                        DropdownMenuItem(text = { Text(name) }, onClick = {
                            selectedIndex = index
                            expanded = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = ovpnPath,
                onValueChange = { ovpnPath = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Path to .ovpn") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                scope.launch {
                    status = viewModel.connect(selectedIndex, ovpnPath)
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Connect")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(status)
        }
    }
}
