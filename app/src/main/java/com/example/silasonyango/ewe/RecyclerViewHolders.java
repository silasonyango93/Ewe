package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SilasOnyango on 3/2/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{


    AlertDialog.Builder builder;
    public String ProductId;
    public Context mContext;
    CheckBox checkBook;
    NetworkImageView largeImage;
    public String LargeImageUrl;
    public ImageLoader imageLoader;
    public TextView Product;
    public TextView Price;
    public NetworkImageView ProductImage;

    LayoutInflater inflater;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        mContext=GeneralPublic.context;
        inflater =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        itemView.setOnClickListener(this);
        Product = (TextView)itemView.findViewById(R.id.product);
        Price = (TextView)itemView.findViewById(R.id.price);
        ProductImage = (NetworkImageView)itemView.findViewById(R.id.product_image);



    }



    @Override
    public void onClick(View view) {
        View v = inflater.inflate(R.layout.large_render,null);
        NetworkImageView largeImage = (NetworkImageView) v.findViewById(R.id.large_render);
        TextView tvProductType=(TextView)v.findViewById(R.id.tv_product_type);
        TextView tvProductMake=(TextView)v.findViewById(R.id.tv_product_make);
        TextView tvProductColour=(TextView)v.findViewById(R.id.tv_colour);
        TextView tvPrice=(TextView)v.findViewById(R.id.tv_price);
        TextView tvOtherDescriptions=(TextView)v.findViewById(R.id.tv_other_descriptions);

        checkBook=(CheckBox)v.findViewById(R.id.check_book);




        getLargeImage(view.getContext(),largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions);
        renderLargeImage(view.getContext(),v);


        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void renderLargeImage(final Context mContext, View v) {
       builder = new AlertDialog.Builder(mContext);

        builder.setView(v);

       checkBook.setOnClickListener(new View.OnClickListener() {

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

    private void getLargeImage(Context context, final NetworkImageView largeImage, final TextView tvProductType, final TextView tvProductMake, final TextView tvProductColour, final TextView tvPrice, final TextView tvOtherDescriptions){
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
                    inflateLargeImage(array,largeImage,tvProductType,tvProductMake,tvProductColour,tvPrice,tvOtherDescriptions);

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

    private void inflateLargeImage(JSONArray jsonArray, NetworkImageView largeImage, TextView tvProductType, TextView tvProductMake, TextView tvProductColour, TextView tvPrice, TextView tvOtherDescriptions){
        //Looping through all the elements of json array

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


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("url",ImageUrl);
        imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
        imageLoader.get(LargeImageUrl, ImageLoader.getImageListener(largeImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        largeImage.setImageUrl(LargeImageUrl,imageLoader);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

}