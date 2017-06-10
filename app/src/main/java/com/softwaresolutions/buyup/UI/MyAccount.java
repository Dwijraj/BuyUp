package com.softwaresolutions.buyup.UI;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.softwaresolutions.buyup.R;

import java.util.Timer;
import java.util.TimerTask;

import Constants.Prefconstants;

public class MyAccount extends AppCompatActivity {

    private CardView cardView;
    private EditText Mobile;
    private EditText Email;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;

        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

       final ProgressDialog progressDialog=new ProgressDialog(this);
        Mobile=(EditText) findViewById(R.id.MY_ACCOUNT_MOBILE_NUMER);
        Email=(EditText)  findViewById(R.id.MY_ACCOUNT_EMAIL_ID);
        cardView=(CardView) findViewById(R.id.MY_ACCOUNT_CARD_VIEW);

       final SharedPreferences Details = getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME, MODE_PRIVATE);

         String EmailID=Details.getString(Prefconstants.USER_NAME,"DEFAULT");
         String MobileNumber=Details.getString(Prefconstants.MOBILE_USER,"DEFAULT");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);



        Mobile.setText(MobileNumber);
        Email.setText(EmailID);

        (findViewById(R.id.BUTTON)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailID=Details.getString(Prefconstants.USER_NAME,"DEFAULT");
                String MobileNumber=Details.getString(Prefconstants.MOBILE_USER,"DEFAULT");


                if(Email.getText().toString().isEmpty()==false&&Mobile.getText().toString().isEmpty()==false&&
                        TextUtils.isDigitsOnly(Mobile.getText().toString())
                        &&Mobile.getText().toString().length()==10
                        &&(!Mobile.getText().toString().equals(MobileNumber)
                        ||!(Email.getText().toString().equals(EmailID))))
                {
                    progressDialog.setMessage("Updating your details");
                    progressDialog.show();
                    cardView.setCardElevation(5);

                    SharedPreferences.Editor editor = getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME, MODE_PRIVATE).edit();
                    editor.putString(Prefconstants.USER_NAME,Email.getText().toString()).commit();
                    editor.putString(Prefconstants.MOBILE_USER,Mobile.getText().toString()).commit();

                    Mobile.setText(Mobile.getText().toString());
                    Email.setText(Email.getText().toString());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            cardView.setCardElevation(10);
                            Toast.makeText(getApplicationContext(),"Changes Updated",Toast.LENGTH_LONG).show();

                        }
                    },3000);


                }
                else if((Mobile.getText().toString().equals(MobileNumber)
                        &&Email.getText().toString().equals(EmailID)))
                {
                    Toast.makeText(getApplicationContext(),"Please enter the new details",Toast.LENGTH_LONG).show();
                }
                else if (MobileNumber.length()!=10 || !TextUtils.isDigitsOnly(Mobile.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Mobile Number ",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"EmailID and Mobile number cannot be blank ",Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}
