package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SilasOnyango on 3/2/2017.
 */

public class GeneralPublic extends AppCompatActivity {

    public static Context context;
ImageView Cover;
    RecyclerView rView;



    private ArrayList<String> images;
    private ArrayList<String> ProductTypes;
    private ArrayList<String> ProductMakes;
    private ArrayList<String> ProductColours;
    private ArrayList<String> Prices;
    private ArrayList<String> OtherDescriptions;
    private ArrayList<String> ids;
    private ArrayList<String> ProductIds;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_public);
        getData();
        setTitle(null);

        images = new ArrayList<>();
        ProductTypes = new ArrayList<>();
        ProductMakes = new ArrayList<>();
        ProductColours = new ArrayList<>();
        Prices = new ArrayList<>();
        OtherDescriptions = new ArrayList<>();
       ids = new ArrayList<>();
        ProductIds = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        Cover=(ImageView)findViewById(R.id.backdrop);
        Cover.bringToFront();
        Cover.invalidate();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        lLayout = new GridLayoutManager(GeneralPublic.this, 2);

        rView = (RecyclerView)findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        context=getBaseContext();
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general_public, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_refresh){
            Toast.makeText(GeneralPublic.this, "Refresh App", Toast.LENGTH_LONG).show();
        }
        if(id == R.id.action_new){
            Toast.makeText(GeneralPublic.this, "Create Text", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_all_products, new Response.Listener<String>() {
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
        });

        //Creating a request queue
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
                images.add(obj.getString(TAG_IMAGE_URL));
                ProductTypes.add(obj.getString(TAG_PRODUCT_TYPE));
                ProductMakes.add(obj.getString(TAG_PRODUCT_MAKE));
                ProductColours.add(obj.getString(TAG_PRODUCT_COLOUR));
                Prices.add(obj.getString(TAG_PRICE));
                OtherDescriptions.add(obj.getString(TAG_OTHER_DESCRIPTIONS));
                ids.add(obj.getString(TAG_ID));
                ProductIds.add(obj.getString(TAG_PRODUCT_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(GeneralPublic.this,images,ids,ProductTypes,ProductMakes,ProductColours,Prices,OtherDescriptions,ProductIds);
        rView.setAdapter(rcAdapter);
        //Creating GridViewAdapter Object
      /*  GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);*/
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}





}
