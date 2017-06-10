package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwaresolutions.buyup.R;

import Constants.DefaultValues;
import Constants.Prefconstants;
import Models.Product;

/**
 * Created by 1405003 on 16-02-2017.
 */
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.softwaresolutions.buyup.UI.BuyActivity;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.softwaresolutions.buyup.UI.CategorizedSearch.IS_IN_MYCART;
import static com.softwaresolutions.buyup.UI.HomePage.FLAG;

/**
 * Created by 1405214 on 23-10-2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder> {

    private List<Product> ListData;
    private LayoutInflater inflater;
    private Context Main;
    private int LayoutId;
    private Boolean SHOW_OTHER_FIELDS;


    public RecyclerViewAdapter(List<Product> listData, Context c,int LayoutId) {
        this.ListData = listData;
        this.inflater=LayoutInflater.from(c);
        this.LayoutId=LayoutId;
        this.SHOW_OTHER_FIELDS=true;
        Main=c;
    }

    public RecyclerViewAdapter(List<Product> listData, Context c,int LayoutId,boolean SHOW_OTHER_FIELDS) {
        this.ListData = listData;
        this.inflater=LayoutInflater.from(c);
        this.LayoutId=LayoutId;
        this.SHOW_OTHER_FIELDS=SHOW_OTHER_FIELDS;
        Main=c;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(LayoutId, parent, false);

            return new Holder(view);


    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        Product list_item=ListData.get(position);

        if(!SHOW_OTHER_FIELDS)
        {
            holder.Price.setText("Delivered");

        }
        else
            holder.Price.setText("Just for ₹"+list_item.Price);

        holder.Description.setText(list_item.Product_Description);

        if(LayoutId==R.layout.categorized_search)
        {
            Glide.with(Main)
                    .load(list_item.ImageX600)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ProductImage);

        }
        else {
            Glide.with(Main)
                    .load(list_item.ImageX150)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ProductImage);
        }

        if(IS_IN_MYCART.equals("true"))
        {
            holder.ADD_TO_CART.setText("Remove from cart");

            holder.ADD_TO_CART.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences USER_SHARED_PREF=Main.getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                    String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);

                    Product SELECTED=ListData.get(position);

                    DatabaseReference DF=FirebaseDatabase.getInstance().getReference().child("Cart").child(Logged_In_User).child(SELECTED.Product_Id);

                    DF.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(Main,"Item Removed from Cart",Toast.LENGTH_SHORT).show();
                            IS_IN_MYCART="true";


                            holder.ADD_TO_CART.setText("RemoveD from cart");
                            holder.ADD_TO_CART.setEnabled(false);
                            holder.ADD_TO_CART.setTextColor(Color.RED);



                        }
                    });

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {
        private TextView Price;
        private Button Buy;
        private ImageView ProductImage;
        private TextView Description;
        private CardView container;
        private Button ADD_TO_CART;
        private DatabaseReference DF;

        public Holder(View itemView) {
            super(itemView);

            Description=(TextView) itemView.findViewById(R.id.ItemDescription);
            Price=(TextView) itemView.findViewById(R.id.PriceItem);
            ProductImage=(ImageView) itemView.findViewById(R.id.ProductImage);
            container=(CardView)itemView.findViewById(R.id.Container);

            if(!SHOW_OTHER_FIELDS)
            {
                Description.setVisibility(View.INVISIBLE);
            }

            if(LayoutId==R.layout.categorized_search)
            {
                Buy=(Button) itemView.findViewById(R.id.BUY_ITEM);

                Buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BuyActivity.Related_products=ListData;
                        BuyActivity.product_buy=ListData.get(getPosition());
                        Main.startActivity(new Intent(Main,BuyActivity.class));
                    }
                });

                ADD_TO_CART=(Button) itemView.findViewById(R.id.add_to_cart);
                if(!SHOW_OTHER_FIELDS)
                {
                    Buy.setVisibility(View.INVISIBLE);
                    ADD_TO_CART.setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.COLOR_1).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.COLOR_2).setVisibility(View.INVISIBLE);
                }

                ADD_TO_CART.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FLAG=false;

                        SharedPreferences USER_SHARED_PREF=Main.getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                        String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);

                        final Product SELECTED=ListData.get(getPosition());

                        DF= FirebaseDatabase.getInstance().getReference().child("Cart").child(Logged_In_User);

                        DF.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(SELECTED.Product_Id))
                                {
                                    Toast.makeText(Main,"Item Already In your CART",Toast.LENGTH_LONG).show();
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

                                            Toast.makeText(Main,"Item Successfully Added to Cart",Toast.LENGTH_SHORT).show();

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
            else
            {
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BuyActivity.Related_products=ListData;
                        BuyActivity.product_buy=ListData.get(getPosition());
                        Main.startActivity(new Intent(Main,BuyActivity.class));
                    }
                });
            }

        }
    }


}