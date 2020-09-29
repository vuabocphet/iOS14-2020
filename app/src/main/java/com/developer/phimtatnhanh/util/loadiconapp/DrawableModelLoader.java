package com.developer.phimtatnhanh.util.loadiconapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

public class DrawableModelLoader implements ModelLoader<ApplicationInfo, Drawable>
{
    private final Context mContext;

    DrawableModelLoader(Context context ) {
        mContext = context;
    }

    @Nullable
    @Override
    public LoadData<Drawable> buildLoadData(@NonNull ApplicationInfo applicationInfo, int width, int height, @NonNull Options options) {

        return new LoadData<>( new ObjectKey( applicationInfo ),
                new DrawableDataFetcher( mContext, applicationInfo ) );
    }

    @Override
    public boolean handles(@NonNull ApplicationInfo applicationInfo) {
        return true;
    }
}