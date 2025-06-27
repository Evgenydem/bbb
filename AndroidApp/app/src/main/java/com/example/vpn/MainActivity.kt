package com.example.vpn

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.net.URL

class MainActivity : ComponentActivity() {

    private val viewModel: VpnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hostSpinner: Spinner = findViewById(R.id.hostSpinner)
        val connectButton: Button = findViewById(R.id.connectButton)
        val pathInput: EditText = findViewById(R.id.pathInput)
        val statusText: TextView = findViewById(R.id.statusText)

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.loadHosts()
            launch(Dispatchers.Main) {
                hostSpinner.adapter = ArrayAdapter(this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    viewModel.hostNames)
            }
        }

        connectButton.setOnClickListener {
            val selected = hostSpinner.selectedItemPosition
            val ovpnPath = pathInput.text.toString().ifEmpty {
                null
            }
            lifecycleScope.launch(Dispatchers.IO) {
                val result = viewModel.connect(selected, ovpnPath)
                launch(Dispatchers.Main) {
                    statusText.text = result
                }
            }
        }
    }
}
