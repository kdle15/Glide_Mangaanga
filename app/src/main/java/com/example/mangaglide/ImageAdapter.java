package com.example.mangaglide;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder>{
    private Fragment fragment;
    private ImageURLInterface images;
    private RecyclerView recyclerView;
    private GestureDetectorCompat mDetector;

    ImageAdapter(ImageURLInterface images, Fragment fragment, RecyclerView recyclerView){
        this.fragment = fragment;
        this.images = images;
        this.recyclerView = recyclerView;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ImageAdapterViewHolder imageAdapterViewHolder, int i) {
        final ImageView imageView = imageAdapterViewHolder.imageView;
        String currentUrl = images.get(i);
        // In landscape
        int imageWidthPixels = 1024*4;
        int imageHeightPixels = 768*4;
        Glide.with(fragment)
                .load(currentUrl)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .override(imageWidthPixels, imageHeightPixels)
                .into(imageView);

        mDetector = new GestureDetectorCompat(fragment.getContext(),new MyGestureListener());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
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

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 20;
        private static final int SWIPE_THRESHOLD_VELOCITY = 40;
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println("Movement is from right to left");
                //go the next chapter:
                if(!((Manga) fragment).next().equals("")){
                    ImageURLInterface myUrls = ImageURLInterface.create(((Manga) fragment).next());
                    ((Manga) fragment).setMyUrls(myUrls);
                    recyclerView.swapAdapter(new ImageAdapter(myUrls, fragment, recyclerView), true);
                    ((Manga) fragment).setCurrent(1);
                    Toast.makeText(fragment.getContext(), "NEXT CHAPTER", Toast.LENGTH_SHORT).show();
                }
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println("Movement is from left to right");
                if(!((Manga) fragment).prev().equals("")){
                    ImageURLInterface myUrls = ImageURLInterface.create(((Manga) fragment).prev());
                    ((Manga) fragment).setMyUrls(myUrls);
                    recyclerView.swapAdapter(new ImageAdapter(myUrls, fragment, recyclerView), true);
                    ((Manga) fragment).setCurrent(-1);
                    Toast.makeText(fragment.getContext(), "PREVIOUS CHAPTER", Toast.LENGTH_SHORT).show();
                }
                return false; // Left to right
            }
            return false;
        }
    }
}
