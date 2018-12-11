package com.example.silasonyango.ewe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatWall extends AppCompatActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // DO YOUR STUFF
            getMessages();

           // Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        }
    };
    IntentFilter filter = new IntentFilter();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    DatabaseHelper myDb;
    public static Context context;
    int i;
    EditText etMessage;
    Button btSend;

    private ArrayList<String> SenderIds;
    private ArrayList<String> Messages;
    private ArrayList<String> Periods;

    public static final String TAG_SENDER_ID = "SenderId";
    public static final String TAG_MESSAGE = "Message";
    public static final String TAG_PERIOD = "Period";

    public Context dContext;

    String ProductOwnerId,ProductId;
    RecyclerViewAdapterChat rcAdapter;

    String UserId,UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_wall);


        ProductOwnerId =getIntent().getStringExtra("ProductOwnerId");
        ProductId=getIntent().getStringExtra("ProductId");

        myDb = new DatabaseHelper(getBaseContext());
        getSQLiteData();
        SenderIds = new ArrayList<>();
        Messages = new ArrayList<>();
        Periods = new ArrayList<>();
        etMessage=(EditText)findViewById(R.id.message) ;
        btSend=(Button) findViewById(R.id.btn_send);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strMessage=etMessage.getText().toString();
                sendMessage(strMessage);
            }
        });

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SenderIds.clear();
                Messages.clear();
                Periods.clear();
                getMessages();

                // getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        dContext=ChatWall.this;
        getMessages();
    }

    private void getMessages(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_messages_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

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
                params.put("UserOne",UserId);
                params.put("UserTwo",ProductOwnerId);
                params.put("ProductId",ProductId);


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

            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);


                //getting image url and title from json object

                SenderIds.add(obj.getString(TAG_SENDER_ID));
                Messages.add(obj.getString(TAG_MESSAGE));
                Periods.add(obj.getString(TAG_PERIOD));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        // lLayout = new GridLayoutManager(getBaseContext(), 1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        // LayoutInflater inflater =(LayoutInflater)dContext.getSystemService(dContext.LAYOUT_INFLATER_SERVICE);


        rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(layoutManager);

        rcAdapter = new RecyclerViewAdapterChat(getBaseContext(),SenderIds,Messages,Periods);
        rView.setAdapter(rcAdapter);
    }
    private void sendMessage(final String strMessage){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.send_message, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
getMessages();
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
                params.put("SenderId",UserId);
                params.put("RecepientId",ProductOwnerId);
                params.put("ProductId",ProductId);
                params.put("Message",strMessage);
                params.put("UserName",UserName);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);
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
            UserName=res.getString(2);
        }

        //Show all data

        // showMessage("Data", buffer.toString());


    }

    @Override
    public void onStart() {
        super.onStart();

        myDb.updateChatWallStatus("InsideChatWall","RowKey");
    }

    @Override
    public void onStop() {
        super.onStop();
        myDb.updateChatWallStatus("OffChatWall","RowKey");
    }

    @Override
    public void onResume() {
        super.onResume();
        filter.addAction("com.silasonyango.CUSTOM_CHAT_INTENT");
        registerReceiver(receiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
                Log.w("Rara","Tried to unregister the reciver when it's not registered");
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
    }

}
