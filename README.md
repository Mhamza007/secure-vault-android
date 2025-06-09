# 🔐 secure-vault-android

[![](https://jitpack.io/v/Mhamza007/secure-vault-android.svg)](https://jitpack.io/#Mhamza007/secure-vault-android)

## 🔐 API Key Security with SecureVault

This app uses a reusable module (`:securevault`) that encrypts API keys at runtime using Tink + Android Keystore.

### 📁 Secret Management

- All keys are stored in `key.properties` (excluded from Git)
- For **debug builds**:
    - Keys are injected into `BuildConfig` for convenience
- For **release builds**:
    - Keys are injected into an encrypted vault at runtime
    - `build.gradle.kts` dynamically generates `secure-config.properties` during the build
    - This file is not committed and not able to reverse-engineer

### 🔐 Vault Features

- Uses `Tink` AES256-GCM with Android Keystore-backed keyset
- No raw keys exist in the APK
- All key access is via `SecureVault.getDecryptedKey(context, keyLabel)`

## 🚀 Installation

### 1. Add JitPack to your project

In your root `settings.gradle.kts` or `build.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}
```

### 2. Import the library

In your app `build.gradle.kts`:

```kotlin
implementation("com.github.Mhamza007:secure-vault-android:(lastest-version)")
```
