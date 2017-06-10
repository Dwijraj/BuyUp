package com.softwaresolutions.buyup.UI;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwaresolutions.buyup.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Constants.DefaultValues;
import Constants.GmailSender;
import Constants.Prefconstants;
import Models.Product;
import Utils.GMailSender;
import adapter.RecyclerViewAdapter;

import static com.softwaresolutions.buyup.UI.HomePage.FLAG;

public class BuyActivity extends AppCompatActivity {

    public static List<Product> Related_products;
    public static Product product_buy;
    private ImageView MainImage;
    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private Button     Buy;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter2;
    private SearchView searchView;
    private int REQUEST_CODE=9981;
    private DatabaseReference DF;
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
        setContentView(R.layout.activity_buy);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);






        Buy=(Button) findViewById(R.id.button);
        MainImage=(ImageView) findViewById(R.id.MainImage);
        Image1=(ImageView) findViewById(R.id.Image1);
        Image2=(ImageView) findViewById(R.id.Image2);
        Image3=(ImageView) findViewById(R.id.Image3);
        recyclerView=(RecyclerView) findViewById(R.id.Recycler_Related_Product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Glide.with(BuyActivity.this)
                .load(product_buy.ImageX600)
                .into(MainImage);

        Glide.with(BuyActivity.this)
                .load(product_buy.ImageX150)
                .into(Image1);


        Glide.with(BuyActivity.this)
                .load(product_buy.ImageX150)
                .into(Image2);


        Glide.with(BuyActivity.this)
                .load(product_buy.ImageX150)
                .into(Image3);

        ((TextView)findViewById(R.id.Description)).setText(product_buy.Product_Description);
        ((TextView)findViewById(R.id.Price)).setText("₹"+product_buy.Price);


        recyclerViewAdapter2=new RecyclerViewAdapter(Related_products,BuyActivity.this,R.layout.list_items);
        recyclerView.setAdapter(recyclerViewAdapter2);

        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DropInRequest dropInRequest = new DropInRequest()
                        .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI1ZTc3YTAzYmJlYjBhZTFjZTAxNjFlYTQ1MGM0MTJkZjMwN2U0NTFmZjdiZjk4ZDkxYzYwNDU3ODI4OGRhZmE2fGNyZWF0ZWRfYXQ9MjAxNy0wNi0wN1QyMDowMDo1NS4yOTA0MDg4MjYrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
                startActivityForResult(dropInRequest.getIntent(BuyActivity.this), REQUEST_CODE);
            }
        });

        ((Button) findViewById(R.id.buttonCart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FLAG=false;

                SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);

                final Product SELECTED=product_buy;

                DF= FirebaseDatabase.getInstance().getReference().child("Cart").child(Logged_In_User);

                DF.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(SELECTED.Product_Id))
                        {
                            Toast.makeText(getApplicationContext(),"Item Already In your CART",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            DF=DF.child(SELECTED.Product_Id);

                            DF.child("Product_Category").setValue(SELECTED.Product_Category);
                            DF.child("Product_Id").setValue(SELECTED.Product_Id);
                            DF.child("ImageX150").setValue(SELECTED.ImageX150);
                            DF.child("ImageX600").setValue(SELECTED.ImageX600);
                            DF.child("Product_Description").setValue(SELECTED.Product_Description);
                            DF.child("Price").setValue(SELECTED.Price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(BuyActivity.this,"Item Successfully Added to Cart",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server


                SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);
                final String EMAIL=USER_SHARED_PREF.getString(Prefconstants.USER_NAME,DefaultValues.NO_USER);

                final Product SELECTED=product_buy;

                DF= FirebaseDatabase.getInstance().getReference().child("Orders").child(Logged_In_User);



                            DF=DF.push();
                            DF.child("Product_Category").setValue(SELECTED.Product_Category);
                            DF.child("Product_Id").setValue(SELECTED.Product_Id);
                            DF.child("ImageX150").setValue(SELECTED.ImageX150);
                            DF.child("ImageX600").setValue(SELECTED.ImageX600);
                            DF.child("Tracking_ID").setValue(DF.getKey());
                            DF.child("rating").setValue("0");
                            DF.child("Time_Order").setValue(DateFormat.getDateTimeInstance().format(new Date()));
                            DF.child("Product_Description").setValue(SELECTED.Product_Description);
                            DF.child("Price").setValue(SELECTED.Price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    NotificationManager notificationManager = (NotificationManager)
                                            getSystemService(NOTIFICATION_SERVICE);

                                    Notification notification  = new Notification.Builder(BuyActivity.this)
                                            .setContentText("Payment successful "+getResources().getString(R.string.app_name)+"\n Order placed \n"
                                                    +" Order ID :-"+"BuyUp"+DF.getKey()
                                                    +"\n Ordered Time :-"+ DateFormat.getDateTimeInstance().format(new Date()))
                                            .setContentTitle(getResources().getString(R.string.app_name))
                                            .setSmallIcon(R.mipmap.ic_launcher_1)
                                            .setAutoCancel(true)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .build();
                                    notificationManager.notify(0, notification);


                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                GMailSender sender = new GMailSender(GmailSender.EMAIL_SENDER
                                                        , GmailSender.PASSWORD_SENDER);


                                                sender.sendMail("Order confirmation", "Your BuyUp order received \n"
                                                        +" Order ID "+"BuyUp"+DF.getKey()
                                                        +"\n Amount Paid"+SELECTED.Price
                                                        +"\n Ordered Time "+ DateFormat.getDateTimeInstance().format(new Date())
                                                        , GmailSender.EMAIL_SENDER,
                                                        EMAIL);
                                                //  progressDialog.dismiss();
                                                Log.v("GmailSending","Success");


                                            } catch (Exception e) {

//                                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                                // progressDialog.dismiss();
                                                ///Snackbar.make(findViewById(R.id.LOGIN_ROOT_VIEW),"Failed to send OTP",3000);
                                                Log.v("GmailSending",e.getLocalizedMessage()+"...");

                                            }


                                        }
                                    }).start();






                                }
                            });



            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }
}
