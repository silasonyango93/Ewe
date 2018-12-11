package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SilasOnyango on 3/10/2017.
 */

public class RecyclerViewAdapterTwo extends RecyclerView.Adapter<RecyclerViewHoldersTwo> {

public String strAcknowledgment;
    private Context context;
    //Imageloader to load images
    private ImageLoader imageLoader,imageLoader2;
    public NetworkImageView networkImageView;

    //Context


    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> ProductTypes;
    private ArrayList<String> ProductMakes;
    private ArrayList<String> ProductColours;
    private ArrayList<String> Prices;
    private ArrayList<String> OtherDescriptions;
    private ArrayList<String> ids;
    private ArrayList<String> ProductIds;
    private ArrayList<String> Periods;



//Product owner's details****************************************

    private ArrayList<String> OwnerNames;
    private ArrayList<String> OwnerEmails;
    private ArrayList<String> OwnerAddresses;
    private ArrayList<String> OwnerCampuses;
    private ArrayList<String> ProfPicUrls;


    //For colouring Booked products pink

    private ArrayList<String> BookedProductIds;

    public RecyclerViewAdapterTwo(Context context, ArrayList<String> images, ArrayList<String> ids, ArrayList<String> productTypes, ArrayList<String> productMakes, ArrayList<String> productColours, ArrayList<String>  prices, ArrayList<String> otherDescriptions, ArrayList<String> productIds, ArrayList<String> ownerNames, ArrayList<String> ownerEmails, ArrayList<String> ownerAddresses, ArrayList<String> ownerCampuses, ArrayList<String> periods, ArrayList<String> profPicUrls, ArrayList<String> BookedProductIds) {
        // this.itemList = itemList;
        this.context = context;
        this.images = images;
        this.ProductTypes = productTypes;
        this.ProductMakes = productMakes;
        this.ProductColours = productColours;
        this.Prices = prices;
        this.OtherDescriptions = otherDescriptions;
        this.ids = ids;
        this.ProductIds = productIds;
        this.Periods = periods;
        this.ProfPicUrls = profPicUrls;

        //Owner details*******************************************************

        this.OwnerNames = ownerNames;
        this.OwnerEmails = ownerEmails;
        this.OwnerAddresses = ownerAddresses;
        this.OwnerCampuses = ownerCampuses;


        //Ids for products to be painted pink
        this.BookedProductIds = BookedProductIds;


    }
    @Override
    public RecyclerViewHoldersTwo onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.card_view_list,null);
        RecyclerViewHoldersTwo rcv = new RecyclerViewHoldersTwo(layoutView,this.context);

        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersTwo holder, int position) {
String key="present";

for(int i=0;i<BookedProductIds.size();i++) {

    if(ProductIds.get(position).equals(BookedProductIds.get(i)))
{holder.tvHeartIcon.setTextColor(context.getResources().getColor(R.color.pink));

}

}





        holder.Product.setText(ProductTypes.get(position));
        holder.Price.setText(Prices.get(position));
        holder.Period.setText(Periods.get(position));
        holder.tvUserName.setText(OwnerNames.get(position));
        holder.ProductId=(ProductIds.get(position));


        //holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());


        //Initializing ImageLoader
        imageLoader = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(holder.ProductImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.ProductImage.setImageUrl(images.get(position),imageLoader);



     imageLoader2 = MCustomVolleyRequest.getInstance(context).getImageLoader();
     imageLoader2.get(ProfPicUrls.get(position), ImageLoader.getImageListener(holder.NIvprofPic, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.NIvprofPic.setImageUrl(ProfPicUrls.get(position),imageLoader2);


    }

    @Override
    public int getItemCount() {

        return this.images.size();
    }

 /*   @Override
    public Object getItem(int position) {
        return images.get(position);
    }*/

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}


}

