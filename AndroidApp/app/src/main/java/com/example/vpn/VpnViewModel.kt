package com.example.vpn

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.URL

class VpnViewModel : ViewModel() {
    val hostNames = mutableListOf<String>()
    private val hostMap = mutableMapOf<String, String>()

    suspend fun loadHosts() {
        withContext(Dispatchers.IO) {
            try {
                val text = URL("https://spreadsheets.google.com/feeds/list/YOUR_SHEET_ID/1/public/values?alt=json").readText()
                val json = JSONObject(text)
                val entries = json.getJSONObject("feed").getJSONArray("entry")
                for (i in 0 until entries.length()) {
                    val entry = entries.getJSONObject(i)
                    val host = entry.getJSONObject("gsx$hostname").getString("\$t")
                    val ip = entry.getJSONObject("gsx$ip").getString("\$t")
                    hostNames.add(host)
                    hostMap[host] = ip
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun connect(index: Int, path: String?): String {
        if (index < 0 || index >= hostNames.size) {
            return "Invalid host index"
        }
        val host = hostNames[index]
        val ip = hostMap[host] ?: return "Unknown host"
        val configFile = path?.let { File(it) } ?: return "OVPN path required"
        if (!configFile.exists()) {
            return "Config not found"
        }
        val lines = configFile.readLines().filterNot { it.trim().startsWith("remote ", ignoreCase = true) }
        val newConfig = (listOf("remote $ip 1194") + lines).joinToString("\n")

        val tempFile = File.createTempFile("temp", ".ovpn")
        tempFile.writeText(newConfig)

        // This uses OpenVPN for Android via implicit intents
        val intent = VpnUtils.buildOpenVpnIntent(tempFile.path, host)
        VpnUtils.context.startActivity(intent)
        return "Connecting to $host"
    }
}
