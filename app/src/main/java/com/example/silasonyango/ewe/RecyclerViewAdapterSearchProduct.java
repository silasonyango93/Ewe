package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by SilasOnyango on 3/16/2017.
 */

public class RecyclerViewAdapterSearchProduct extends RecyclerView.Adapter<RecyclerViewHoldersSearchProduct> {

    private Context context;
    //Imageloader to load images
    private ImageLoader imageLoader,imageLoader2;
    public NetworkImageView networkImageView;

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


    public RecyclerViewAdapterSearchProduct(Context dContext ,ArrayList<String> images, ArrayList<String> ids, ArrayList<String> productTypes, ArrayList<String> productMakes, ArrayList<String> productColours, ArrayList<String>  prices, ArrayList<String> otherDescriptions, ArrayList<String> productIds, ArrayList<String> periods) {


        this.context=dContext;
        this.images = images;
        this.ProductTypes = productTypes;
        this.ProductMakes = productMakes;
        this.ProductColours = productColours;
        this.Prices = prices;
        this.OtherDescriptions = otherDescriptions;
        this.ids = ids;
        this.ProductIds = productIds;
        this.Periods = periods;


    }

    @Override
    public RecyclerViewHoldersSearchProduct onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.card_view_list_search,null);
        RecyclerViewHoldersSearchProduct rcv = new RecyclerViewHoldersSearchProduct(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersSearchProduct holder, int position) {
        holder.Product.setText(ProductTypes.get(position));
        holder.Price.setText(Prices.get(position));
        holder.Period.setText(Periods.get(position));
        holder.ProductId=(ProductIds.get(position));

        //holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());


        //Initializing ImageLoader
        imageLoader = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(holder.ProductImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.ProductImage.setImageUrl(images.get(position),imageLoader);



     /* imageLoader2 = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader2.get(images.get(position), ImageLoader.getImageListener(holder.largeImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.largeImage.setImageUrl(images.get(position),imageLoader2);*/
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

