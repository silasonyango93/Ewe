package com.example.silasonyango.ewe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SplashLauncher extends Activity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private ViewPager viewPager;
    TextView tvGuest,tvSplash;
    private CustomSplashScreenAdapter mAdapter;
    private Handler handler;
    Button btSignUp,btLogin;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new SessionManager(getBaseContext()).isLoggedIn()) {
            Intent in = new Intent(getBaseContext(), Landing.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("access","signin");
            startActivity(in);
            finish();
        }
        setContentView(R.layout.activity_splash_launcher);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        handler = new Handler();
        viewPager = (ViewPager)findViewById(R.id.hot_deal_view_pager);
        tvGuest = (TextView) findViewById(R.id.tv_asGuest);
        btSignUp=(Button)findViewById(R.id.btn_signup);
        btLogin=(Button)findViewById(R.id.btn_signin);

        tvSplash = (TextView) findViewById(R.id.consa);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "JosefinSans-Light.ttf");

        // tvConservation.setTypeface(custom_font);
        tvSplash.setTypeface(custom_font);

        tvGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i =new Intent(SplashLauncher.this,Menu.class)  ;

                startActivity(i);

            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SplashLauncher.this,SignUp.class)  ;

                startActivity(i);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SplashLauncher.this,SignIn.class)  ;

                startActivity(i);
            }
        });

        mAdapter = new CustomSplashScreenAdapter(getBaseContext(), getTestData());
        // SlideshowPagerAdapter adapter = new SlideshowPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                page = position;
                switch (position){
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;
                    case 3:
                        radioGroup.check(R.id.radioButton4);
                        break;
                    case 4:
                        radioGroup.check(R.id.radioButton5);
                        break;
                    case 5:
                        radioGroup.check(R.id.radioButton6);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public List<SplashObject> getTestData() {
        List<SplashObject> mTestData = new ArrayList<SplashObject>();
        mTestData.add(new SplashObject(R.drawable.jaja));
        mTestData.add(new SplashObject(R.drawable.lala));
        mTestData.add(new SplashObject(R.drawable.zaza));

        mTestData.add(new SplashObject(R.drawable.xaxa));
        mTestData.add(new SplashObject(R.drawable.sasa));
        mTestData.add(new SplashObject(R.drawable.vava));
        return mTestData;
    }
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }



}
