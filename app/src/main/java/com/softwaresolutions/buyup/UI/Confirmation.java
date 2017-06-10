package com.softwaresolutions.buyup.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.softwaresolutions.buyup.R;

import Constants.Prefconstants;

public class Confirmation extends AppCompatActivity  {

    private EditText FIRSTCHAR;
    private EditText SECONDCHAR;
    private EditText THIRDCHAR;
    private Boolean  FIRSTCHECK;
    private Boolean  SECONDCHECK;
    private Boolean  THIRDCHECK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Bundle Extras=getIntent().getExtras();
        String OTP=Extras.getString("OTP");
        final String EMAIL=Extras.getString("Email");
        final String MOBILE=Extras.getString("MobileNumber");

        Snackbar.make(findViewById(R.id.CONFIRMATION_ROOT),"Enter the OTP received",Snackbar.LENGTH_INDEFINITE).show();

        FIRSTCHECK=false;
        SECONDCHECK=false;
        THIRDCHECK=false;

        final String RECEIVED_FIRST=Character.toString(OTP.charAt(0));

        final String RECEIVED_SECOND=Character.toString(OTP.charAt(1));

        final String RECEIVED_THIRD=Character.toString(OTP.charAt(2));


        FIRSTCHAR=(EditText) findViewById(R.id.FIRST_DIGIT);
        SECONDCHAR=(EditText) findViewById(R.id.SECOND_DIGIT);
        THIRDCHAR=(EditText) findViewById(R.id.THIRD_DIGIT);


        FIRSTCHAR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                {
                    Log.v("Checking1",s.toString()+":"+RECEIVED_FIRST);
                    if(s.toString().equals(RECEIVED_FIRST))
                        FIRSTCHECK=true;
                    SECONDCHAR.requestFocus();
                }

            }
        });

        SECONDCHAR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                {


                    Log.v("Checking2",s.toString()+":"+RECEIVED_SECOND);
                    if(s.toString().equals(RECEIVED_SECOND))
                        SECONDCHECK=true;
                   THIRDCHAR.requestFocus();
                }

            }
        });
        THIRDCHAR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



                Log.v("Checking3",s.toString()+":"+RECEIVED_THIRD);
                if(s.toString().equals(RECEIVED_THIRD))
                    THIRDCHECK=true;

                Log.v("1st,2nd,3rd",FIRSTCHECK+""+SECONDCHECK+THIRDCHECK);

                try {
                    if ((FIRSTCHECK ==true) && (SECONDCHECK ==true) && (THIRDCHECK==true)) {
                        SharedPreferences.Editor editor = getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME, MODE_PRIVATE).edit();
                        editor.putString(Prefconstants.USER_NAME,EMAIL).commit();
                        editor.putString(Prefconstants.MOBILE_USER,MOBILE).commit();

                        Intent HOME = new Intent(Confirmation.this, HomePage.class);
                        finish();
                        startActivity(HOME);

                    }
                    else
                    {
                        Snackbar.make(findViewById(R.id.CONFIRMATION_ROOT),"Entered OTP doesnot match",Snackbar.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Log.v("Failed",e.getLocalizedMessage()+"...");
                }

            }
        });
    }


}
