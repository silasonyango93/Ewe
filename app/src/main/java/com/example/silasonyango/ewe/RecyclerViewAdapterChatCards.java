package com.example.silasonyango.ewe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecyclerViewAdapterChatCards extends RecyclerView.Adapter<RecyclerViewHoldersChatCards> {
    private Context context;
    private String ProductId;
    private ArrayList<String> FullNames;
    private ArrayList<String> Ids;

    public RecyclerViewAdapterChatCards(Context context, ArrayList<String> FullNames, ArrayList<String> Ids,String ProductId) {

        this.context = context;
        this.FullNames = FullNames;
        this.Ids = Ids;
        this.ProductId = ProductId;

    }

    @Override
    public RecyclerViewHoldersChatCards onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.chat_card,null);
        RecyclerViewHoldersChatCards rcv = new RecyclerViewHoldersChatCards(layoutView,this.context);

        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersChatCards holder, int position) {
        holder.tvName.setText(FullNames.get(position));
        holder.SenderId=(Ids.get(position));
        holder.ProductId=this.ProductId;

    }

    @Override
    public int getItemCount() {

        return this.FullNames.size();
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

