package com.example.silasonyango.ewe;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.RequestHandler;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by hp on 3/2/2016.
 */
public class Landing extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context dContext;
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ID = "id";
    Boolean isConnected=false;
    //Variables for Dialog Sell room**********************
    EditText etHostelName,etRoomPrice,etRoomOtherDescriptions;
    Button btSellRoomSubmit,btSellRoomCancel;
    final Handler handler = new Handler();

    //****************************************************

    //Variables for Dialog buy room*************************************
    EditText etHostelNameBuy,etRoomPriceBuy,etRoomOtherDescriptionsBuy;
    Button btSellRoomSubmitBuy,btSellRoomCancelBuy;

    private TabLayout tabLayout;
    private ViewPager viewPager;



    View sellRoomView,buyRoomView,searchProductView;
    LayoutInflater inflater,inflater1,inflater2;
    String ImageUrl,access;
    DatabaseHelper myDb;
    NavigationView navigationView;
    DrawerLayout drawer;
    // String DATA_URL="http://192.168.43.118/PhotoUpload/getProfPic.php";
    NetworkImageView profPic;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    private static final String TAG = "MyFirebaseInsIDService";
    public static String UserId, UserName, Addr;
    //Uri to store the image uri
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    TextView profText, tvAddrr;
    public static Context mContext;
    //Imageloader to load images
    public ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getBaseContext();
        setContentView(R.layout.landing);

        myDb = new DatabaseHelper(this);
        viewSpecificData();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        checkConnection();







        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView1.getHeaderView(0);
        profText = (TextView) v.findViewById(R.id.name);
        profPic = (NetworkImageView) v.findViewById(R.id.ProfPic);
        tvAddrr = (TextView) v.findViewById(R.id.adrr);

        DrawerLayout drawer2 = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer2.openDrawer(Gravity.LEFT);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this, UploadProducts.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        inflater=this.getLayoutInflater();
        inflater1=this.getLayoutInflater();


dContext=Landing.this;


        access=getIntent().getStringExtra("access");

        String comp="signin";

        if(access.equals(comp))
        {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "New Token: " + refreshedToken);
            sendToken(refreshedToken);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "MY UPLOADS");
        adapter.addFragment(new TwoFragment(), "BOOKED");
        adapter.addFragment(new ThreeFragment(), "PUBLIC");
        viewPager.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sellRoom) {
            sellRoomView= inflater.inflate(R.layout.sell_room_view, null);
            etHostelName = (EditText) sellRoomView.findViewById(R.id.et_hostel_name);
            etRoomPrice = (EditText) sellRoomView.findViewById(R.id.et_price);
            etRoomOtherDescriptions= (EditText) sellRoomView.findViewById(R.id.et_other_descriptions);
            btSellRoomSubmit = (Button) sellRoomView.findViewById(R.id.bt_submit_sell_room);
            btSellRoomCancel = (Button) sellRoomView.findViewById(R.id.bt_cancel_sell_room);
            sellRoomDialog();


        } else if (id == R.id.buyRoom) {
            buyRoomView= inflater1.inflate(R.layout.buy_room_view, null);
            etHostelNameBuy = (EditText) buyRoomView.findViewById(R.id.et_hostel_name);
            etRoomPriceBuy = (EditText) buyRoomView.findViewById(R.id.et_price);
            etRoomOtherDescriptionsBuy= (EditText) buyRoomView.findViewById(R.id.et_other_descriptions);
            btSellRoomSubmitBuy = (Button) buyRoomView.findViewById(R.id.bt_submit_sell_room);
            btSellRoomCancelBuy = (Button) buyRoomView.findViewById(R.id.bt_cancel_sell_room);
            buyRoomDialog();
        } else if (id == R.id.customizedSearch) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            startActivity(intent);
        } else if (id == R.id.electronics) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Electronics");
            startActivity(intent);
        } else if (id == R.id.men_clothing) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Clothing for men");
            startActivity(intent);
        } else if (id == R.id.women_clothing) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Clothing for ladies");
            startActivity(intent);
        }else if (id == R.id.men_footwear) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Footwear for men");
            startActivity(intent);
        }else if (id == R.id.women_footwear) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Footwear for ladies");
            startActivity(intent);
        }else if (id == R.id.stationery) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Stationery");
            startActivity(intent);
        }else if (id == R.id.bedding) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Bedding");
            startActivity(intent);
        }else if (id == R.id.utensils) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Kitchen utensils");
            startActivity(intent);
        }else if (id == R.id.laundry) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Laundry items");
            startActivity(intent);
        }else if (id == R.id.household) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Household items");
            startActivity(intent);
        }else if (id == R.id.food) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Food and ingredients");
            startActivity(intent);
        }else if (id == R.id.airtime) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Phone Airtime");
            startActivity(intent);
        }else if (id == R.id.cosmetics) {


            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Ornaments and Cosmetics for ladies");
            startActivity(intent);
        }else if (id == R.id.regalia) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Regalia for men e.g watches,bungles,neclaces");
            startActivity(intent);
        }else if (id == R.id.any_other) {

            Intent intent =new Intent(Landing.this,ProductSearchResults.class);
            intent.putExtra("Category","Any other");
            startActivity(intent);
        }else if (id == R.id.nav_send) {

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* @Override
    public void onFragmentInteraction(Uri uri) {

    }*/

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void getProfPic() {
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Prof_Pic_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("result");
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("id", UserId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(getBaseContext()).add(stringRequest);
    }


    private void showGrid(JSONArray jsonArray) {
        //Looping through all the elements of json array

        for (int i = 0; i < jsonArray.length(); i++) {
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                ImageUrl = obj.getString("url");
                profText.setText(UserName);
                tvAddrr.setText(Addr);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("url",ImageUrl);
        imageLoader = CustomVolleyRequest.getInstance(getBaseContext()).getImageLoader();
        imageLoader.get(ImageUrl, ImageLoader.getImageListener(profPic, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        profPic.setImageUrl(ImageUrl, imageLoader);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void myCustomErrorMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //  imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void uploadImage() {

        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Landing.this, "Please wait...", "uploading", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Landing.this, s, Toast.LENGTH_LONG).show();
                getProfPic();
            }

            @Override
            protected String doInBackground(Void... params) {
                com.example.silasonyango.ewe.RequestHandler rh = new com.example.silasonyango.ewe.RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put(KEY_ID, UserId);
                param.put(KEY_IMAGE, image);
                String result = rh.sendPostRequest(Config.change_prof_pic_url, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    public void viewSpecificData() {

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void sellRoom(final String hostelName, final String roomPrice, final String roomOtherDescriptions) {


        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.sell_room, new Response.Listener<String>() {
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
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserId);
                params.put("HostelName", hostelName);
                params.put("Price", roomPrice);
                params.put("OtherDescriptions", roomOtherDescriptions);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void sellRoomDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(sellRoomView);
        final AlertDialog alertDialog = builder.create();
        btSellRoomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String HostelName,roomPrice,RoomOtherDescriptions;

                HostelName=etHostelName.getText().toString();
                roomPrice=etRoomPrice.getText().toString();
                RoomOtherDescriptions=etRoomOtherDescriptions.getText().toString();

                sellRoom( HostelName,roomPrice,RoomOtherDescriptions);
                alertDialog.cancel();

            }
        });
        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");

        alertDialog.show();





    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void buyRoomDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(buyRoomView);
        final AlertDialog alertDialog = builder.create();
        btSellRoomSubmitBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String HostelName,roomPrice,RoomOtherDescriptions;

                HostelName=etHostelNameBuy.getText().toString();
                roomPrice=etRoomPriceBuy.getText().toString();
                RoomOtherDescriptions=etRoomOtherDescriptionsBuy.getText().toString();

                buyRoom( HostelName,roomPrice,RoomOtherDescriptions);
                alertDialog.cancel();

            }
        });
        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");

        alertDialog.show();





    }

    public void buyRoom(final String hostelName, final String roomPrice, final String roomOtherDescriptions) {


        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.buy_room, new Response.Listener<String>() {
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
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserId);
                params.put("HostelName", hostelName);
                params.put("Price", roomPrice);
                params.put("OtherDescriptions", roomOtherDescriptions);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void sendToken(final String refreshedToken){
        //Showing a progress dialog while our app fetches the data from url


        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.update_token, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


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



                stringMap.put("token_value",refreshedToken);
                stringMap.put("UserId",UserId);


                //Log.d("pupu", "pupu:"+ProductId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public static boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }

 

public void checkConnection()
{boolean connected;
    try {
        connected= isConnected();

        if(connected==true)
        {getProfPic();}
        else{ handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkConnection();
            }
        }, 2000);}
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }




}


}

