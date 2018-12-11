package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MyContactAdapter extends ArrayAdapter<Contacts> {
    LayoutInflater inflater;
    List<Contacts> contactList;
    Context context;
    CheckBox checkBook,checkBook1;
    int resource;
    AlertDialog alertDialog;
    String strPageContent;


    public MyContactAdapter(Context context, int resource, List<Contacts> contactList) {
        super(context, resource, contactList);
        this.context = context;
        this.resource = resource;
        this.contactList = contactList;
        this.strPageContent = strPageContent;

        inflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        //ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
        checkBook=(CheckBox)view.findViewById(R.id.chBox);

        final Contacts contact = contactList.get(position);

        checkBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });

        //getting the hero of the specified position


        //adding values to the list item
        //imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));



        tvName.setText(contact.getName());
        tvNumber.setText(contact.getPhoneNumber());


        //adding a click listener to the button to remove item from the list

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


        //finally returning the view
        return view;
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void previewUpload(View v) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }
}
