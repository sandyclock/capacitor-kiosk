package jk.cordova.plugin.kiosk;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import org.apache.cordova.*;
import android.widget.*;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;

import com.getcapacitor.Logger;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import jk.cordova.plugin.kiosk.KioskActivity;
import org.json.JSONObject;
import java.lang.Integer;
import java.util.HashSet;

public class KioskPlugin extends CordovaPlugin {
    
    public static final String EXIT_KIOSK = "exitKiosk";
    public static final String IS_IN_KIOSK = "isInKiosk";
    public static final String IS_SET_AS_LAUNCHER = "isSetAsLauncher";
    public static final String SET_ALLOWED_KEYS = "setAllowedKeys";

    public static final String ENTER_KIOSK = "enterKiosk";
    public static final String LEAVE_KIOSK = "leaveKiosk";
    public static final String SELECT_LAUNCHER = "selectLauncher";

    public static final String IS_IN_KIOSK_MODE = "isInKioskMode";

  public static final String IS_DEVICE_SECURE = "isDeviceSecure";

  public static final String OPEN_LOCK_SCREEN_SETTING = "openLockScreenSetting";

  /*
   * Snippet adopted from cordova secure storage plugin
   * https://github.com/mibrito707/cordova-plugin-secure-storage-echo/blob/master/src/android/SecureStorage.java
   *
   * @tanli 9/6/23
   */
  private boolean _isDeviceSecure(Context context) {
    KeyguardManager keyguardManager = (KeyguardManager) (context.getSystemService(Context.KEYGUARD_SERVICE));
    try {
      Method isSecure = null;
      isSecure = keyguardManager.getClass().getMethod("isDeviceSecure");
      return ((Boolean) isSecure.invoke(keyguardManager)).booleanValue();
    } catch (Exception e) {
      return keyguardManager.isKeyguardSecure();
    }
  }


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (IS_IN_KIOSK.equals(action)) {

              callbackContext.success(Boolean.toString(KioskActivity.running != null));
              return true;
            }
            else if (SELECT_LAUNCHER.equals(action)){
              if (KioskActivity.running!=null){
                KioskActivity.running.selectLauncher();
              }
              callbackContext.success();
              return true;
            }
            else if (IS_IN_KIOSK_MODE.equals(action)) {
                callbackContext.success(Boolean.toString(KioskActivity.running!=null && KioskActivity.kioskMode));
                return true;

            }
            else if (IS_SET_AS_LAUNCHER.equals(action)) {

                String myPackage = cordova.getActivity().getApplicationContext().getPackageName();
                callbackContext.success(Boolean.toString(myPackage.equals(findLauncherPackageName())));
                return true;

            }
            else if (IS_DEVICE_SECURE.equals(action)) {

              Context context = cordova.getActivity().getApplicationContext();

              callbackContext.success(Boolean.toString(this._isDeviceSecure(context)));
              return true;

            }
            else if (OPEN_LOCK_SCREEN_SETTING.equals(action)) {
              if (KioskActivity.running != null) {
                KioskActivity.running.openLockScreenSetting();
              }
              callbackContext.success();
              return true;

            }
            else if (ENTER_KIOSK.equals(action)) {
              Logger.info("***************** arg passed in:");
              Logger.info(String.valueOf(args));

              Logger.info("***************** arg passed in:");
              Logger.info(String.valueOf(args));
              Boolean flagOnly=null;
              if (args!=null && args.length()>0){
                Logger.info("first arg extracted is:");
                Logger.info(String.valueOf(args.getBoolean(0)));
                flagOnly = args.getBoolean(0);
              }



              if (KioskActivity.running != null) {

                  KioskActivity.running.enterKioskMode(flagOnly);
//                KioskActivity.running.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                KioskActivity.running.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                // https://github.com/hkalina/cordova-plugin-kiosk/issues/14
//                View decorView = KioskActivity.running.getWindow().getDecorView();
//                // Hide the status bar.
//                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
              }
              callbackContext.success();
              return true;

            }
            else if (LEAVE_KIOSK.equals(action)){

              Logger.info("***************** arg passed in:");
              Logger.info(String.valueOf(args));
              Boolean flagOnly=null;
              if (args!=null && args.length()>0){
                Logger.info("first arg extracted is:");
                Logger.info(String.valueOf(args.getBoolean(0)));
                flagOnly = args.getBoolean(0);
              }

              if (KioskActivity.running!=null) {

                  KioskActivity.running.leaveKioskMode(flagOnly);
//                KioskActivity.running.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                KioskActivity.running.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                // https://github.com/hkalina/cordova-plugin-kiosk/issues/14
//                View decorView = KioskActivity.running.getWindow().getDecorView();
//                // Hide the status bar.
//                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
              }
              callbackContext.success();
              return true;

            }
            else if (EXIT_KIOSK.equals(action)) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                Intent chooser = Intent.createChooser(intent, "Select destination...");
                if (intent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
                    cordova.getActivity().startActivity(chooser);
                }
                
                callbackContext.success();
                return true;
                
            } else if (SET_ALLOWED_KEYS.equals(action)) {
                
                System.out.println("setAllowedKeys: " + args.toString());
                HashSet<Integer> allowedKeys = new HashSet<Integer>();
                for (int i = 0; i < args.length(); i++) {
                    allowedKeys.add(args.optInt(i));
                }
                KioskActivity.allowedKeys = allowedKeys;
                
                callbackContext.success();
                return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }
    
    private String findLauncherPackageName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = this.cordova.getActivity().getPackageManager().resolveActivity(intent, 0);
        return res.activityInfo.packageName;
    }
}

