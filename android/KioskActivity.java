package jk.cordova.plugin.kiosk;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import org.apache.cordova.*;
import android.widget.*;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import java.lang.Integer;
import java.util.Collections;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Logger;


public class KioskActivity extends BridgeActivity {

    public static volatile KioskActivity running = null;

    public static volatile  boolean kioskMode = false;
    public static volatile Set<Integer> allowedKeys = Collections.EMPTY_SET;

    private StatusBarOverlay statusBarOverlay = null;

  private boolean isInKioskMode() {
    ActivityManager activityManager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    if (Build.VERSION.SDK_INT < 23) {
      return activityManager.isInLockTaskMode();
    } else {
//      Logger.i(TAG, "Task Mode: " + Integer.toString(activityManager.getLockTaskModeState()));
      return activityManager.getLockTaskModeState() > ActivityManager.LOCK_TASK_MODE_NONE;
    }
  }


    public void enterKioskMode(Boolean flagOnly){

        this.runOnUiThread(
          ()-> {
            if (!kioskMode) {
              kioskMode = true;
            }

            if (Boolean.TRUE.equals(flagOnly)){
              return;
            }

        // https://github.com/apache/cordova-plugin-statusbar/blob/master/src/android/StatusBar.java
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // https://github.com/hkalina/cordova-plugin-kiosk/issues/14
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        androidx.appcompat.app.ActionBar _actionBar = getSupportActionBar();


//        System.out.println("getActionBar:2:**********");
//
//        System.out.println(String.valueOf(_actionBar));

        if (_actionBar != null) {
          _actionBar.hide();
        }

        if (!isInKioskMode()){
          this.startLockTask();
        }

          }
        );

    }

  /*
   * https://stackoverflow.com/questions/53778920/android-open-lock-screen-settings-programmatically
   */
  public void openLockScreenSetting(){
      this.runOnUiThread(
        ()-> {
          if (!kioskMode) {
            kioskMode = true;
          }
//          Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
          Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
          startActivity(intent);
      }
      );

    }

  /*
   * This is based on the link below,
   *   https://stackoverflow.com/questions/27991656/how-to-set-default-app-launcher-programmatically
   *
   * The basic idea is to create a fake component and declare it as disabled, and then enable it to force the system to show the launcher selector screen even if the user already made a previous selection,
   * because the default launch list has been changed.
   *
   * We made two important tweets as documented in the code below.
   *
   * @tanli 9/5/23
   */
  public void selectLauncher() {

    this.runOnUiThread(
      () -> {
        /*
         * We disable kioskMode because otherwise, when the system launcher chooser appears, the current app will be in sleep mode, and if the kiosk mode is on,
         * it will pull back the current app, and hence dismiss the chooser without letting the user to choose.
         */
        if (kioskMode){
          kioskMode = false;
        }

        Context context = this.getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, FakeLauncherActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        /*
         * We ask the system to remove the current instanceof the app after selection, so it will start a new thread, and if the user needs to re-select launcher,
         * the launcher chooser will still appear (otherwise, it will hide because the user already made a selection for the current thread)
         */
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.SYNCHRONOUS);

      });
  }

  public void leaveKioskMode(Boolean flagOnly){
      this.runOnUiThread(
        ()-> {
          if (kioskMode) {
            kioskMode = false;
          }

          if (Boolean.TRUE.equals(flagOnly)){

            return;
          }

      // https://github.com/apache/cordova-plugin-statusbar/blob/master/src/android/StatusBar.java
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

      // https://github.com/hkalina/cordova-plugin-kiosk/issues/14
      View decorView = getWindow().getDecorView();
      // Hide the status bar.
      decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

      // decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
      // Remember that you should never show the action bar if the
      // status bar is hidden, so hide that too if necessary.
      ActionBar actionBar = getActionBar();
      if (actionBar != null) actionBar.show();

      androidx.appcompat.app.ActionBar _actionBar = getSupportActionBar();


//      System.out.println("getActionBar:2:**********");
//
//      System.out.println(String.valueOf(_actionBar));

      if (_actionBar != null) {
        _actionBar.show();
      }

      if (this.isInKioskMode()){
        this.stopLockTask();
      }
    }
      );

  }

    @Override
    public void onStart() {
        super.onStart();
        Logger.info("KioskActivity started");
        running = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.info("KioskActivity stopped");
        running = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // super.init();

        if (running!=null) {
            finish(); // prevent more instances of kiosk activity
        }

        // loadUrl(launchUrl);

//        // https://github.com/apache/cordova-plugin-statusbar/blob/master/src/android/StatusBar.java
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        // https://github.com/hkalina/cordova-plugin-kiosk/issues/14
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//        // decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        if (actionBar != null) actionBar.hide();
//
//      androidx.appcompat.app.ActionBar _actionBar = getSupportActionBar();
//
//
//      System.out.println("getActionBar:2:**********");
//
//      System.out.println(String.valueOf(_actionBar));
//
//      if (_actionBar != null) {
//        _actionBar.hide();
//      }


        // add overlay to prevent statusbar access by swiping
        // statusBarOverlay = StatusBarOverlay.createOrObtainPermission(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (statusBarOverlay != null) {
            statusBarOverlay.destroy(this);
            statusBarOverlay = null;
        }
    }

    @Override
    public void onPause() {
            super.onPause();
            this.moveTaskToFront();
//            if (kioskMode) {
//              ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//              activityManager.moveTaskToFront(getTaskId(), 0);
//            }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("onKeyDown event: keyCode = " + event.getKeyCode());
        return ! allowedKeys.contains(event.getKeyCode()); // prevent event from being propagated if not allowed
    }

    @Override
    public void finish() {
        Logger.info("Never finish...");
        // super.finish();
    }

    private void moveTaskToFront() {
      Logger.info("**********move task to front *********");
      if (kioskMode) {
//        System.out.println("move:check:1");

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//              am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
//        System.out.println("move:check:2");
//        System.out.println(getTaskId());

        am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);

      }
    }

    // http://www.andreas-schrade.de/2015/02/16/android-tutorial-how-to-create-a-kiosk-mode-in-android/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            System.out.println("Focus lost - closing system dialogs");

            /*
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
            */
            this.moveTaskToFront();
//            if (kioskMode) {
//              ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
////              am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
//              am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
//
//            }

            // sometime required to close opened notification area
            // Timer timer = new Timer();
            // timer.schedule(new TimerTask(){
            //     public void run() {
            //         Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            //         sendBroadcast(closeDialog);
            //     }
            // }, 500); // 0.5 second
        }
    }
}

