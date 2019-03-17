package com.example.mangaglide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder>{
    private Fragment fragment;
    private ImageURLInterface images;

    ImageAdapter(ImageURLInterface images, Fragment fragment){
        this.fragment = fragment;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterViewHolder imageAdapterViewHolder, int i) {
        ImageView imageView = imageAdapterViewHolder.imageView;
        String currentUrl = images.get(i);

        int imageWidthPixels = 1024*4;
        int imageHeightPixels = 768*4;
        Glide.with(fragment)
                .load(currentUrl).fitCenter()
                .override(imageWidthPixels, imageHeightPixels)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return images.count();
    }

    class ImageAdapterViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
