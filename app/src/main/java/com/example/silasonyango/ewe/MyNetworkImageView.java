package com.example.silasonyango.ewe;

import android.content.Context;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyNetworkImageView extends CircleImageView{
    public MyNetworkImageView(Context context) {
        super(context);
    }

    /** The URL of the network image to load */
    private String mUrl;
    /**
     * Resource ID of the image to be used as a placeholder until the network image is loaded.
     */
    private int mDefaultImageId;
    /**
     * Resource ID of the image to be used if the network response fails.
     */
    private int mErrorImageId;
    /** Local copy of the ImageLoader. */
    private ImageLoader mImageLoader;

    public MyNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url, ImageLoader imageLoader) {
        mUrl = url;
        mImageLoader = imageLoader;
        // The URL has potentially changed. See if we need to load it.
        loadImageIfNecessary();
    }
    /**
     * Sets the default image resource ID to be used for this view until the attempt to load it
     * completes.
     */
    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }
    /**
     * Sets the error image resource ID to be used for this view in the event that the image
     * requested fails to load.
     */
    public void setErrorImageResId(int errorImage) {
        mErrorImageId = errorImage;
    }
    /**
     * Loads the image for the view if it isn't already loaded.
     */
    private void loadImageIfNecessary() {
        int width = getWidth();
        int height = getHeight();
        // if the view's bounds aren't known yet, hold off on loading the image.
        if (width == 0 && height == 0) {
            return;
        }
        // if the URL to be loaded in this view is empty, cancel any old requests and clear the
        // currently loaded image.
        if (TextUtils.isEmpty(mUrl)) {
            ImageContainer oldContainer = (ImageContainer) getTag();
            if (oldContainer != null) {
                oldContainer.cancelRequest();
                setImageBitmap(null);
            }
            return;
        }
        ImageContainer oldContainer = (ImageContainer) getTag();
        // if there was an old request in this view, check if it needs to be canceled.
        if (oldContainer != null && oldContainer.getRequestUrl() != null) {
            if (oldContainer.getRequestUrl().equals(mUrl)) {
                // if the request is from the same URL, return.
                return;
            } else {
                // if there is a pre-existing request, cancel it if it's fetching a different URL.
                oldContainer.cancelRequest();
                setImageBitmap(null);
            }
        }
        // The pre-existing content of this view didn't match the current URL. Load the new image
        // from the network.
        ImageContainer newContainer = mImageLoader.get(mUrl,
                ImageLoader.getImageListener(this, mDefaultImageId, mErrorImageId));
        // update the tag to be the new bitmap container.
        setTag(newContainer);
        // look at the contents of the new container. if there is a bitmap, load it.
        final Bitmap bitmap = newContainer.getBitmap();
        if (bitmap != null) {
            setImageBitmap(bitmap);
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary();
    }
    @Override
    protected void onDetachedFromWindow() {
        ImageContainer oldContainer = (ImageContainer) getTag();
        if (oldContainer != null) {
            // If the view was bound to an image request, cancel it and clear
            // out the image from the view.
            oldContainer.cancelRequest();
            setImageBitmap(null);
            // also clear out the tag so we can reload the image if necessary.
            setTag(null);
        }
        super.onDetachedFromWindow();
    }
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}