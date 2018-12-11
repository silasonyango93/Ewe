package com.example.silasonyango.ewe;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CustomMenuViewPagerAdapter extends PagerAdapter{
    private Context context;
    public ImageLoader imageLoader;
    private List<MenuObject> MenuList;
    private LayoutInflater layoutInflater;

    public CustomMenuViewPagerAdapter(Context context, List<MenuObject> MenuList) {
        this.context = context;
        this.MenuList = MenuList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public  int getCount() {
        return MenuList.size();
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
        View view = layoutInflater.inflate(R.layout.menu_view_pager_layout, container, false);
        MenuObject mHotDealObject = MenuList.get(position);

        NetworkImageView hotDealFoodImage = (NetworkImageView)view.findViewById(R.id.hot_deal_food_image);



        imageLoader = MCustomVolleyRequest.getInstance(this.context).getImageLoader();
        imageLoader.get(mHotDealObject.getImageUrl(), ImageLoader.getImageListener(hotDealFoodImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        hotDealFoodImage.setImageUrl(mHotDealObject.getImageUrl(), imageLoader);



        container.addView(view);
        return view;
    }
}
