package com.softwaresolutions.buyup.UI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwaresolutions.buyup.R;

import java.util.ArrayList;
import java.util.List;

import Constants.DefaultValues;
import Constants.Prefconstants;
import Models.Product;
import adapter.RecyclerViewAdapter;

import static com.softwaresolutions.buyup.UI.HomePage.TO_CATGORIZED;

public class CategorizedSearch extends AppCompatActivity {

    private SearchView searchView;
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter recyclerViewAdapter;
    private TextView DISPLAY_CATEGORY;
    public static String IS_IN_MYCART="false";
    private ArrayList<Product>  MyCart;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IS_IN_MYCART="false";
    }

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
        setContentView(R.layout.activity_categorized_search);


        recyclerView=(RecyclerView) findViewById(R.id.Recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DISPLAY_CATEGORY=(TextView) findViewById(R.id.Category_Display);


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        View v = LayoutInflater
                .from(CategorizedSearch.this)
                .inflate(R.layout.custom_action_bar, null);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(v,params);

        View ACTION_BAR_VIEW =getSupportActionBar().getCustomView();
        searchView=(android.support.v7.widget.SearchView) ACTION_BAR_VIEW.findViewById(R.id.ITEM_SEARCH_VIEW);
        final TextView SearchViewTextView=(TextView) searchView.findViewById(R.id.search_src_text);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Filter(SearchViewTextView.getText().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        Bundle extras=getIntent().getExtras();
        String Label=extras.getString("Label");

        DISPLAY_CATEGORY.setText(Label);




        actionBar.setTitle(Label);

        if(Label.contains("Orders"))
        {
            recyclerViewAdapter = new RecyclerViewAdapter(TO_CATGORIZED.subList(0, TO_CATGORIZED.size() - 1), CategorizedSearch.this, R.layout.categorized_search,false);
            recyclerView.setAdapter(recyclerViewAdapter);

        }
      /*  else if(Label.contains("Cart"))
        {
            final ProgressDialog progressDialog1=new ProgressDialog(this);
            progressDialog1.setMessage("Fetching your cart...");
            progressDialog1.show();
            MyCart=new ArrayList<>();
            System.gc();
            SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);
            String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);
            DatabaseReference DF= FirebaseDatabase.getInstance().getReference().child("Cart").child(Logged_In_User);
            DF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    MyCart.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {

                        MyCart.add(ds.getValue(Product.class));
                    }
                    progressDialog1.dismiss();

                    if(MyCart.size()==1)
                        Toast.makeText(getApplicationContext(),"You have not items in your cart",Toast.LENGTH_SHORT).show();

                    try {
                        recyclerViewAdapter = new RecyclerViewAdapter(MyCart.subList(0, TO_CATGORIZED.size() - 1), CategorizedSearch.this, R.layout.categorized_search);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                    catch (Exception e)
                    {
                        finish();
                    }
                    //MyCart.clear();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                    progressDialog1.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Connect",Toast.LENGTH_LONG);
                }
            });
        } */
        else
        {
            try {
                recyclerViewAdapter = new RecyclerViewAdapter(TO_CATGORIZED.subList(0, TO_CATGORIZED.size() - 1), CategorizedSearch.this, R.layout.categorized_search);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
            catch (Exception e)
            {
                finish();
            }
        }





    }
    public void Filter(String Searched)
    {
        List<Product> Searched_data=new ArrayList<>();

        for(Product p:TO_CATGORIZED)
        {
            if(p.Product_Description.contains(Searched))
                Searched_data.add(p);
        }


        Log.v("SizeOfSeacrhed",Searched_data.size()+"");

        for(Product p:Searched_data)
        {
            Log.v("Product",p.toString());
        }

        if(Searched_data.size()!=0) {

            Dialog CustomDialog = new Dialog(this);
            CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            CustomDialog.setContentView(R.layout.product_searched);
            CustomDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(Searched_data, CategorizedSearch.this, R.layout.categorized_search);
            RecyclerView recyclerView = (RecyclerView) CustomDialog.findViewById(R.id.Dialog_Recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(recyclerViewAdapter);

            CustomDialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No product matched your search",Toast.LENGTH_LONG).show();
        }
    }

}
