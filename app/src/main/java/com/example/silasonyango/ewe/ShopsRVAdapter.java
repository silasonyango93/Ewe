package com.example.silasonyango.ewe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class ShopsRVAdapter extends RecyclerView.Adapter<ShopsRVHoldersTwo> {
    private ImageLoader imageLoader,imageLoader2;
    public NetworkImageView networkImageView;
    private ArrayList<String> images;
    private Context context;

    public ShopsRVAdapter(Context context, ArrayList<String> images) {
        // this.itemList = itemList;
        this.context = context;
        this.images = images;

    }

    @Override
    public ShopsRVHoldersTwo onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.shop_card,null);
        ShopsRVHoldersTwo rcv = new ShopsRVHoldersTwo(layoutView,this.context);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ShopsRVHoldersTwo holder, int position) {

        imageLoader = MCustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(holder.ProductImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.ProductImage.setImageUrl(images.get(position),imageLoader);

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

}
