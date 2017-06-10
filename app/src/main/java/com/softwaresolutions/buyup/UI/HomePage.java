package com.softwaresolutions.buyup.UI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwaresolutions.buyup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Constants.DefaultValues;
import Constants.Prefconstants;
import Constants.URLs;
import Models.Product;
import adapter.RecyclerViewAdapter;

import static com.softwaresolutions.buyup.UI.CategorizedSearch.IS_IN_MYCART;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Boolean StoreSetup;
    private Boolean RecyclerView1Complete;
    private Boolean RecyclerView2Complete;
    private View VIEW_OF_NAVIGATION_DRAWER_HEADER;
    private TextView WELCOME_MESSAGE;
    private android.support.v7.widget.SearchView searchView;
    private ProgressDialog progressDialog;
    private ArrayList<Product>  Television;
    private ArrayList<Product>  Electronics;
    private ArrayList<Product>  Fashion;
    private ArrayList<Product>  Home;
    private ArrayList<Product>  OfferZone;
    private RecyclerView RecyclerViewOffers1;
    private RecyclerViewAdapter recyclerViewAdapter1;
    private RecyclerView RecyclerviewMostBought1;
    private RecyclerViewAdapter recyclerViewAdapter2;
    public static ArrayList<Product>  TO_CATGORIZED;
    private  ArrayList<Product> MyCart;
    private  List<Product> SubList1;
    private  List<Product> SubList2;
    public static Boolean FLAG=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_drawer);



        RecyclerViewOffers1=(RecyclerView) findViewById(R.id.Recycler_view_Offers_Home_1);
        RecyclerViewOffers1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerviewMostBought1=(RecyclerView) findViewById(R.id.Recycler_View_Most_bought_2);
        RecyclerviewMostBought1.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        View v = LayoutInflater
                .from(HomePage.this)
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








        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.InputTextColor));
        searchEditText.setHintTextColor(getResources().getColor(R.color.InputTextColor));
        searchEditText.setHint("Search your product here based on description");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        VIEW_OF_NAVIGATION_DRAWER_HEADER=navigationView.getHeaderView(0);
        WELCOME_MESSAGE=(TextView) VIEW_OF_NAVIGATION_DRAWER_HEADER.findViewById(R.id.NAVIGATION_HEADER_LOGGED_USER);
        WELCOME_MESSAGE.setText("Welcome to"+getResources().getString(R.string.app_name));

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Setting up store...");
        progressDialog.show();

        Television=new ArrayList<>();
        Electronics=new ArrayList<>();
        Fashion=new ArrayList<>();
        Home=new ArrayList<>();
        OfferZone=new ArrayList<>();
        StoreSetup=true;
        RecyclerView1Complete=false;
        RecyclerView2Complete=false;


        SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

        String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);



       DatabaseReference DF=FirebaseDatabase.getInstance().getReference().child("Cart").child(Logged_In_User).child("100000000");

        DF.child("Product_Category").setValue("default");
        DF.child("Product_Id").setValue("default");
        DF.child("ImageX150").setValue("default");
        DF.child("ImageX600").setValue("default");
        DF.child("Product_Description").setValue("default");
        DF.child("Price").setValue("default");

        DF= FirebaseDatabase.getInstance().getReference().child("Orders").child(Logged_In_User);



        DF=DF.child("Default");
        DF.child("Product_Category").setValue("default");
        DF.child("Product_Id").setValue("default");
        DF.child("ImageX150").setValue("default");
        DF.child("ImageX600").setValue("default");
        DF.child("Tracking_ID").setValue("default");
        DF.child("rating").setValue("0");
        DF.child("Time_Order").setValue("default");
        DF.child("Product_Description").setValue("default");
        DF.child("Price").setValue("default");

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URLs.PRODUCTS_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {



                for (int i=1;i<response.length()+1;i++)
                {
                    try {

                        JSONObject jsonObject=response.getJSONObject(i-1);

                        /**
                         *"albumId": 1,
                         "id": 1,
                         "title": "accusamus beatae ad facilis cum similique qui sunt",
                         "url": "http://placehold.it/600/92c952",
                         "thumbnailUrl": "http://placehold.it/150/92c952"
                         *
                         */
                        Product product=new Product(jsonObject.getString("albumId")
                                ,jsonObject.getString("title"),
                                jsonObject.getString("thumbnailUrl"),
                                jsonObject.getString("url"),i+"",
                                jsonObject.getString("id"));

                        Log.v("Product",product.toString());

                        if(i%5==0)
                        {
                            OfferZone.add(product);
                        }
                        else if(i%4==0)
                        {
                            Home.add(product);
                        }
                        else if(i%3==0)
                        {
                            Fashion.add(product);
                        }
                        else if(i%2==0)
                        {
                            Electronics.add(product);
                        }
                        else
                        {
                            Television.add(product);
                        }



                    } catch (JSONException e) {

                        progressDialog.dismiss();
                        StoreSetup=false;
                        Toast.makeText(getApplicationContext(),"Store Setup Failed",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
                findViewById(R.id.Home_Fashion_IMG).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StartCategorizedSearch(Fashion,"Fashion");
                    }
                });
                findViewById(R.id.Home_Television_IMG).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartCategorizedSearch(Television,"Television and Accessories");

                    }
                });
                findViewById(R.id.Home_Electronics_IMG).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StartCategorizedSearch(Electronics,"Electronics");
                    }
                });

                findViewById(R.id.Home_Home_IMG).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StartCategorizedSearch(Home,"Home and Accessories");
                    }
                });

                findViewById(R.id.Home_Offer_IMG).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StartCategorizedSearch(OfferZone,"OfferZone");
                    }
                });

               SubList1=OfferZone.subList(0,50);
               SubList2=OfferZone.subList(500,510);

                recyclerViewAdapter1=new RecyclerViewAdapter(SubList1,HomePage.this,R.layout.list_items);
                RecyclerViewOffers1.setAdapter(recyclerViewAdapter1);



                RecyclerViewOffers1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if(RecyclerView1Complete&&RecyclerView2Complete)
                        {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(),"Store Setup Success",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                recyclerViewAdapter2=new RecyclerViewAdapter(SubList2,HomePage.this,R.layout.list_items2);
                RecyclerviewMostBought1.setAdapter(recyclerViewAdapter2);
                RecyclerviewMostBought1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(RecyclerView1Complete&&RecyclerView2Complete)
                        {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(),"Store Setup Success",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                progressDialog.dismiss();
                Log.v("Response",error.toString()+"..");
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());  //Queues the tasks if other tassks are being performed
        requestQueue.add(jsonArrayRequest);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                progressDialog.dismiss();
            }
        },5000);



        Log.v("OfferZone",OfferZone.size()+"");



    }
    public void Filter(String Searched)
    {
        List<Product> Searched_data=new ArrayList<>();

        for(Product p:SubList1)
        {
            if(p.Product_Description.contains(Searched))
                Searched_data.add(p);
        }

        for(Product p:SubList2)
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

            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(Searched_data, HomePage.this, R.layout.categorized_search);
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
    public void StartCategorizedSearch(ArrayList<Product> Category_Selected,String Label)
    {
        TO_CATGORIZED=Category_Selected;
        startActivity(new Intent(HomePage.this,CategorizedSearch.class).putExtra("Label",Label));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.Electronics) {
            StartCategorizedSearch(Electronics,"Electronics");
            return true;
        }
        else if (id == R.id.Fashion) {
            StartCategorizedSearch(Fashion,"Fashion");
            return true;
        }
        else if (id == R.id.Television_Appliances) {
            StartCategorizedSearch(Television,"Television and Accessories");
            return true;
        }
        else if (id == R.id.Home_Accessories) {
            StartCategorizedSearch(Home,"Home and Accessories");
            return true;
        }
        else if(id==R.id.Offer_Zone)
        {
            StartCategorizedSearch(OfferZone,"OfferZone");
            return true;
        }
        else if(id==R.id.App_Share)
        {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, "Install Buyup");
            share.putExtra(Intent.EXTRA_TEXT, "India's number one ecommerce site "+" BuyUp "+"download now to avail free discounts");

            startActivity(Intent.createChooser(share, "Share"));
        }
        else if(id==R.id.My_Account)
        {
            startActivity(new Intent(HomePage.this,MyAccount.class));

        }
        else if(id==R.id.MyCart)
        {

            FLAG=true;

            final  ProgressDialog progressDialog1=new ProgressDialog(this);
            progressDialog1.setMessage("Fetching your cart...");
            progressDialog1.show();
            MyCart=new ArrayList<>();
            System.gc();
            SharedPreferences USER_SHARED_PREF=getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);
            String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);
            DatabaseReference DF= FirebaseDatabase.getInstance().getReference().child("Cart").child(Logged_In_User);
            DF.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(FLAG) {
                        MyCart.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            MyCart.add(ds.getValue(Product.class));
                        }
                        progressDialog1.dismiss();
                        IS_IN_MYCART = "true";

                        if (MyCart.size() == 1)
                            Toast.makeText(getApplicationContext(), "Your Cart is empty", Toast.LENGTH_LONG).show();


                        StartCategorizedSearch(MyCart, "My Cart (" + (MyCart.size() - 1) + ")");

                        //FLAG=false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                    progressDialog1.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Connect",Toast.LENGTH_LONG);
                }
            });
            //StartCategorizedSearch(null,"My Cart");
            return true;
        }
        else if(id==R.id.My_Orders)
        {

            startActivity(new Intent(getApplicationContext(),Activity_Order_Show.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

