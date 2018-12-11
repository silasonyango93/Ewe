package com.example.silasonyango.ewe;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.silasonyango.ewe.Landing.isConnected;
import static com.example.silasonyango.ewe.SignIn.decodeSampledBitmapFromResource;


public class ThreeFragment extends Fragment {
    public static final String TAG_sIMAGE_URL = "ProductImageUrl";
    private ViewPager viewPager;
    private CustomMenuViewPagerAdapter mAdapter;
    RelativeLayout rlMensClothing,rlWomensClothing,rlElectronics,rlMensShoes,rlLadysShoes,rlStationery,rlBedding,rlCosmetics,rlKitchenware,rlLaundry,rlHouseHolds,rlFood,rlAirtime,rlMensRegalia,rlWomensRegalia,rlBuyersRequests;
    final String ip="https://www.zebaki.co.ke/Klabu/getAllProducts.php";
    List<MenuObject> mTestData = new ArrayList<>();
    final Handler handler = new Handler();
    final Handler mhandler = new Handler();
    private final int delay = 2000;
    private int page = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            if (mAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };
    int myCounter=0;
    TextView tvShop;
    RecyclerView rView;
    private ArrayList<String> images;
    private GridLayoutManager lLayout;
    public static final String TAG_IMAGE_URL = "ProductImageUrl";
    View v;


    public ThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_three, container, false);

        checkConnection(v);

        return v;
    }




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}



    private void klabu() {
        //Showing a progress dialog while our app fetches the data from url

        //Creating a json array request to get the json from our api

        StringRequest request = new StringRequest(Request.Method.POST, ip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        Log.d("responseKlabu", s);


                        try {

                            JSONObject object = new JSONObject(s);
                            JSONArray array= object.getJSONArray("result");

                            showmGrid(array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("ggg", volleyError.toString());
            }
        }) {


        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        Log.d("respo", request.toString());
        requestQueue.add(request);
    }

    private void showmGrid(JSONArray jsonArray) {
        //Looping through all the elements of json array

        int j=0;


        for (int i = 0; i < jsonArray.length(); i++) {

            //Creating a json object of the current index
            JSONObject obj ;
            try {

                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                ArrayList<String> testURLS= new ArrayList<String>();
                String testURL= obj.getString(TAG_sIMAGE_URL);
                images.add(obj.getString(TAG_sIMAGE_URL));
                testURLS.add(testURL);
                Log.d("testURLS",testURLS.toString());
                //getting image url and title from json object
                mTestData.add(new MenuObject(testURL));
                Log.d( "showmGridData", mTestData.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        final RadioGroup myRadioGroup = new RadioGroup(getActivity());
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

        for (j = 1; j <= mTestData.size(); j++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId(j);
            rdbtn.setScaleX((float) 0.5);
            rdbtn.setScaleY((float) 0.5);
            if(Build.VERSION.SDK_INT>=21)
            {

                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{

                                new int[]{-android.R.attr.state_enabled}, //disabled
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[] {

                                R.color.pink //disabled
                                , Color.BLUE //enabled

                        }
                );


                rdbtn.setButtonTintList(colorStateList);//set the color tint list
                rdbtn.invalidate(); //could not be necessary
            }


            //rdbtn.setText("Radio " + rdbtn.getId());
            myRadioGroup.addView(rdbtn);
        }
        ((ViewGroup) v.findViewById(R.id.radiogroup)).addView(myRadioGroup);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @SuppressLint("ResourceType")
            @Override
            public void onPageSelected(int position) {
                page = position;
                myRadioGroup.check(position+1);

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mAdapter = new CustomMenuViewPagerAdapter(getActivity(), mTestData);

        // SlideshowPagerAdapter adapter = new SlideshowPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        ShopsRVAdapter rcAdapter = new ShopsRVAdapter(getActivity(),images);
        rView.setAdapter(rcAdapter);

    }


    public void checkConnection(final View v)
    {boolean connected;
        try {
            connected= isConnected();

            if(connected==true)
            {images = new ArrayList<>();
                lLayout = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);

                rView = (RecyclerView)v.findViewById(R.id.shops_rv);
                rView.setHasFixedSize(true);
                rView.setLayoutManager(lLayout);
                tvShop=(TextView) v.findViewById(R.id.tvShop);
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "JosefinSans-Regular.ttf");
                tvShop.setTypeface(custom_font);
                rlMensClothing=(RelativeLayout) v.findViewById(R.id.rlMensClothing);
                rlWomensClothing=(RelativeLayout) v.findViewById(R.id.rlWomensClothing);
                rlElectronics=(RelativeLayout) v.findViewById(R.id.rlElectronics);
                rlMensShoes=(RelativeLayout)v. findViewById(R.id.rlMensShoes);
                rlLadysShoes=(RelativeLayout)v. findViewById(R.id.rlLadysShoes);
                rlStationery=(RelativeLayout)v. findViewById(R.id.rlStationery);
                rlBedding=(RelativeLayout)v. findViewById(R.id.rlBedding);
                rlCosmetics=(RelativeLayout) v.findViewById(R.id.rlCosmetics);
                rlKitchenware=(RelativeLayout) v.findViewById(R.id.rlKitchenware);
                rlLaundry=(RelativeLayout) v.findViewById(R.id.rlLaundry);
                rlHouseHolds=(RelativeLayout) v.findViewById(R.id.rlHouseHolds);
                rlFood=(RelativeLayout) v.findViewById(R.id.rlFood);
                rlAirtime=(RelativeLayout)v. findViewById(R.id.rlAirtime);
                rlMensRegalia=(RelativeLayout) v.findViewById(R.id.rlMensRegalia);
                rlWomensRegalia=(RelativeLayout)v. findViewById(R.id.rlWomensRegalia);
                rlBuyersRequests=(RelativeLayout)v. findViewById(R.id.rlBuyersRequests);

                rlMensClothing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Clothing for men");
                        startActivity(intent);

                    }
                });
                rlWomensClothing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Clothing for ladies");
                        startActivity(intent);

                    }
                });
                rlElectronics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Electronics");
                        startActivity(intent);

                    }
                });
                rlMensShoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Footwear for men");
                        startActivity(intent);

                    }
                });
                rlLadysShoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Footwear for ladies");
                        startActivity(intent);

                    }
                });
                rlStationery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Stationery");
                        startActivity(intent);

                    }
                });
                rlBedding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Bedding");
                        startActivity(intent);

                    }
                });
                rlCosmetics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Ornaments and Cosmetics for ladies");
                        startActivity(intent);

                    }
                });
                rlKitchenware.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Kitchen utensils");
                        startActivity(intent);

                    }
                });
                rlLaundry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Laundry items");
                        startActivity(intent);

                    }
                });
                rlHouseHolds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Household items");
                        startActivity(intent);

                    }
                });
                rlFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Food and ingredients");
                        startActivity(intent);

                    }
                });
                rlAirtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Phone Airtime and Phone Accesories");
                        startActivity(intent);

                    }
                });
                rlMensRegalia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Regalia for men e.g watches,bungles,neclaces");
                        startActivity(intent);

                    }
                });
                rlWomensRegalia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","Ornaments and Cosmetics for ladies");
                        startActivity(intent);

                    }
                });
                rlBuyersRequests.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductSearchResults.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Category","");
                        startActivity(intent);

                    }
                });

                viewPager = (ViewPager) v.findViewById(R.id.hot_deal_view_pager);


                klabu();





                // myCounter=mAdapter.getCount();

                RadioGroup ll = new RadioGroup(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                for (int i = 1; i <= myCounter; i++) {
                    RadioButton rdbtn = new RadioButton(getActivity());
                    rdbtn.setId((1 + 1000) + i);
                    rdbtn.setScaleX((float) 0.5);
                    rdbtn.setScaleY((float) 0.5);
                    if(Build.VERSION.SDK_INT>=21)
                    {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_enabled}, //disabled
                                        new int[]{android.R.attr.state_enabled} //enabled
                                },
                                new int[] {

                                        R.color.pink //disabled
                                        , Color.BLUE //enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }



                    //rdbtn.setText("Radio " + rdbtn.getId());
                    ll.addView(rdbtn);
                }
                ((ViewGroup) v.findViewById(R.id.radiogroup)).addView(ll);

            }
            else{ handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkConnection(v);
                }
            }, 2000);}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onResume() {
        super.onResume();
        mhandler.postDelayed(runnable, delay);
    }
    @Override
    public void onPause() {
        super.onPause();
        mhandler.removeCallbacks(runnable);
    }

}
