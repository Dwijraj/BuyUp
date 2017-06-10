package com.softwaresolutions.buyup.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.softwaresolutions.buyup.R;

import Constants.DefaultValues;
import Constants.Prefconstants;

public class Splashscreen extends AppCompatActivity {

    private ImageView SPLASH_IMAGE_VIEW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SPLASH_IMAGE_VIEW=(ImageView) findViewById(R.id.SPLASH_APP_ICON);

        int SPLASH_DISPLAY_LENGTH=5000;

        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                (findViewById(R.id.WELCOME_MESSAGE)).animate().alpha(1).setDuration(2500).setInterpolator(new LinearInterpolator()).start();

            }
        };

        SPLASH_IMAGE_VIEW.animate().alpha(1).withEndAction(runnable).setDuration(2500).setInterpolator(new LinearInterpolator()).start();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {




                SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.USER_NAME, DefaultValues.NO_USER);

                SharedPreferences LOGIN_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                String IS_FIRST_TIME=LOGIN_SHARED_PREF.getString(Prefconstants.IS_FIRST_TIME, DefaultValues.IS_FIRST_TIME);


                if(IS_FIRST_TIME.equals(DefaultValues.IS_FIRST_TIME))
                {
                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);

                    Intent TO_REDIRECT=new Intent(Splashscreen.this,Splashscreen.class);

                    PendingIntent PENDING_INTENT = PendingIntent.getActivity(Splashscreen.this, (int) System.currentTimeMillis(), TO_REDIRECT, 0);

                      Notification notification  = new Notification.Builder(Splashscreen.this)
                              .setContentText("Welcome to"+getResources().getString(R.string.app_name))
                              .setContentTitle(getResources().getString(R.string.app_name))
                              .setContentIntent(PENDING_INTENT)
                              .setSmallIcon(R.mipmap.ic_launcher_1)
                              .setAutoCancel(true)
                              .setStyle(new Notification.BigPictureStyle()
                                      .bigPicture(BitmapFactory.decodeResource(Splashscreen.this.getResources(),
                                              R.drawable.welcome_notification)).setBigContentTitle("Welcome to"+getResources().getString(R.string.app_name)))
                              .build();




                     notificationManager.notify(0, notification);

                    SharedPreferences.Editor EDIT_FIRST_LOGIN=LOGIN_SHARED_PREF.edit();
                    EDIT_FIRST_LOGIN.putString(Prefconstants.IS_FIRST_TIME,"FALSE").commit();

                }
                if(Logged_In_User.equals(DefaultValues.NO_USER)) {

                    Intent LOGIN=new Intent(Splashscreen.this,LoginActivity.class);
                    finish();
                    startActivity(LOGIN);
                }
                else {
                    Intent LOGIN = new Intent(Splashscreen.this, HomePage.class);
                    finish();
                    startActivity(LOGIN);
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}

