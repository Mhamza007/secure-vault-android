package com.zerodaydev.securevault

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.aead.AeadKeyTemplates

object EncryptedStorage {

    private const val KEYSET_NAME = "secure_keyset"
    private const val PREF_FILE = "secure_keyset_prefs"
    private const val KEY_TEMPLATE = "AES256_GCM"

    private lateinit var aead: Aead

    fun init(context: Context) {
        AeadConfig.register() // Register Tink configuration

        val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withSharedPref(context, KEYSET_NAME, PREF_FILE)
            .withMasterKeyUri("android-keystore://$KEYSET_NAME")
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    fun encrypt(plainText: String, associatedData: String = ""): String {
        check(::aead.isInitialized) { "EncryptedStorage.init() must be called first" }

        val ciphertext = aead.encrypt(
            plainText.toByteArray(),
            associatedData.toByteArray()
        )
        return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
    }

    fun decrypt(cipherText: String, associatedData: String = ""): String {
        check(::aead.isInitialized) { "EncryptedStorage.init() must be called first" }

        val decrypted = aead.decrypt(
            Base64.decode(cipherText, Base64.NO_WRAP),
            associatedData.toByteArray()
        )
        return String(decrypted)
    }

    fun getDecryptedKey(context: Context, keyLabel: String): String? {
        val encrypted = context
            .getSharedPreferences("encrypted_api_keys", Context.MODE_PRIVATE)
            .getString(keyLabel, null)

        return encrypted?.let {
            try {
                decrypt(it, keyLabel)
            } catch (_: Exception) {
                null // Return null if decryption fails
            }
        }
    }

}
