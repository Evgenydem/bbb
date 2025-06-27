# VPN Launcher Android App

This repository contains a minimal Android application implementing the logic of the original Python script. The application loads hostnames and IP addresses from Google Sheets, allows the user to choose a host and select a `.ovpn` file, then launches OpenVPN on Android.

Windows specific checks were removed. If the `.ovpn` file path is not provided or the file does not exist, the user is prompted to specify the location manually.

## Structure
- `AndroidApp/` – Gradle based Android project.
  - `app/src/main/java/com/example/vpn/` – Kotlin sources.
  - `app/src/main/res/layout/` – UI layout.
  - `build.gradle` – Gradle configuration.

## Building
Open the `AndroidApp` directory in Android Studio and build the application as usual.

