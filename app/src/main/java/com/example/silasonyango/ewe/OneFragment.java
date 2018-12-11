package com.example.silasonyango.ewe;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.silasonyango.ewe.Landing.isConnected;
import static com.example.silasonyango.ewe.SignIn.decodeSampledBitmapFromResource;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    DatabaseHelper myDb;
    public static Context context;
    final Handler handler = new Handler();
    ImageView img;
    String UserId;
    View landing;
    TextView tvBackgroundIcon,tvBackgroundText;




    private ArrayList<String> images;
    private ArrayList<String> ProductTypes;
    private ArrayList<String> ProductMakes;
    private ArrayList<String> ProductColours;
    private ArrayList<String> Prices;
    private ArrayList<String> OtherDescriptions;
    private ArrayList<String> ids;
    private ArrayList<String> ProductIds;
    private ArrayList<String> Periods;

    private ArrayList<String> ProfPicUrls;

    private ArrayList<String> OwnerNames;


    private GridLayoutManager lLayout;
    //Web api url


    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "ProductImageUrl";
    public static final String TAG_PRODUCT_TYPE = "ProductType";
    public static final String TAG_PRODUCT_MAKE = "ProductMake";
    public static final String TAG_PRODUCT_COLOUR = "ProductColour";
    public static final String TAG_PRICE = "Price";
    public static final String TAG_OTHER_DESCRIPTIONS = "OtherDescriptions";
    public static final String TAG_ID = "id";
    public static final String TAG_PRODUCT_ID = "ProductId";
    public static final String TAG_PERIOD = "Period";
    public static final String TAG_PROFPIC_URL = "url";

    public static final String TAG_OWNER_NAME = "name";



    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        tvBackgroundIcon=(TextView) v.findViewById(R.id.tvBackground);
        tvBackgroundText=(TextView) v.findViewById(R.id.tvBackground2);
        img=(ImageView) v.findViewById(R.id.img1);

        img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.eggs,  700, 700));

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        tvBackgroundIcon.setTypeface(font);

        tvBackgroundIcon.setVisibility(View.GONE);
        tvBackgroundText.setVisibility(View.GONE);

        myDb = new DatabaseHelper(getContext());
        getSQLiteData();

        //setTitle(null);



        images = new ArrayList<>();
        ProductTypes = new ArrayList<>();
        ProductMakes = new ArrayList<>();
        ProductColours = new ArrayList<>();
        Prices = new ArrayList<>();
        OtherDescriptions = new ArrayList<>();
        ids = new ArrayList<>();
        ProductIds = new ArrayList<>();
        Periods = new ArrayList<>();
        OwnerNames = new ArrayList<>();
        ProfPicUrls = new ArrayList<>();


        lLayout = new GridLayoutManager(getActivity(), 2);

        rView = (RecyclerView) v.findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        context=getContext();



        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        images.clear();
                        ProductTypes.clear();
                        ProductMakes.clear();
                        ProductColours.clear();
                        Prices.clear();
                        OtherDescriptions.clear();
                        ids.clear();
                        ProductIds.clear();
                        Periods.clear();
                        OwnerNames.clear();
                        ProfPicUrls.clear();
                        getData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        /*swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

            }


        });*/

        checkConnection();


        return v;
    }

    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_my_uploads, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    showGrid(array);

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
                params.put("id",UserId);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                //getting image url and title from json object
                images.add(obj.getString(TAG_IMAGE_URL));
                ProductTypes.add(obj.getString(TAG_PRODUCT_TYPE));
                ProductMakes.add(obj.getString(TAG_PRODUCT_MAKE));
                ProductColours.add(obj.getString(TAG_PRODUCT_COLOUR));
                Prices.add(obj.getString(TAG_PRICE));
                OtherDescriptions.add(obj.getString(TAG_OTHER_DESCRIPTIONS));
                ids.add(obj.getString(TAG_ID));
                ProductIds.add(obj.getString(TAG_PRODUCT_ID));
                Periods.add(obj.getString(TAG_PERIOD));
                OwnerNames.add(obj.getString(TAG_OWNER_NAME));
                ProfPicUrls.add(obj.getString(TAG_PROFPIC_URL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        RecyclerViewAdapterOne rcAdapter = new RecyclerViewAdapterOne(getActivity(),images,ids,ProductTypes,ProductMakes,ProductColours,Prices,OtherDescriptions,ProductIds,Periods,OwnerNames,ProfPicUrls);
        rView.setAdapter(rcAdapter);
        //Creating GridViewAdapter Object
      /*  GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);*/
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}


    public void getSQLiteData() {

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            //Show message
            showMessage("Error", "No data found Silas");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("dbID : " + res.getString(0) + "\n");
            buffer.append("id : " + res.getString(1) + "\n");
            buffer.append("Name : " + res.getString(2) + "\n");
            buffer.append("Email : " + res.getString(3) + "\n\n");


            UserId=res.getString(1);
        }

        //Show all data

        // showMessage("Data", buffer.toString());


    }

    public void checkConnection()
    {boolean connected;
        try {
            connected= isConnected();

            if(connected==true)
            {getData();
                tvBackgroundIcon.setVisibility(View.GONE);
                tvBackgroundText.setVisibility(View.GONE);}
            else{
                final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_card);
                tvBackgroundIcon.setVisibility(View.VISIBLE);
                tvBackgroundText.setVisibility(View.VISIBLE);
                tvBackgroundText.setText("No internet connection");
                tvBackgroundIcon.setText(getActivity().getResources().getString(R.string.net));
                tvBackgroundIcon.startAnimation(animShake);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkConnection();
                    }
                }, 2000);

                //checkConnection();
                }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
