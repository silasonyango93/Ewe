package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

/**
 * Created by SilasOnyango on 3/16/2017.
 */

public class ProductSearchResults extends AppCompatActivity {
    View searchProductView;
    String stcategory,CurrentlyTyped;

    View v;
    String productType,productMake,productColour,price,otherDescriptions,campus,category;
    LayoutInflater inflater2;
    AutoCompleteTextView textView;

    int i;
    public String[] COUNTRIES = new String[900000];
    /* private static final String[] COUNTRIES = new String[]{"Short title", "Accounting officer", "Appeal", "Guiding principles", "Conflicts with other Acts", "Application of the Act", "PUBLIC PROCUREMENT AND ASSET DISPOSAL ACT", "Conflicts with international agreements", "Interpretation", "Role National Treasury on public procurement and assets disposal"
             , "The Public Procurement Regulatory Authority", "Functions of Authority,Public Procurement Regulatory Board,Qualifications of members of the Board",
             "Functions of the Board", "Tenure of office", "Procedures of the Board", "Director-General of the Authority", "Term of office of Director-General",
             "Functions of the Director-General", "Restrictions on activities of Director-General", "Terms and conditions of service of Director-General and staff",
             "Vacancy of office", "Removal of Director-General", "Acting Director-General"};*/
    TextView tvCount;
    //*****************************************************************

    //Variables for Product Search dialog*******************************************************************************
    EditText searchProductType,searchProductMake,searchProductColour,searchCampus,searchProductOtherDescriptions,searchPrice;
    Button searchButtonSubmit,searchButtonCancel;


    ProductSearchResults psr;


    //**************************************************************************************************************



    String strproductType,strproductMake,strproductColour,strprice,strotherDescriptions,strcampus;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    DatabaseHelper myDb;
    public static Context context;

    public Context dContext;
    final ArrayList<String> list = new ArrayList<String>();
    TextView tvBackgroundIcon,tvBackgroundText;
    final Handler handler = new Handler();

    String UserId;
    View landing;

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
    private ArrayList<String> OwnerEmails;
    private ArrayList<String> OwnerAddresses;
    private ArrayList<String> OwnerCampuses;

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

    //Tagging owner Details****************************************************

    public static final String TAG_OWNER_NAME = "name";
    public static final String TAG_OWNER_EMAIL = "email";
    public static final String TAG_OWNER_ADDRESS = "Address";
    public static final String TAG_OWNER_CAMPUS = "Campus";


    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_search_results);

        // Log.d("ccc",String.valueOf(COUNTRIES[0]));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        tvBackgroundIcon=(TextView) findViewById(R.id.tvBackground);
        tvBackgroundText=(TextView) findViewById(R.id.tvBackground2);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        tvBackgroundIcon.setTypeface(font);

        tvBackgroundIcon.setVisibility(View.GONE);
        tvBackgroundText.setVisibility(View.GONE);

        colourPink();
        //setTitle(null);


        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflator.inflate(R.layout.actionbar, null);

        getSupportActionBar().setCustomView(v);

        TextView tvSearch = (TextView) v.findViewById(R.id.dot);

        tvSearch.setTypeface(font);
        images = new ArrayList<>();
        ProductTypes = new ArrayList<>();
        ProductMakes = new ArrayList<>();
        ProductColours = new ArrayList<>();
        Prices = new ArrayList<>();
        OtherDescriptions = new ArrayList<>();
        ids = new ArrayList<>();
        ProductIds = new ArrayList<>();
        Periods = new ArrayList<>();


        //Initializing Owners' arrays*******************************************************

        OwnerNames = new ArrayList<>();
        OwnerEmails = new ArrayList<>();
        OwnerAddresses = new ArrayList<>();
        OwnerCampuses = new ArrayList<>();
        ProfPicUrls = new ArrayList<>();

        textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);


        final long delay = 1000; // 1 seconds after user stops typing
        final long[] last_text_edit = {0};


        final Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    // TODO: do what you need here
                    // ............
                    // ............
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
                    OwnerEmails.clear();
                    OwnerAddresses.clear();
                    OwnerCampuses.clear();
                    ProfPicUrls.clear();
                    generalSearch(CurrentlyTyped) ;
                    swipeRefreshLayout.setRefreshing(false);


                }
            }
        };

        final Handler handler = new Handler();
        textView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {

                CurrentlyTyped = s.toString().trim();
                CurrentlyTyped =CurrentlyTyped.toLowerCase();

                /*  search(CurrentlyTyped) ;*/

                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit[0] = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }


            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

//You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);

            }
        });


        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = textView.getText().toString().trim();
                //generalSearch(key);

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
                OwnerEmails.clear();
                OwnerAddresses.clear();
                OwnerCampuses.clear();
                ProfPicUrls.clear();
                generalSearch(key);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //getPredictedSearch();




        inflater2=this.getLayoutInflater();
        searchProductView= inflater2.inflate(R.layout.product_search_view, null);

        tvCount=(TextView) findViewById(R.id.count);

        // searchProductDialog();

        stcategory=getIntent().getStringExtra("Category");


        getCategory(stcategory);


        dContext=ProductSearchResults.this;

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

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
                OwnerEmails.clear();
                OwnerAddresses.clear();
                OwnerCampuses.clear();
                ProfPicUrls.clear();
                getCategory(stcategory);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }






    private void searchProduct(final String productType, final String productMake, final String productColour, final String price, final String otherDescriptions, final String campus){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.search_products, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("tata", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    populateSearch(array);

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
                params.put("ProductType",productType);
                params.put("ProductMake",productMake);
                params.put("ProductColour",productColour);
                params.put("Price",price);
                params.put("OtherDescriptions",otherDescriptions);
                params.put("Campus",campus);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void populateSearch(JSONArray jsonArray){
        //Looping through all the elements of json array

        for(i = 0; i<jsonArray.length(); i++){
            tvCount.setText(String.valueOf(i+1));
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
                OwnerEmails.add(obj.getString(TAG_OWNER_EMAIL));
                OwnerAddresses.add(obj.getString(TAG_OWNER_ADDRESS));
                OwnerCampuses.add(obj.getString(TAG_OWNER_CAMPUS));
                ProfPicUrls.add(obj.getString(TAG_PROFPIC_URL));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        lLayout = new GridLayoutManager(getBaseContext(), 2);
        // LayoutInflater inflater =(LayoutInflater)dContext.getSystemService(dContext.LAYOUT_INFLATER_SERVICE);


        rView = (RecyclerView)findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapterTwo rcAdapter = new RecyclerViewAdapterTwo(getBaseContext(),images,ids,ProductTypes,ProductMakes,ProductColours,Prices,OtherDescriptions,ProductIds,OwnerNames,OwnerEmails,OwnerAddresses,OwnerCampuses,Periods,ProfPicUrls,list);
        rView.setAdapter(rcAdapter);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void searchProductDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        searchProductType = (EditText) searchProductView.findViewById(R.id.et_product_type);
        searchProductMake = (EditText) searchProductView.findViewById(R.id.et_product_make);
        searchProductColour = (EditText) searchProductView.findViewById(R.id.et_product_colour);
        searchPrice = (EditText) searchProductView.findViewById(R.id.et_price);
        searchProductOtherDescriptions = (EditText) searchProductView.findViewById(R.id.et_other_descriptions);
        searchCampus = (EditText) searchProductView.findViewById(R.id.et_campus);
        searchButtonSubmit=(Button)searchProductView.findViewById(R.id.bt_submit_product);
        searchButtonCancel=(Button)searchProductView.findViewById(R.id.bt_cancel_product);



        builder.setView(searchProductView);
        final AlertDialog alertDialog = builder.create();
        searchButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                productType=searchProductType.getText().toString();
                productMake=searchProductMake.getText().toString();
                productColour=searchProductColour.getText().toString();

                price=searchPrice.getText().toString();
                otherDescriptions=searchProductOtherDescriptions.getText().toString();
                campus=searchCampus.getText().toString();


                if(productType.isEmpty())
                {
                    productType="" ;
                }
                else if(productMake.isEmpty())
                {
                    productMake="" ;
                }
                else if(productColour.isEmpty())
                {
                    productColour="" ;
                }
                else if(price.isEmpty())
                {
                    price="" ;
                }
                else if(otherDescriptions.isEmpty())
                {
                    otherDescriptions="" ;
                }
                else if(campus.isEmpty())
                {
                    campus="" ;
                }


                searchProduct(productType,productMake,productColour,price,otherDescriptions,campus);



                alertDialog.cancel();

            }
        });
        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");

        alertDialog.show();
    }


    private void getCategory(final String category){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_category, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("tata", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    populateCategory(array);

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
                params.put("Category",category);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void populateCategory(JSONArray jsonArray){
        //Looping through all the elements of json array

        for(i = 0; i<jsonArray.length(); i++){
            tvCount.setText(String.valueOf(i+1));
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
                OwnerEmails.add(obj.getString(TAG_OWNER_EMAIL));
                OwnerAddresses.add(obj.getString(TAG_OWNER_ADDRESS));
                OwnerCampuses.add(obj.getString(TAG_OWNER_CAMPUS));
                ProfPicUrls.add(obj.getString(TAG_PROFPIC_URL));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        lLayout = new GridLayoutManager(getBaseContext(), 2);
        // LayoutInflater inflater =(LayoutInflater)dContext.getSystemService(dContext.LAYOUT_INFLATER_SERVICE);


        rView = (RecyclerView)findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapterTwo rcAdapter = new RecyclerViewAdapterTwo(getBaseContext(),images,ids,ProductTypes,ProductMakes,ProductColours,Prices,OtherDescriptions,ProductIds,OwnerNames,OwnerEmails,OwnerAddresses,OwnerCampuses,Periods,ProfPicUrls,list);
        rView.setAdapter(rcAdapter);
    }


    public void getPredictedSearch(){


        final String[] search = new String[900000];






        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_predicted_search, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("hhh", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray= object.getJSONArray("result");
                    // showGrid(array);


                    for(int i = 0; i<jsonArray.length(); i++){
                        //Creating a json object of the current index
                        JSONObject obj = null;

                        try {
                            //getting json object from current index
                            obj = jsonArray.getJSONObject(i);
                            search[i]=obj.getString("ProductType");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //COUNTRIES=search;
                    assignArray(search);
                    Log.d("ccc",String.valueOf(search[0]));
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

        // String[] COUNTRIES = new String[900000000];
        //Looping through all the elements of json array

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;

            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                COUNTRIES[i]=obj.getString("ProductType");
                //getting image url and title from json object
               /* images.add(obj.getString(TAG_IMAGE_URL));
                ProductTypes.add(obj.getString(TAG_PRODUCT_TYPE));
                ProductMakes.add(obj.getString(TAG_PRODUCT_MAKE));
                ProductColours.add(obj.getString(TAG_PRODUCT_COLOUR));
                Prices.add(obj.getString(TAG_PRICE));
                OtherDescriptions.add(obj.getString(TAG_OTHER_DESCRIPTIONS));
                ids.add(obj.getString(TAG_ID));
                ProductIds.add(obj.getString(TAG_PRODUCT_ID));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }



    public void assignArray(String[] COUNTRIESz)
    {

        final String[] COUNTRIES = new String[]{"Short title", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer", "Accounting officer"};

        //String huyu="Silas";

        // COUNTRIES[1]=huyu;
        for(int i=0;i<COUNTRIES.length;i++)
        {
            COUNTRIES[i]=COUNTRIESz[i];
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        Log.d("ddd",String.valueOf(COUNTRIES[1]));
        textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        textView.setAdapter(adapter);
    }


    private void generalSearch(final String Key){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.general_search, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("tata", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    populategeneralSearch(array);

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
                params.put("Key",Key);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void populategeneralSearch(JSONArray jsonArray){
        //Looping through all the elements of json array

        for(i = 0; i<jsonArray.length(); i++){
            tvCount.setText(String.valueOf(i+1));
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

                //Gettingt Owner details*********************************************************

                OwnerNames.add(obj.getString(TAG_OWNER_NAME));
                OwnerEmails.add(obj.getString(TAG_OWNER_EMAIL));
                OwnerAddresses.add(obj.getString(TAG_OWNER_ADDRESS));
                OwnerCampuses.add(obj.getString(TAG_OWNER_CAMPUS));
                ProfPicUrls.add(obj.getString(TAG_PROFPIC_URL));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        lLayout = new GridLayoutManager(getBaseContext(), 2);
        // LayoutInflater inflater =(LayoutInflater)dContext.getSystemService(dContext.LAYOUT_INFLATER_SERVICE);


        rView = (RecyclerView)findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapterTwo rcAdapter = new RecyclerViewAdapterTwo(getBaseContext(),images,ids,ProductTypes,ProductMakes,ProductColours,Prices,OtherDescriptions,ProductIds,OwnerNames,OwnerEmails,OwnerAddresses,OwnerCampuses,Periods,ProfPicUrls,list);
        rView.setAdapter(rcAdapter);
    }

    private void colourPink() {

        String key;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.colour_pink, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {




                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray = object.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonArray.getJSONObject(i);


                            list.add(obj.getString("ProductId"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

                return stringMap;
            }
        };
        Volley.newRequestQueue(getBaseContext()).add(stringRequest);


    }

    public void checkConnection()
    {boolean connected;
        try {
            connected= isConnected();

            if(connected==true)
            {colourPink();
                getCategory(stcategory);
                tvBackgroundIcon.setVisibility(View.GONE);
                tvBackgroundText.setVisibility(View.GONE);}
            else{
                final Animation animShake = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake_card);
                tvBackgroundIcon.setVisibility(View.VISIBLE);
                tvBackgroundText.setVisibility(View.VISIBLE);
                tvBackgroundText.setText("No internet connection");
                tvBackgroundIcon.setText(getResources().getString(R.string.net));
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
