package com.example.vpn

import android.content.Context
import android.content.Intent
import java.io.File
import net.openvpn.openvpn.OpenVpnApi

object VpnUtils {
    lateinit var context: Context

    fun init(ctx: Context) {
        context = ctx.applicationContext
    }

    fun buildOpenVpnIntent(configPath: String, profileName: String): Intent {
        val config = File(configPath).readText()
        val intent = Intent(OpenVpnApi.API_ENDPOINT)
        intent.putExtra(OpenVpnApi.API_PROFILE_NAME, profileName)
        intent.putExtra(OpenVpnApi.API_PROFILE_CONTENT, config)
        intent.putExtra(OpenVpnApi.API_PROFILE_PW_SAVE, OpenVpnApi.PW_SAVE_PASSWORD)
        return intent
    }
}
