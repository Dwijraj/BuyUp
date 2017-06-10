package com.softwaresolutions.buyup.UI;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwaresolutions.buyup.R;

import java.util.ArrayList;

import Constants.DefaultValues;
import Constants.Prefconstants;
import Models.Order;
import adapter.RecyclerViewAdapter;
import adapter.RecyclerViewAdapter2;

import static com.softwaresolutions.buyup.UI.HomePage.TO_CATGORIZED;

public class Activity_Order_Show extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter2 recyclerViewAdapter2;
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
        setContentView(R.layout.activity__order__show);

        recyclerView=(RecyclerView) findViewById(R.id.MyOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /**
         * Activity to show Orders
         */


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

        String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);

        final ArrayList<Order> Orders=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Orders").child(Logged_In_User).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Orders.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                    Order order=ds.getValue(Order.class);
                    Orders.add(order);


                }

                if(Orders.size()==1)
                {
                    Toast.makeText(getApplicationContext(),"You have no ordered Items",Toast.LENGTH_LONG).show();
                }
                else {
                    recyclerViewAdapter2 = new RecyclerViewAdapter2(Orders, Activity_Order_Show.this, R.layout.order_show);
                    recyclerView.setAdapter(recyclerViewAdapter2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
