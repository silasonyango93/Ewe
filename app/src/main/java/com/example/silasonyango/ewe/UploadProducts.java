package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.example.silasonyango.ewe.SignIn.decodeSampledBitmapFromResource;

/**
 * Created by SilasOnyango on 3/7/2017.
 */

public class UploadProducts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    AlertDialog alertDialog;
    String UserId,UserName,Addr,Category;
    FloatingActionButton fab;
    String path;
    ImageView img;
    View v;
    LayoutInflater inflater;
    DatabaseHelper myDb;
    public ImageView imgUploadPreview;
    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    public EditText etProductType,etProductMake,etProductColour,etPrice,etOtherDescriptions;
    public ImageButton btGalleryButton;
    public Button btSubmitProduct,btCancelProduct,btSubmitNothing,btCancelNothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_products);
        img=(ImageView) findViewById(R.id.img1);
        img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.shop,  700, 700));
        myDb = new DatabaseHelper(this);
        viewSpecificData();

fab=(FloatingActionButton)findViewById(R.id.fab);
        etProductType=(EditText)findViewById(R.id.et_product_type);
        etProductMake=(EditText)findViewById(R.id.et_product_make);
        etProductColour=(EditText)findViewById(R.id.et_product_colour);
        etPrice=(EditText)findViewById(R.id.et_price);
        etOtherDescriptions=(EditText)findViewById(R.id.et_other_descriptions);



        btSubmitProduct=(Button)findViewById(R.id.bt_submit_product);
        btCancelProduct=(Button)findViewById(R.id.bt_cancel_product);

        inflater=this.getLayoutInflater();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                v= inflater.inflate(R.layout.upload_preview, null);
                imgUploadPreview = (ImageView) v.findViewById(R.id.img_upload_preview);
                btSubmitNothing = (Button) v.findViewById(R.id.btn_submit_nothing);
                btCancelNothing = (Button) v.findViewById(R.id.btn_cancel_nothing);
                btCancelNothing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                btSubmitNothing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                showFileChooser();
            }
        });



        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_ATOP);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select category");
        categories.add("Electronics");
        categories.add("Clothing for men");
        categories.add("Clothing for ladies");
        categories.add("Footwear for men");
        categories.add("Footwear for ladies");
        categories.add("Stationery");
        categories.add("Bedding");
        categories.add("Kitchen utensils");
        categories.add("Laundry items");
        categories.add("Household items");
        categories.add("Food and ingredients");
        categories.add("Phone Airtime");
        categories.add("Ornaments and Cosmetics for ladies");
        categories.add("Regalia for men e.g watches,bungles,neclaces");
        categories.add("Any other");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        btSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strProductType = etProductType.getText().toString().trim();
                String strProductMake = etProductMake.getText().toString().trim();
                String strProductColour =  etProductColour.getText().toString().trim();
                String strPrice = etPrice.getText().toString().trim();
                String strOtherDescriptions = etOtherDescriptions.getText().toString().trim();
                String comparison="Select category";
                if(Category.equals(comparison))
                {
                    Toast.makeText(getApplicationContext(),
                            "Kindly select the category of your product!", Toast.LENGTH_LONG)
                            .show();
                }

                else if (strProductType.isEmpty() && strProductMake.isEmpty() && strProductColour.isEmpty()&& strPrice.isEmpty()&& strOtherDescriptions.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Kindly fill every field on the form!", Toast.LENGTH_LONG)
                            .show();
                    // registerUser(name, email, password);
                } else {


                    Intent intent= new Intent(UploadProducts.this,Landing.class);
                    intent.putExtra("access","upload");
                    uploadImage();

                    startActivity(intent);
                }


            }



        });
        btCancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UploadProducts.this,Landing.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Category = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + Category, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(getBaseContext(),"Nothing selected", Toast.LENGTH_LONG).show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgUploadPreview.setImageBitmap(bitmap);
                previewUpload();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {

        if (uri!=null)
        {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();}
        else { Toast.makeText(getApplicationContext(),
                "You forgot to upload an image of your product!", Toast.LENGTH_LONG)
                .show();
        path="";
        }

        return path;
    }

   /* public void uploadMultipart() {

        String strProductType = etProductType.getText().toString().trim();
       String strProductMake = etProductMake.getText().toString().trim();
        String strProductColour = etProductColour.getText().toString().trim();
        String strPrice = etPrice.getText().toString().trim();
        String strOtherDescriptions = etOtherDescriptions.getText().toString().trim();

        String path = getPath(filePath);

        if (path.isEmpty()){//Do nothing and wait
             }
else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, Config.upload_products)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("id", UserId)
                        .addParameter("ProductType", strProductType)//Adding text parameter to the request
                        .addParameter("ProductMake", strProductMake)
                        .addParameter("ProductColour", strProductColour)
                        .addParameter("Price", strPrice)
                        .addParameter("OtherDescriptions", strOtherDescriptions)
                        .addParameter("Category",Category)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload


            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }}

        finish();
        startActivity(getIntent());

    }*/

    public void viewSpecificData() {

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            //Show message
            showMessage("Error", "No data found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("dbID : " + res.getString(0) + "\n");
            buffer.append("id : " + res.getString(1) + "\n");
            buffer.append("Name : " + res.getString(2) + "\n");
            buffer.append("Email : " + res.getString(3) + "\n");
            buffer.append("Key : " + res.getString(4) + "\n");
            buffer.append("Address : " + res.getString(5) + "\n\n");

            UserId=res.getString(1);
            UserName=res.getString(2);
            Addr=res.getString(5);

            //Addrr.setText(Addr);
        }

        //Show all data


        //showMessage("Data", buffer.toString());

        //Addrr.setText(Addr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void previewUpload() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }

    public void uploadImage() {
        final String strProductType = etProductType.getText().toString().trim();
        final String strProductMake = etProductMake.getText().toString().trim();
        final String strProductColour = etProductColour.getText().toString().trim();
        final String strPrice = etPrice.getText().toString().trim();
        final String strOtherDescriptions = etOtherDescriptions.getText().toString().trim();

        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UploadProducts.this, "Please wait...", "uploading", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(UploadProducts.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                com.example.silasonyango.ewe.RequestHandler rh = new com.example.silasonyango.ewe.RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("id", UserId);
                param.put("ProductType", strProductType);
                param.put("ProductMake", strProductMake);
                param.put("ProductColour", strProductColour);
                param.put("Price", strPrice);
                param.put("OtherDescriptions", strOtherDescriptions);
                param.put("Category",Category);
                param.put("image", image);
                String result = rh.sendPostRequest(Config.upload_products, param);
                return result;
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



}
