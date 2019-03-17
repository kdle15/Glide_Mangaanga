package com.example.mangaglide;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Manga extends Fragment {
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    private ImageURLInterface myUrls;
    private String url = "https://blogtruyen.com/c2240/one-piece-chap-2";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.p, null);
        return inflater.inflate(R.layout.manga_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myUrls = ImageURLInterface.create(url);
        ListPreloader.PreloadSizeProvider sizeProvider = new FixedPreloadSizeProvider(imageWidthPixels, imageHeightPixels);
        ListPreloader.PreloadModelProvider modelProvider = new MyPreloadModelProvider();
        RecyclerViewPreloader<Image> preloader =
                new RecyclerViewPreloader<>(Glide.with(this), modelProvider, sizeProvider, 20);
        RecyclerView recyclerView = view.findViewById(R.id.rv_images);
        recyclerView.addOnScrollListener(preloader);

        //set layout
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new ImageAdapter(myUrls, this));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),
                DividerItemDecoration.VERTICAL));
    }

    private class MyPreloadModelProvider implements ListPreloader.PreloadModelProvider{
        @NonNull
        @Override
        public List getPreloadItems(int position) {
            String url = myUrls.get(position);
            if (TextUtils.isEmpty(url)) {
                return Collections.emptyList();
            }
            return Collections.singletonList(url);
        }

        @Nullable
        @Override
        public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
            return Glide.with(Manga.this).load(item).override(imageWidthPixels, imageHeightPixels);
        }
    }
}
