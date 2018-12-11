package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartCards extends Activity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    ImageView Img;
    DatabaseHelper myDb;
    public static Context context,mcontext;
    String UserId,ProductId;

    private GridLayoutManager lLayout;
    private ArrayList<String> FullNames;
    private ArrayList<String> Ids;

    public static final String TAG_FULL_NAME = "SenderName";
    public static final String TAG_ID = "SenderId";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_cards);
        ProductId=getIntent().getStringExtra("ProductId");

        Img=(ImageView) findViewById(R.id.img);

        Img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.shop,  390, 390));

        myDb = new DatabaseHelper(getBaseContext());
        getSQLiteData();

        FullNames = new ArrayList<>();
        Ids = new ArrayList<>();





        lLayout = new GridLayoutManager(getBaseContext(), 1);

        rView = (RecyclerView) findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);

        rView.setLayoutManager(lLayout);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FullNames.clear();
                Ids.clear();
                getData();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        getData();
    }

    public void getSQLiteData() {

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            //Show message

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




    }



    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
        loading.setCancelable(true);
        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_chat_cards_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("yaya", s);


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

                // String BloodGroup="A-";
                params.put("RecepientId",UserId);
                params.put("ProductId",ProductId);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                FullNames.add(obj.getString(TAG_FULL_NAME));

                Ids.add(obj.getString(TAG_ID));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        RecyclerViewAdapterChatCards rcAdapter = new RecyclerViewAdapterChatCards(getBaseContext(),FullNames,Ids,ProductId);
        rView.setAdapter(rcAdapter);
        //Creating GridViewAdapter Object
      /*  GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);*/

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
