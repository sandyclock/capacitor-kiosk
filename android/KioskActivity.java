package jk.cordova.plugin.kiosk;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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


public class KioskActivity extends BridgeActivity {

    public static volatile KioskActivity running = null;

    public static volatile  boolean kioskMode = false;
    public static volatile Set<Integer> allowedKeys = Collections.EMPTY_SET;

    private StatusBarOverlay statusBarOverlay = null;

    public void enterKioskMode(){
      if (!kioskMode){
        kioskMode = true;
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


        System.out.println("getActionBar:2:**********");

        System.out.println(String.valueOf(_actionBar));

        if (_actionBar != null) {
          _actionBar.hide();
        }
      }
    }

  public void leaveKioskMode(){
    if (kioskMode){
      kioskMode = false;
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


      System.out.println("getActionBar:2:**********");

      System.out.println(String.valueOf(_actionBar));

      if (_actionBar != null) {
        _actionBar.show();
      }
    }
  }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("KioskActivity started");
        running = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("KioskActivity stopped");
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
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
    }     
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("onKeyDown event: keyCode = " + event.getKeyCode());
        return ! allowedKeys.contains(event.getKeyCode()); // prevent event from being propagated if not allowed
    }
    
    @Override
    public void finish() {
        System.out.println("Never finish...");
        // super.finish();
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


            if (KioskActivity.kioskMode) {
              ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
            }

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

