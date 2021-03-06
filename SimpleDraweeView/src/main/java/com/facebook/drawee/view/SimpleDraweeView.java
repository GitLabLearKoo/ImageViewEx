package com.facebook.drawee.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;


import com.facebook.drawee.replace.R;
import com.taoweiji.image.ImageViewExBase;

public class SimpleDraweeView extends ImageViewExBase {


    public SimpleDraweeView(Context context) {
        this(context, null);
    }

    public SimpleDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDraweeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleDraweeView);

        this.roundedCornerRadius = typedArray.getDimensionPixelSize(R.styleable.SimpleDraweeView_roundedCornerRadius, roundedCornerRadius);
        this.roundTopLeft = typedArray.getBoolean(R.styleable.SimpleDraweeView_roundTopLeft, roundTopLeft);
        this.roundTopRight = typedArray.getBoolean(R.styleable.SimpleDraweeView_roundTopRight, roundTopRight);
        this.roundBottomLeft = typedArray.getBoolean(R.styleable.SimpleDraweeView_roundBottomLeft, roundBottomLeft);
        this.roundBottomRight = typedArray.getBoolean(R.styleable.SimpleDraweeView_roundBottomRight, roundBottomRight);


        this.circle = typedArray.getBoolean(R.styleable.SimpleDraweeView_roundAsCircle, circle);

        this.borderColor = typedArray.getColor(R.styleable.SimpleDraweeView_roundingBorderColor, Color.WHITE);
        this.borderWidth = typedArray.getDimensionPixelSize(R.styleable.SimpleDraweeView_roundingBorderWidth, borderWidth);
        this.fadeDuration = typedArray.getInt(R.styleable.SimpleDraweeView_fadeDuration, fadeDuration);

        int roundingBorderPadding = typedArray.getDimensionPixelSize(R.styleable.SimpleDraweeView_roundingBorderPadding, 0);
        int backgroundImage = typedArray.getResourceId(R.styleable.SimpleDraweeView_backgroundImage, 0);
        int actualImageScaleType = typedArray.getInt(R.styleable.SimpleDraweeView_actualImageScaleType, 0);
        int actualImageResource = typedArray.getResourceId(R.styleable.SimpleDraweeView_actualImageResource, 0);
        String actualImageUri = typedArray.getString(R.styleable.SimpleDraweeView_actualImageUri);

        Drawable placeholderImageTmp = typedArray.getDrawable(R.styleable.SimpleDraweeView_placeholderImage);
        int placeholderImageScaleType = typedArray.getInt(R.styleable.SimpleDraweeView_placeholderImageScaleType, 0);

        float viewAspectRatio = typedArray.getFloat(R.styleable.SimpleDraweeView_viewAspectRatio, 0);


        if (backgroundImage != 0) {
            setBackgroundResource(backgroundImage);
        }
        if (actualImageScaleType != 0) {
            ScaleType scaleType = intToScaleType(actualImageScaleType);
            setScaleType(scaleType);
        } else if (placeholderImageScaleType != 0) {
            ScaleType scaleType = intToScaleType(placeholderImageScaleType);
            setScaleType(scaleType);
        }
        if (actualImageUri != null) {
            Uri uri = Uri.parse(actualImageUri);
            if (!TextUtils.isEmpty(uri.getScheme())) {
                setImageURI(uri);
            }
        }
        if (actualImageResource != 0) {
            setImageResource(actualImageResource);
        }

        typedArray.recycle();
        setImageDrawable(getDrawable());
        setPlaceholderImageDrawable(placeholderImageTmp);
    }

    @Override
    public void setImageURI(Uri uri) {
        if (uri == null) {
            super.setImageURI(null);
        } else if ("res".equalsIgnoreCase(uri.getScheme())) {
            // 兼容 Fresco 的 res://xxx/xxx 的写法
            uri = uri.buildUpon().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE).build();
            super.setImageURI(uri);
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
            super.setImageURI(uri);
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) || ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            super.setImageURI(uri);
        } else {
            setImageURI(uri.toString());
        }
    }

    public void setImageURI(String uriString) {
        loadHandler.load(this, uriString);
    }

    private static LoadHandler loadHandler;

    public static void setLoadHandler(LoadHandler loadHandler) {
        SimpleDraweeView.loadHandler = loadHandler;
    }

    public interface LoadHandler {
        void load(SimpleDraweeView imageView, String url);
    }

    private ScaleType intToScaleType(int type) {
        ScaleType scaleType;
        switch (type) {
            case 1:
                scaleType = ScaleType.FIT_XY;
                break;
            case 2:
                scaleType = ScaleType.FIT_START;
                break;
            case 3:
                scaleType = ScaleType.FIT_CENTER;
                break;
            case 4:
                scaleType = ScaleType.FIT_END;
                break;
            case 5:
                scaleType = ScaleType.CENTER;
                break;
            case 6:
                scaleType = ScaleType.CENTER_CROP;
                break;
            case 7:
                scaleType = ScaleType.CENTER_INSIDE;
                break;
            default:
                scaleType = ScaleType.MATRIX;
        }
        return scaleType;
    }
}
