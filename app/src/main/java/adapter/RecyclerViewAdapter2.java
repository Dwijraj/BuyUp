package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwaresolutions.buyup.R;
import com.softwaresolutions.buyup.UI.BuyActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import Constants.DefaultValues;
import Constants.GmailSender;
import Constants.Prefconstants;
import Models.Order;
import Models.Product;
import Utils.GMailSender;

import static android.content.Context.MODE_PRIVATE;
import static com.softwaresolutions.buyup.UI.CategorizedSearch.IS_IN_MYCART;
import static com.softwaresolutions.buyup.UI.HomePage.FLAG;

/**
 * Created by 1405214 on 10-06-2017.
 */


public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.Holder> {

    private ArrayList<Order> ArrayListData;
    private LayoutInflater inflater;
    private Context Main;
    private int LayoutId;
    private Boolean SHOW_OTHER_FIELDS;


    public RecyclerViewAdapter2(ArrayList<Order> ArrayListData, Context c,int LayoutId) {
        this.ArrayListData = ArrayListData;
        this.inflater=LayoutInflater.from(c);
        this.LayoutId=LayoutId;
        this.SHOW_OTHER_FIELDS=true;
        Main=c;
    }

    public RecyclerViewAdapter2(ArrayList<Order> ArrayListData, Context c,int LayoutId,boolean SHOW_OTHER_FIELDS) {
        this.ArrayListData = ArrayListData;
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

        Order ArrayList_item=ArrayListData.get(position);

        Glide.with(Main)
                .load(ArrayList_item.ImageX600)
                .into(holder.Product_Image);

        holder.Price.setText("₹"+ArrayList_item.Price);
        holder.Time_Ordered.setText(ArrayList_item.Time_Order);
        holder.Order_Id.setText("BuyUp"+ArrayList_item.Tracking_ID);

        if(ArrayList_item.Tracking_ID.equals("default"))
        {
            ArrayListData.remove(position);
        }

        holder.CANCEL_ORDER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Clicked","Here");

                AlertDialog.Builder builder=new AlertDialog.Builder(Main);

                builder.setTitle("Confirm delete");
                builder.setMessage("Are you sure you want to cancel this Order ?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final Order order=ArrayListData.get(position);

                        SharedPreferences USER_SHARED_PREF=Main.getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                        String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);

                        String SPLIT[]=order.Tracking_ID.split("BuyUp");
                        String REF=SPLIT[0];

                        Log.v("Reference",REF);
                        FirebaseDatabase.getInstance().getReference().child("Orders").child(Logged_In_User).child(REF).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(Main,"Item Removed",Toast.LENGTH_LONG).show();
                                holder.CANCEL_ORDER.setText("ORDER CANCELED");
                                holder.CANCEL_ORDER.setTextColor(Color.RED);
                                holder.CANCEL_ORDER.setEnabled(false);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        SharedPreferences USER_SHARED_PREF=Main.getSharedPreferences(Prefconstants.LOGGED_IN_USER_PREF_NAME,MODE_PRIVATE);

                                        String Logged_In_User=USER_SHARED_PREF.getString(Prefconstants.MOBILE_USER, DefaultValues.NO_USER);
                                        final String EMAIL=USER_SHARED_PREF.getString(Prefconstants.USER_NAME,DefaultValues.NO_USER);

                                        try {
                                            GMailSender sender = new GMailSender(GmailSender.EMAIL_SENDER
                                                    , GmailSender.PASSWORD_SENDER);


                                            sender.sendMail("Order confirmation", "Your BuyUp order cancelled \n"
                                                            +" Order ID :-"+"BuyUp"+order.Tracking_ID
                                                            +"\n Cancelled Time :-"+ DateFormat.getDateTimeInstance().format(new Date()
                                                            +"\n You will be refunded within next 24 hours")
                                                    , GmailSender.EMAIL_SENDER,
                                                    EMAIL);
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

                    }
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return ArrayListData.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {

        private ImageView Product_Image;
        private TextView  Price;
        private TextView  Time_Ordered;
        private TextView  Order_Id;
        private Button    CANCEL_ORDER;
        public Holder(View itemView) {
            super(itemView);

            Product_Image=(ImageView )itemView.findViewById(R.id.ProductImage);
            Price=(TextView) itemView.findViewById(R.id.Price);
            Time_Ordered=(TextView) itemView.findViewById(R.id.ORDERED_TIME);
            Order_Id=(TextView) itemView.findViewById(R.id.ORDER_ID);
            CANCEL_ORDER=(Button) itemView.findViewById(R.id.CANCEL_ORDER);


        }
    }



}