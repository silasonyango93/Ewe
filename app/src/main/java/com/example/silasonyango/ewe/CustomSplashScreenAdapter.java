package com.example.silasonyango.ewe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomSplashScreenAdapter extends PagerAdapter{

    private Context context;
    private List<SplashObject> SplashlList;
    private LayoutInflater layoutInflater;
    public CustomSplashScreenAdapter(Context context, List<SplashObject> SplashlList) {
        this.context = context;
        this.SplashlList = SplashlList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return SplashlList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.splash_list_layout, container, false);
        SplashObject mSplashObject = SplashlList.get(position);

        ImageView ivSplashImage = (ImageView)view.findViewById(R.id.splash_image);



        ivSplashImage.setImageBitmap(
                decodeSampledBitmapFromResource(context.getResources(), mSplashObject.getImage(), 700, 700));


        container.addView(view);
        return view;
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