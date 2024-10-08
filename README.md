# 

##### Email 
suporte@codemaker.pt
## Installation
`yarn add @codemakerpt/react-native-ttlock`
## Add configuration to project
#### iOS
1. `cd ./ios && pod install && cd ../`
2. In XCode  `TARGETS` ➜ `info` ➜ add key `Privacy - Bluetooth Peripheral Usage Description` value `your description for bluetooth` and key `Privacy - Bluetooth Always Usage Description` value `your description for bluetooth`
#### Android
1. AndroidManifest.xml configuration:   
(1) Add 'xmlns:tools="http://schemas.android.com/tools"' to element   
(2) Add 'tools:replace="android:label"' to element   
(3) Additional permissions:  
``` 
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
android 12 needs below permission.
  <!--
 Needed only if your app looks for Bluetooth devices.
         You must add an attribute to this permission, or declare the
         ACCESS_FINE_LOCATION permission, depending on the results when you
         check location usage in your app.
    -->
    <uses-permission
        android:name="android.permission.BLUETOOTH_SCAN"
        android:usesPermissionFlags="neverForLocation"
        tools:targetApi="s" />
    <!--
 Needed only if your app makes the device discoverable to Bluetooth
         devices.
    -->
    <uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />
    <!--
 Needed only if your app communicates with already-paired Bluetooth
         devices.
    -->
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```
2. In order to get the permission request result in ttlock plugin, in MainActivity extends ReactActivity, you need override the onRequestPermissionsResult method and add below code:   
(1) java code:
``` 
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    ReactInstanceManager mReactInstanceManager = getReactNativeHost().getReactInstanceManager();
    TtlockModule ttlockModule = mReactInstanceManager.getCurrentReactContext().getNativeModule(TtlockModule.class);
    ttlockModule.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
```
3. When you release the apk, you need disable proguard in release builds.Config buildTypes in build.gradle like this:
``` 
repositories {
    buildTypes {
        release {
            minifyEnabled false
            shrinkResources false
        }
    }
}
```
4. Support min sdk version is 18
``` 
defaultConfig {
        minSdkVersion 18
    }
```
5. On Settings.Gradle
``` 
include ':react-native-ttlock'
project(':react-native-ttlock').projectDir = new File(rootProject.projectDir, '../node_modules/@codemakerpt/react-native-ttlock/android')
```
5. On Build.Gradle (App)
``` 
dependencies {
    ...REST OF YOUR DEPENDENCIES

    implementation project(":react-native-ttlock")
}
```