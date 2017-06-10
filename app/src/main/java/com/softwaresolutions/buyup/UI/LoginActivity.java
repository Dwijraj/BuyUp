package com.softwaresolutions.buyup.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softwaresolutions.buyup.R;

import java.util.Random;

import Constants.DefaultValues;
import Constants.GmailSender;
import Constants.Prefconstants;
import Utils.GMailSender;

public class LoginActivity extends AppCompatActivity {

    private EditText ENTERED_EMAIL;
    private EditText ENTERED_MOBILE;
    private Button   CONFIRM;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

        String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.USER_NAME, DefaultValues.NO_USER);

        if(!Logged_In_User.equals(DefaultValues.NO_USER)) {

         //   Intent LOGIN=new Intent(Splashscreen.this,LoginActivity.class);
            finish();
           // startActivity(LOGIN);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ENTERED_EMAIL=(EditText) findViewById(R.id.LOGIN_EMAIL_ID);
        ENTERED_MOBILE=(EditText) findViewById(R.id.LOGIN_MOBILE_NUMER);
        CONFIRM=(Button) findViewById(R.id.LOGIN_GET_STARTED);


        CONFIRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String EMAIL=ENTERED_EMAIL.getText().toString().trim();
                final String MOBILE=ENTERED_MOBILE.getText().toString().trim();

                if(TextUtils.isEmpty(EMAIL)|| TextUtils.isEmpty(MOBILE))
                {
                    Snackbar.make(findViewById(R.id.LOGIN_ROOT_VIEW),"Enter Credentials",Snackbar.LENGTH_SHORT).show();


                }
                else if((!TextUtils.isDigitsOnly(MOBILE))||MOBILE.length()!=10)
                {

                    Snackbar.make(findViewById(R.id.LOGIN_ROOT_VIEW),"Enter Valid Mobile number",Snackbar.LENGTH_SHORT).show();

                }
                else
                {

                    AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Email address :"+EMAIL+"\n"+"Mobile Number :"+MOBILE);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Random rn = new Random();
                                    int n = 999 - 99;
                                    int i = rn.nextInt() % n;
                                    if(i<0)
                                    {
                                        i*=-1;
                                    }
                                    int OTPint=  99 + i;
                                    String OTPstring=String.valueOf(OTPint);

                                    GMailSender sender = new GMailSender(GmailSender.EMAIL_SENDER
                                            , GmailSender.PASSWORD_SENDER);


                                    try {
                                        sender.sendMail("OTP from BuyUp", "One time password is " +
                                                        OTPstring, GmailSender.EMAIL_SENDER,
                                                EMAIL);
                                      //  progressDialog.dismiss();
                                        Log.v("GmailSending","Success");

                                        startActivity((new Intent(LoginActivity.this,Confirmation.class)).putExtra("OTP",OTPstring)
                                                .putExtra("Email",EMAIL)
                                                .putExtra("MobileNumber",MOBILE));



                                    } catch (Exception e) {

                                       // progressDialog.dismiss();
                                        Snackbar.make(findViewById(R.id.LOGIN_ROOT_VIEW),"Failed to send OTP",3000);
                                        Log.v("GmailSending",e.getLocalizedMessage());

                                    }

                                }
                            }).start();


                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog=builder.create();
                    dialog.show();

                }

            }
        });


    }
    public class SendEmail extends AsyncTask<String,Integer,Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected Integer doInBackground(String... params) {

            String str=params[0];
            return null;
        }
    }
}
