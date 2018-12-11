package com.example.silasonyango.ewe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

public class ShopsRVHoldersTwo extends RecyclerView.ViewHolder implements View.OnClickListener{
    public NetworkImageView ProductImage;
    MyNetworkImageView NIvprofPic;
    public Context mContext;
    LayoutInflater inflater,inflater1;

    public ShopsRVHoldersTwo(View itemView,Context bContext) {
        super(itemView);
        mContext=bContext;
        inflater =(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        itemView.setOnClickListener(this);

        ProductImage = (NetworkImageView)itemView.findViewById(R.id.product_image);


    }
    @Override
    public void onClick(View view) {

    }

}
