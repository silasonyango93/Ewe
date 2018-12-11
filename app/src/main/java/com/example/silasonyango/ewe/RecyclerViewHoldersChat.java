package com.example.silasonyango.ewe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewHoldersChat extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView tvMessage,tvPeriod;
    String SenderId;

    public RecyclerViewHoldersChat(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        tvMessage = (TextView)itemView.findViewById(R.id.message);
        tvPeriod = (TextView)itemView.findViewById(R.id.timestamp);





    }

    @Override
    public void onClick(View view) {

       // Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
