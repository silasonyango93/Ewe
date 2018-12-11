package com.example.silasonyango.ewe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by SilasOnyango on 3/10/2017.
 */

public class RecyclerViewHoldersThree  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public String productMake;
    AlertDialog alertDialog2;
    AlertDialog.Builder builder;
    public String ProductId,UserId,ProductOwnerId;
    List<MenuObject> mTestData = new ArrayList<>();
    private CustomMenuViewPagerAdapter mAdapter;
    public Context mContext;
    CheckBox checkBook,checkBook1;
    ViewPager viewPager;
    NetworkImageView largeImage;
    public String LargeImageUrl;
    public ImageLoader imageLoader,imageLoader1;
    public TextView Product,Period,tvUserName;
    public TextView Price,tvDeleteIcon;
    public NetworkImageView ProductImage;
    MyNetworkImageView NIvprofPic;
    public Context bContext;
    public static final String TAG_sIMAGE_URL = "PhotoUrl";
    AlertDialog alertDialog;
    final Handler handler = new Handler();
    final Handler mhandler = new Handler();
    private final int delay = 2000;
    private int page = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            if (mAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    LayoutInflater inflater,inflater1;

    public RecyclerViewHoldersThree(View itemView,Context vContext) {
        super(itemView);
        mContext=vContext;
        this.UserId=Landing.UserId;
        inflater =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        inflater1 =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        itemView.setOnClickListener(this);
        Product = (TextView)itemView.findViewById(R.id.product);
        Price = (TextView)itemView.findViewById(R.id.price);
        Period = (TextView)itemView.findViewById(R.id.period);
        ProductImage = (NetworkImageView)itemView.findViewById(R.id.product_image);
        NIvprofPic = (MyNetworkImageView)itemView.findViewById(R.id.profPic);
        tvUserName = (TextView)itemView.findViewById(R.id.name);
       /* tvDeleteIcon = (TextView)itemView.findViewById(R.id.deleteIcon);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");

        tvDeleteIcon.setTypeface(font);*/



    }

    @Override
    public void onClick(View view) {
        View v = inflater.inflate(R.layout.my_booked_products_large_render,null);
        //NetworkImageView largeImage = (NetworkImageView) v.findViewById(R.id.large_render);
        viewPager = (ViewPager)v.findViewById(R.id.hot_deal_view_pager);
        TextView tvProductType=(TextView)v.findViewById(R.id.tv_product_type);
        TextView tvProductMake=(TextView)v.findViewById(R.id.tv_product_make);
        TextView tvProductColour=(TextView)v.findViewById(R.id.tv_colour);
        TextView tvPrice=(TextView)v.findViewById(R.id.tv_price);
        TextView tvOtherDescriptions=(TextView)v.findViewById(R.id.tv_other_descriptions);

        TextView tvCallIcon=(TextView)v.findViewById(R.id.ic_call_icon);
        TextView tvChatIcon=(TextView)v.findViewById(R.id.ic_chat_icon);
        TextView tvShareIcon=(TextView)v.findViewById(R.id.ic_share_icon);

        TextView tvOwnerName=(TextView)v.findViewById(R.id.tv_owner_name);
        TextView tvOwnerEmail=(TextView)v.findViewById(R.id.tv_owner_email);
        TextView tvOwnerAddress=(TextView)v.findViewById(R.id.tv_owner_address);
        TextView tvOwnerCampus=(TextView)v.findViewById(R.id.tv_owner_campus);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");

        tvCallIcon.setTypeface(font);
        tvChatIcon.setTypeface(font);
        tvShareIcon.setTypeface(font);


        tvChatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent o=new Intent(mContext,ChatWall.class);
                o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                o.putExtra("ProductOwnerId",ProductOwnerId);
                o.putExtra("ProductId",ProductId);
                mContext.startActivity(o);

            }
        });

        tvShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View p = inflater.inflate(R.layout.contact_list, null);
                popPage(p,mContext);



            }
        });



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


        bContext=view.getContext();


        getLargeImage(view.getContext(),largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions,v,roomImage,tvHostelName,roomPrice,roomOtherDescriptions,w,roomSellingOrBuying,tvOwnerName,tvOwnerEmail,tvOwnerAddress,tvOwnerCampus,roomEmail,roomCampus);



        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void renderLargeImage(final Context mContext, View v) {
        builder = new AlertDialog.Builder(mContext);

        builder.setView(v);



        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();

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
                    productMake =inflateLargeImage(v,array,largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions,roomImage,tvHostelName,tvRoomPrice,tvRoomOtherDescriptions,roomSellingOrBuying,tvOwnerName,tvOwnerEmail,tvOwnerAddress,tvOwnerCampus,roomEmail,roomCampus);

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

    public String inflateLargeImage(View v,JSONArray jsonArray, NetworkImageView largeImage, TextView tvProductType, TextView tvProductMake, TextView tvProductColour, TextView tvPrice, TextView tvOtherDescriptions,NetworkImageView roomImage,TextView tvHostelName,TextView tvRoomPrice,TextView tvRoomOtherDescriptions,TextView roomSellingOrBuying,TextView ownerName,TextView ownerEmail,TextView ownerAddress,TextView ownerCampus,TextView roomEmail,TextView roomCampus) {
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
        mTestData.clear();
klabu(v);
        // showMessage("url",ImageUrl);
        /*imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
        imageLoader.get(LargeImageUrl, ImageLoader.getImageListener(largeImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        largeImage.setImageUrl(LargeImageUrl,imageLoader);

        imageLoader1 = CustomVolleyRequest.getInstance(mContext).getImageLoader();
        imageLoader1.get(LargeImageUrl, ImageLoader.getImageListener(roomImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        roomImage.setImageUrl(LargeImageUrl, imageLoader1);*/

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
    public void popPage(View v,Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        getAll(v,c);

        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog2 = builder.create();
        alertDialog2.show();
    }


    public void getAll(final View v, Context c)
    {

        final ListView listview = (ListView) v.findViewById(R.id.listview);
        final ArrayList<Contacts> list = new ArrayList<>();
        Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
            list.add(new Contacts(name,phoneNumber));
        }
        phones.close();

        MyContactAdapter adapter = new MyContactAdapter(mContext, R.layout.my_custom_contact_card, list);
        listview.setAdapter(adapter);
    }


    private void klabu(final View v) {


        //Creating a json array request to get the json from our api

        StringRequest request = new StringRequest(Request.Method.POST, Config.get_extra_product_photos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        Log.d("responseKlabu", s);


                        try {

                            JSONObject object = new JSONObject(s);
                            JSONArray array= object.getJSONArray("result");

                            showmGrid(v,array);
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


        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        //Adding our request to the queue
        Log.d("respo", request.toString());
        requestQueue.add(request);
    }

    private void showmGrid(View v,JSONArray jsonArray) {
        //Looping through all the elements of json array

        int j=0;


        for (int i = 0; i < jsonArray.length(); i++) {

            //Creating a json object of the current index
            JSONObject obj ;
            try {

                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                ArrayList<String> testURLS= new ArrayList<String>();
                String testURL= obj.getString(TAG_sIMAGE_URL);
                //images.add(obj.getString(TAG_sIMAGE_URL));
                testURLS.add(testURL);
                Log.d("testURLS",testURLS.toString());
                //getting image url and title from json object
                mTestData.add(new MenuObject(testURL));
                Log.d( "showmGridData", mTestData.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        final RadioGroup myRadioGroup = new RadioGroup(mContext);
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

        for (j = 1; j <= mTestData.size(); j++) {
            RadioButton rdbtn = new RadioButton(mContext);
            rdbtn.setId(j);
            rdbtn.setScaleX((float) 0.5);
            rdbtn.setScaleY((float) 0.5);
            if(Build.VERSION.SDK_INT>=21)
            {

                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{

                                new int[]{-android.R.attr.state_enabled}, //disabled
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[] {

                                R.color.pink //disabled
                                , Color.BLUE //enabled

                        }
                );


                rdbtn.setButtonTintList(colorStateList);//set the color tint list
                rdbtn.invalidate(); //could not be necessary
            }


            //rdbtn.setText("Radio " + rdbtn.getId());
            myRadioGroup.addView(rdbtn);
        }
        ((ViewGroup) v.findViewById(R.id.radiogroup)).addView(myRadioGroup);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @SuppressLint("ResourceType")
            @Override
            public void onPageSelected(int position) {
                page = position;
                myRadioGroup.check(position+1);

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mAdapter = new CustomMenuViewPagerAdapter(mContext, mTestData);

        // SlideshowPagerAdapter adapter = new SlideshowPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);



    }



}
