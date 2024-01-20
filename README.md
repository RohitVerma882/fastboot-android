# Fastboot Android
[![](https://jitpack.io/v/RohitVerma882/fastboot-android.svg)](https://jitpack.io/#RohitVerma882/fastboot-android)

Android library for sending fastboot commands from an Android device to a device running fastboot.

***Only supports fastboot over USB On-The-Go (OTG) connections.***

Original-Source: https://github.com/google/fastboot-mobile

## Integration
Add it in your root build.gradle at the end of repositories:
```gradle
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency
```gradle
dependencies {
    implementation 'com.github.RohitVerma882:fastboot-android:<version>'
}
```

## Usage
### List Attached Fastboot Devices
```kotlin
// Includes connected devices.
val deviceIds : List<DeviceId> = FastbootDeviceManager.getAttachedDeviceIds()
```

### List Connected Fastboot Devices
```kotlin
val deviceIds : List<DeviceId> = FastbootDeviceManager.getConnectedDeviceIds()
```

### Connect to a Fastboot Device
```kotlin
// typealias DeviceId = String
FastbootDeviceManager.addFastbootDeviceManagerListener(
    object : FastbootDeviceManagerListener() {
        override fun onFastbootDeviceAttached(deviceId: DeviceId) {
            Log.d("Device attached: $deviceId")
        }

        override fun onFastbootDeviceDetached(deviceId: DeviceId) {
            Log.d("Device detached: $deviceId")
        }

        override fun onFastbootDeviceConnected(deviceId: DeviceId, deviceContext: FastbootDeviceContext) {
            // Do some fastboot stuff...
            val response = deviceContext.sendCommand(FastbootCommand.getVar("current-slot"))
            val bootSlot = response.data
            Log.d("Device $deviceId with slot $bootSlot.")
        }
    })

FastbootDeviceManager.connectToDevice(/* Device Name */ "/dev/bus/usb/001/002")
```
