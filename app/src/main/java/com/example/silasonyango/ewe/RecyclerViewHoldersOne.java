package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SilasOnyango on 3/9/2017.
 */

public class RecyclerViewHoldersOne extends RecyclerView.ViewHolder implements View.OnClickListener{
    AlertDialog alertDialog,alertDialog2;
   public String productMake;
    AlertDialog.Builder builder,builder2;
    Context SuperContext;
    public String ProductId;
    public Context mContext;
    CheckBox checkBook1;
    public static String UserId, UserName, Addr;
    NetworkImageView largeImage;
    public String LargeImageUrl;
    public ImageLoader imageLoader,imageLoader1;
    public TextView Product,Period,tvListIcon,tvChatIcon,tvUserName;;
    public TextView Price;
    MyNetworkImageView NIvprofPic;
    public NetworkImageView ProductImage;
    DatabaseHelper myDb;
    LayoutInflater inflater,inflater1;

    public RecyclerViewHoldersOne(View itemView,Context zContext) {
        super(itemView);

        mContext=zContext;
        myDb = new DatabaseHelper(mContext);
        inflater =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        inflater1 =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        itemView.setOnClickListener(this);
        Product = (TextView)itemView.findViewById(R.id.product);
        Price = (TextView)itemView.findViewById(R.id.price);
        Period = (TextView)itemView.findViewById(R.id.period);
        ProductImage = (NetworkImageView)itemView.findViewById(R.id.product_image);
        NIvprofPic = (MyNetworkImageView)itemView.findViewById(R.id.profPic);
        tvUserName = (TextView)itemView.findViewById(R.id.name);

        viewSpecificData();

    }



    @Override
    public void onClick(View view) {
        View v = inflater.inflate(R.layout.my_large_render,null);
        NetworkImageView largeImage = (NetworkImageView) v.findViewById(R.id.large_render);
        TextView tvProductType=(TextView)v.findViewById(R.id.tv_product_type);
        TextView tvProductMake=(TextView)v.findViewById(R.id.tv_product_make);
        TextView tvProductColour=(TextView)v.findViewById(R.id.tv_colour);
        TextView tvPrice=(TextView)v.findViewById(R.id.tv_price);
        TextView tvOtherDescriptions=(TextView)v.findViewById(R.id.tv_other_descriptions);
        TextView tvOwnerName=(TextView)v.findViewById(R.id.tv_owner_name);
        TextView tvOwnerEmail=(TextView)v.findViewById(R.id.tv_owner_email);
        TextView tvOwnerAddress=(TextView)v.findViewById(R.id.tv_owner_address);
        TextView tvOwnerCampus=(TextView)v.findViewById(R.id.tv_owner_campus);



        tvListIcon=(TextView) v.findViewById(R.id.tv_list_icon);
        tvChatIcon=(TextView) v.findViewById(R.id.tv_chat_icon);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");

        tvListIcon.setTypeface(font);
        tvChatIcon.setTypeface(font);

        //Room data*********************************************************************

        View w = inflater1.inflate(R.layout.hostel_render,null);
        NetworkImageView roomImage = (NetworkImageView) w.findViewById(R.id.large_room);
        TextView tvHostelName=(TextView)w.findViewById(R.id.tv_hostel_name);
        TextView roomPrice=(TextView)w.findViewById(R.id.tv_room_price);
        TextView roomOtherDescriptions=(TextView)w.findViewById(R.id.tv_room_other_descriptions);
        TextView roomSellingOrBuying=(TextView)w.findViewById(R.id.tv_selling_or_buying);

        TextView roomEmail=(TextView)w.findViewById(R.id.tv_phone_number);
        TextView roomCampus=(TextView)w.findViewById(R.id.tv_campus);


        checkBook1=(CheckBox)w.findViewById(R.id.check_book);



        SuperContext=view.getContext();

       getLargeImage(view.getContext(),largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions,v,roomImage,tvHostelName,roomPrice,roomOtherDescriptions,w,roomSellingOrBuying,tvOwnerName,tvOwnerEmail,tvOwnerAddress,tvOwnerCampus,roomEmail,roomCampus);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void renderLargeImage(final Context mContext, View v) {
        builder = new AlertDialog.Builder(mContext);

        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        tvListIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // alertDialog.cancel();
             View p = inflater.inflate(R.layout.tags_list, null);

                inflateList(p);


              // popPage(p,SuperContext);


            }
        });

        tvChatIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent o=new Intent(mContext,ChartCards.class);
                o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                o.putExtra("ProductId",ProductId);
                mContext.startActivity(o);

            }
        });

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");


        alertDialog.show();
    }

    public void getLargeImage(final Context context, final NetworkImageView largeImage, final TextView tvProductType, final TextView tvProductMake, final TextView tvProductColour, final TextView tvPrice, final TextView tvOtherDescriptions, final View v,final NetworkImageView roomImage,final TextView tvHostelName,final TextView tvRoomPrice,final TextView tvRoomOtherDescriptions, final View w,final TextView roomSellingOrBuying,final TextView tvOwnerName,final TextView tvOwnerEmail,final TextView tvOwnerAddress,final TextView tvOwnerCampus,final TextView roomEmail,final TextView roomCampus){
        //Showing a progress dialog while our app fetches the data from url
        JSONArray array;

        final ProgressDialog loading = ProgressDialog.show(context, "Please wait...","Fetching data...",false,false);
       // final JSONArray[] array1 = new JSONArray[1];
        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_large_image, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    productMake =inflateLargeImage(array,largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions,roomImage,tvHostelName,tvRoomPrice,tvRoomOtherDescriptions,roomSellingOrBuying,tvOwnerName,tvOwnerEmail,tvOwnerAddress,tvOwnerCampus,roomEmail,roomCampus);

                    differenciate(productMake,context,v,w);
                   //Log.d("tako", "tamu :"+productMake);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("ggg", volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("ProductId",ProductId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);



    }

    public String inflateLargeImage(JSONArray jsonArray, NetworkImageView largeImage, TextView tvProductType, TextView tvProductMake, TextView tvProductColour, TextView tvPrice, TextView tvOtherDescriptions,NetworkImageView roomImage,TextView tvHostelName,TextView tvRoomPrice,TextView tvRoomOtherDescriptions,TextView roomSellingOrBuying,TextView ownerName,TextView ownerEmail,TextView ownerAddress,TextView ownerCampus,TextView roomEmail,TextView roomCampus) {
        //Looping through all the elements of json array


       String ProductMake= new String();
       for (int i = 0; i < jsonArray.length(); i++) {
           //Creating a json object of the current index
           JSONArray array;
           JSONObject obj = null;
           try {
               //getting json object from current index
               obj = jsonArray.getJSONObject(i);

               LargeImageUrl = obj.getString("ProductImageUrl");
               tvProductType.setText(obj.getString("ProductType"));
               tvProductMake.setText(obj.getString("ProductMake"));
               tvProductColour.setText(obj.getString("ProductColour"));
               tvPrice.setText(obj.getString("Price"));
               tvOtherDescriptions.setText(obj.getString("OtherDescriptions"));
               ProductMake = obj.getString("ProductMake");
               ownerName.setText(obj.getString("name"));
               ownerEmail.setText(obj.getString("email"));
               ownerAddress.setText(obj.getString("Address"));
               ownerCampus.setText(obj.getString("Campus"));


               //Room Render*************************
               tvHostelName.setText(obj.getString("ProductType"));
               tvRoomPrice.setText(obj.getString("Price"));
               tvRoomOtherDescriptions.setText(obj.getString("OtherDescriptions"));
               roomSellingOrBuying.setText(obj.getString("ProductColour"));

               roomEmail.setText(obj.getString("email"));
               roomCampus.setText(obj.getString("Campus"));

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

       // showMessage("url",ImageUrl);
       imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
       imageLoader.get(LargeImageUrl, ImageLoader.getImageListener(largeImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

       largeImage.setImageUrl(LargeImageUrl, imageLoader);


       imageLoader1 = CustomVolleyRequest.getInstance(mContext).getImageLoader();
       imageLoader1.get(LargeImageUrl, ImageLoader.getImageListener(roomImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

       roomImage.setImageUrl(LargeImageUrl, imageLoader1);

       return ProductMake;
   }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SuperContext);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    public void differenciate(String productMake, Context context, View v, View w)
    {
       // Log.d("tako", "tamu :"+productMake);

        if(productMake.equals("Hostel room"))
        {
            renderHostelData(context,w);
        }
        else{
            renderLargeImage(context,v);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void renderHostelData(final Context mContext,View w) {
        builder = new AlertDialog.Builder(mContext);

        builder.setView(w);

        checkBook1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(mContext,
                            "Bro, try Android :)", Toast.LENGTH_LONG).show();
                }

            }
        });

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popPage(View v,ArrayList<String> list) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SuperContext);
        builder2.setView(v);
        final ListView listview = (ListView) v.findViewById(R.id.listview);
      // final ArrayList<String> list = new ArrayList<String>();

        /*list.add("Amy");
        list.add("Bobby");
        list.add("Silas");*/




        final StableArrayAdapter adapter = new StableArrayAdapter(SuperContext,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
       // getAll(v,c);



        builder2.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");



        alertDialog2 = builder2.create();
        alertDialog2.show();



    }




    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    public void viewSpecificData() {

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            //Show message
           // showMessage("Error", "No data found Silas");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("dbID : " + res.getString(0) + "\n");
            buffer.append("id : " + res.getString(1) + "\n");
            buffer.append("Name : " + res.getString(2) + "\n");
            buffer.append("Email : " + res.getString(3) + "\n");
            buffer.append("Key : " + res.getString(4) + "\n");
            buffer.append("Address : " + res.getString(5) + "\n\n");

            UserId = res.getString(1);
            UserName = res.getString(2);
            Addr = res.getString(5);

            //Addrr.setText(Addr);
        }

        //Show all data


        //showMessage("Data", buffer.toString());

        //Addrr.setText(Addr);

    }


    private void inflateList(final View p){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(SuperContext, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_tags, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("tata", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray= object.getJSONArray("result");


                    final ArrayList<String> list = new ArrayList<String>();

                    for(int i = 0; i<jsonArray.length(); i++){

                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonArray.getJSONObject(i);

                            list.add(obj.getString("Address"));
                            //getting image url and title from json object
                          /*  images.add(obj.getString(TAG_IMAGE_URL));
                            ProductTypes.add(obj.getString(TAG_PRODUCT_TYPE));
                            ProductMakes.add(obj.getString(TAG_PRODUCT_MAKE));
                            ProductColours.add(obj.getString(TAG_PRODUCT_COLOUR));
                            Prices.add(obj.getString(TAG_PRICE));
                            OtherDescriptions.add(obj.getString(TAG_OTHER_DESCRIPTIONS));
                            ids.add(obj.getString(TAG_ID));
                            ProductIds.add(obj.getString(TAG_PRODUCT_ID));
                            Periods.add(obj.getString(TAG_PERIOD));*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(list.size()==0)
                    {
                        showMessage("No tags yet","We may not have anyone that has shown interest in this product");
                    }
                    else {popPage(p,list);}



                    //populateSearch(array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("ggg", volleyError.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

               // String dodo="80";
                params.put("id",UserId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(SuperContext);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }




}