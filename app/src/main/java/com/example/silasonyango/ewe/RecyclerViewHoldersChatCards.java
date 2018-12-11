package com.example.silasonyango.ewe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RecyclerViewHoldersChatCards extends RecyclerView.ViewHolder implements View.OnClickListener {
    public Context mContext;
    public String UserId,SenderId;
    TextView tvName;
    LayoutInflater inflater;
    String ProductId;

    public RecyclerViewHoldersChatCards(View itemView,Context gContext) {
        super(itemView);
        mContext = gContext;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);


        itemView.setOnClickListener(this);

        tvName = (TextView) itemView.findViewById(R.id.tv_name);

    }

    @Override
    public void onClick(View view) {
        Intent o=new Intent(mContext,ChatWall.class);
        o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        o.putExtra("ProductOwnerId",SenderId);
        o.putExtra("ProductId",ProductId);
        mContext.startActivity(o);
    }
}

