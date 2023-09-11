Cordova Kiosk Mode
==================

A Cordova plugin to create a Cordova application with "kiosk mode".
An app with this plugin can be set as an Android launcher.
If the app starts as a launcher, it blocks hardware buttons and statusbar,
so an user cannot close the app until the app request it.

**This plugin does not change behavior of application until it is set as launcher - home screen of the device.**

Escape from the app is possible only using javascript call `KioskPlugin.exitKiosk()`
or by uninstalling the app using `adb`. (Keeping USB debug allowed necessary.)
If the application starts as usual (not as a launcher), no restrictions are applied.

* Original plugin website: https://github.com/hkalina/cordova-plugin-kiosk
* this plugin website: https://github.com/sandyclock/capacitor-kiosk
* Example app: https://github.com/hkalina/cordova-kiosk-demo

This plugin is for Android platform only. For kiosk on iOS platform check its Guided Access feature.

This version extends the original version with new features, and also add support for capacitor.

1. Add kiosk mode for android. In the kiosk mode it will hide the title bar and task bar. When combined with launch mode, it works similar to single apple mode;

2. Various feature detection functions are added to detect if the desired configuration is set (for example, if the app is set as a launcher);

3. Add functions to open various Android settings to help users set up kiosk mode related features.

IMPORTANT NOTE
--------------
Support for the cordova is via cordova/capacitor migration. It, however, does not use native capacitor support. The end function of this cordova/capacitor is similar to a pure capacitor implementation.

About
-----

By adding this Cordova plugin the Cordova app becomes a homescreen (also known as a launcher) of Android device and should block any attempt of user to leave it.

To add plugin into existing capacitor application use:

    npm install capacitor-kiosk

Add the following to your app's manifest

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.stripe.example.app">
      <activity
            android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|smallestScreenSize|screenLayout|uiMode"
        android:name="jk.cordova.plugin.kiosk.KioskActivity"
        android:keepScreenOn="true"
        android:theme="@style/AppTheme.NoActionBar"
        android:label="@string/title_activity_main"
            android:launchMode="singleTask"
            android:exported="true">
        <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.HOME" />
        </intent-filter>

        <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>

      </activity>

</manifest>
```


To has it working, user have to **set this application as launcher** (see below) and start it by pressing Home button or by restarting the device.

**WARNING** Before installation ensure you have USB debug mode enabled. Without USB debug enabled you can get stuck in broken kiosk application.

Short API description
---------------------

**Exiting** from Kiosk mode using Javascript in the page (for hidden/authenticated button to escape the kiosk application):

    KioskPlugin.exitKiosk();

**Detecting** whether the app is successfly running in kiosk mode (kiosk activity is opened):

    KioskPlugin.isInKiosk(function(isInKiosk){ ... });

**Detecting** whether the app (kiosk activity) is set as launcher:

    KioskPlugin.isSetAsLauncher(function(isLauncher){ ... });

The device is effectively locked only when both methods returns `true`. When the app is "in kiosk", but not set as a launcher, user can escape the app by pressing a Home button (but other buttons are still locked).

**Defining allowed buttons** - buttons whose event propagation should not be prevented - so you can for example allow setting volume up/down:

    KioskPlugin.setAllowedKeys([ 24, 25 ]); // KEYCODE_VOLUME_UP, KEYCODE_VOLUME_DOWN

For list of keycode values check KeyEvent reference: https://developer.android.com/reference/android/view/KeyEvent#KEYCODE_0

For complete example application check: https://github.com/hkalina/cordova-kiosk-demo

Tips
----

* **To remove this application use `adb`:** (Do not install it without USB debug mode enabled!) (com.example.hello replace with package of your app defined in your config.xml)

        $ANDROID_HOME/platform-tools/adb uninstall com.example.hello

* **To change launcher (reset setting which launcher is default):**
 * **Alcatel:** Settings - Applications - All - (This Application) / Launcher - Clear defaults, after Home press will be asked for default to set
 * **Xiaomi:** Settings - Installed apps - Defaults - Launcher

* **To disable screenlock: ("slide to unlock")**
 * **Alcatel:** Settings - Security - Set up screen lock - None
 * **Xiaomi:** Settings - Additional settings - Developer options - Skip screen lock

**"Application Error - The connection to the server was unsuccessful. (file:///android_asset/www/index.html)" occurred**

* One reason can be too long loading of `index.html` - you can set timeout of Cordova's WebView in `config.xml` of application: (value is in milliseconds)

        <preference name="loadUrlTimeoutValue" value="60000" />

