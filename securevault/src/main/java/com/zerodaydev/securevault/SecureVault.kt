package com.zerodaydev.securevault

import android.content.Context

object SecureVault {

    private var initialized = false

    fun init(context: Context, keysToStore: Map<String, String>) {
        if (!initialized) {
            EncryptedStorage.init(context)
            storeIfMissing(context, keysToStore)
            initialized = true
        }
    }

    fun getDecryptedKey(context: Context, keyLabel: String): String? {
        return EncryptedStorage.getDecryptedKey(context, keyLabel)
    }

    private fun storeIfMissing(context: Context, map: Map<String, String>) {
        val prefs = context.getSharedPreferences("encrypted_api_keys", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        map.forEach { (label, value) ->
            if (!prefs.contains(label)) {
                val encrypted = EncryptedStorage.encrypt(value, label)
                editor.putString(label, encrypted)
            }
        }

        editor.apply()
    }
}
