package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import java.util.Map;

/**
 * Created by SilasOnyango on 3/10/2017.
 */

public class RecyclerViewHoldersTwo extends RecyclerView.ViewHolder implements View.OnClickListener{
    public String productMake;
    AlertDialog.Builder builder;
    public String ProductId,UserId;
    public Context mContext;
    CheckBox checkBook,checkBook1;
    NetworkImageView largeImage;
    public String LargeImageUrl;
    public ImageLoader imageLoader,imageLoader1;
    public TextView Product,Period;
    public TextView Price,tvUserName,tvHeartIcon;
    public NetworkImageView ProductImage;
    MyNetworkImageView NIvprofPic;
    Button btMore;
    final ArrayList<String> list = new ArrayList<String>();

    public Context bContext;

    LayoutInflater inflater,inflater1;

    public RecyclerViewHoldersTwo(View itemView,Context bContext) {
        super(itemView);
        mContext=bContext;
        this.UserId=Landing.UserId;
        inflater =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        inflater1 =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        itemView.setOnClickListener(this);
        Product = (TextView)itemView.findViewById(R.id.product);
        tvUserName = (TextView)itemView.findViewById(R.id.name);
        tvHeartIcon = (TextView)itemView.findViewById(R.id.heartIcon);
        Price = (TextView)itemView.findViewById(R.id.price);
        Period = (TextView)itemView.findViewById(R.id.period);
        ProductImage = (NetworkImageView)itemView.findViewById(R.id.product_image);
        NIvprofPic = (MyNetworkImageView)itemView.findViewById(R.id.profPic);
        btMore=(Button) itemView.findViewById(R.id.btMore);
        //colourPinkHolder();
        btMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = inflater.inflate(R.layout.large_render,null);
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



                checkBook=(CheckBox)v.findViewById(R.id.check_book);

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


                //bContext=view.getContext();

                getLargeImage(view.getContext(),largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions,v,roomImage,tvHostelName,roomPrice,roomOtherDescriptions,w,roomSellingOrBuying,tvOwnerName,tvOwnerEmail,tvOwnerAddress,tvOwnerCampus,roomEmail,roomCampus);

                //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");

        tvHeartIcon.setTypeface(font);

        tvHeartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String key="present";
                colourPinkHolder();

if(key.equals(list.get(0)))
{tvHeartIcon.setTextColor(mContext.getResources().getColor(R.color.translucent_scrim));}
else{tvHeartIcon.setTextColor(mContext.getResources().getColor(R.color.pink));
    bookProduct(mContext);}*/
                tvHeartIcon.setTextColor(mContext.getResources().getColor(R.color.pink));
                bookProduct(mContext);

            }
        });



    }
    @Override
    public void onClick(View view) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void renderLargeImage(final Context mContext, View v) {
        builder = new AlertDialog.Builder(mContext);

        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        checkBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    bookProduct(mContext);

alertDialog.cancel();

                }

            }
        });

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");


        alertDialog.show();
    }

    public void getLargeImage(final Context context, final NetworkImageView largeImage, final TextView tvProductType, final TextView tvProductMake, final TextView tvProductColour, final TextView tvPrice, final TextView tvOtherDescriptions, final View v,final NetworkImageView roomImage,final TextView tvHostelName,final TextView tvRoomPrice,final TextView tvRoomOtherDescriptions, final View w,final TextView roomSellingOrBuying,final TextView tvOwnerName,final TextView tvOwnerEmail,final TextView tvOwnerAddress,final TextView tvOwnerCampus,final TextView roomEmail,final TextView roomCampus){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(context, "Please wait...","Fetching data...",false,false);

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
        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                LargeImageUrl= obj.getString("ProductImageUrl");
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

        largeImage.setImageUrl(LargeImageUrl,imageLoader);

        imageLoader1 = CustomVolleyRequest.getInstance(mContext).getImageLoader();
        imageLoader1.get(LargeImageUrl, ImageLoader.getImageListener(roomImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        roomImage.setImageUrl(LargeImageUrl, imageLoader1);

        return ProductMake;

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(bContext);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    private void bookProduct(Context mContext){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(mContext, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.book_product, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid

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
                stringMap.put("id",UserId);

                Log.d("pupu", "pupu:"+ProductId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(mContext).add(stringRequest);
    }
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
        final AlertDialog alertDialog = builder.create();
        checkBook1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    bookProduct(mContext);

                    alertDialog.cancel();
                }

            }
        });

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");


        alertDialog.show();
    }


    private void colourPinkHolder() {

        String key;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.pink_holder, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


Log.d("kaka",s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray = object.getJSONArray("result");


                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonArray.getJSONObject(0);


                            list.add(obj.getString("data"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




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
                stringMap.put("id", UserId);
                stringMap.put("ProductId", ProductId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(mContext).add(stringRequest);


    }




}