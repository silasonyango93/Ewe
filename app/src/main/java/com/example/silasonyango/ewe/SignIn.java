package com.example.silasonyango.ewe;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 2/20/2016.
 */
public class SignIn extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper myDb;
    TextView tvPrompt;
    ImageView img;
    EditText et_email, et_password;
    private RequestPermissionHandler mRequestPermissionHandler;
    SessionManager sessionManager;
    ProgressDialog dialog;
    private String email;
    private String password;

    public  String UserId, UserName, Addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getBaseContext());
        setContentView(R.layout.login);
        myDb = new DatabaseHelper(getBaseContext());
        tvPrompt=(TextView) findViewById(R.id.prompt);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "JosefinSans-Light.ttf");
        mRequestPermissionHandler = new RequestPermissionHandler();
        // tvConservation.setTypeface(custom_font);
        tvPrompt.setTypeface(custom_font);

        img=(ImageView) findViewById(R.id.img1);

        img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.shop,  700, 700));

        findViewById(R.id.btn_submit_login).setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        et_email = (EditText) findViewById(R.id.et_emailLogin);
        et_password = (EditText) findViewById(R.id.et_passwordLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_login:
                if (checkEmpty()) {
                    checkContactAccessPermission();

                }
                break;

            // startActivity(in);
        }
    }

    private boolean checkEmpty() {
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        et_email.setError(null);
        et_password.setError(null);
        if (email.isEmpty()) {
            et_email.setError(getString(R.string.error_email_required));
            et_email.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            et_password.setError(getString(R.string.error_password_required));
            et_password.requestFocus();
            return false;
        } else {
            return true;

        }

    }

    private void SignIn(final String email, final String password) {
        String tag_string_req = "req_login";
        dialog.setMessage("Signing in ...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (error) {//When response returns error
                        String errorMessage = jObj.getString("error_msg");
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        hideDialog();
                    } else {



                        String id = jObj.getString("id");
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String address = jObj.getString("Address");
                        String Key="User";
                        updateCredentials(id,name,email,Key,address);
                        showMessage("Good News","Data inserted into SQLite");



                        Intent intent = new Intent(
                                getBaseContext(),Landing.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("access","signin");
                        startActivity(intent);



                        sessionManager.setLogin(true);
                        hideDialog();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    hideDialog();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        Mimi.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}



    public void updateCredentials(String id,String name,String email,String Key,String address) {

        Cursor res = myDb.getAllCredentials();

        if (res.getCount() == 0) {
            myDb.insertData(id,name,email,Key,address);
            return;
        }else{myDb.updateCredentials(id,name,email,Key,address);}


myDb.insertChatWallStatus("OffChatWall","RowKey");
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

    private void checkContactAccessPermission(){
        mRequestPermissionHandler.requestPermission(this, new String[] {
                Manifest.permission.READ_CONTACTS
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                SignIn(email, password);
            }

            @Override
            public void onFailed() {
                showMessage("Permission Denied","Allowing Klabuu to access your Contacts is to help give you a more personalised and better experience while using the app");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }




}
