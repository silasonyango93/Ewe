package com.example.silasonyango.ewe;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecyclerViewAdapterChat extends RecyclerView.Adapter<RecyclerViewHoldersChat> {
    private Context context;
    DatabaseHelper myDb;
    String  UserId,Real;
    private int SELF = 100;
    private ArrayList<String> SenderIds;
    private ArrayList<String> Messages;
    private ArrayList<String> Periods;

    public RecyclerViewAdapterChat(Context context, ArrayList<String> SenderIds, ArrayList<String> Messages, ArrayList<String> Periods) {
        // this.itemList = itemList;

        this.context = context;
        this.SenderIds = SenderIds;
        this.Messages = Messages;
        this.Periods = Periods;
        myDb = new DatabaseHelper(context);
        viewSpecificData();

    }

    @Override
    public RecyclerViewHoldersChat onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        // View layoutView = LayoutInflater.from(context).inflate(R.layout.chat_item_self,null);
        if (viewType == SELF) {
            // self message
            layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        RecyclerViewHoldersChat rcv = new RecyclerViewHoldersChat(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersChat holder, int position) {
        Log.e("Adapater-BVH->",""+position);

        holder.tvMessage.setText(Messages.get(position));
        holder.tvPeriod.setText(Periods.get(position));
        holder.SenderId=(SenderIds.get(position));

    }

    @Override
    public int getItemCount() {
        Log.e("Adapater-->",""+this.Messages.size());
        return this.Messages.size();
    }

    /*   @Override
       public Object getItem(int position) {
           return images.get(position);
       }*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // Message message = messageArrayList.get(position);

       // String temp="89";
        if(SenderIds.get(position).equals(UserId)) {
            return SELF;
        }

        return position;
    }



    public void viewSpecificData() {

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
            buffer.append("Email : " + res.getString(3) + "\n");
            buffer.append("Key : " + res.getString(4) + "\n");
            // buffer.append("Address : " + res.getString(5) + "\n\n");

            UserId = res.getString(1);


            //Addrr.setText(Addr);
        }

        //Show all data


        //showMessage("Data", buffer.toString());

        //Addrr.setText(Addr);

    }

    public void setItems(String SenderId,String Message,String Period) {
        SenderIds.add(SenderId);
        Messages.add(Message);
        Periods.add(Period);
    }





}

