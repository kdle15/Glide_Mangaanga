package com.example.mangaglide;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Manga extends Fragment {
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    private ImageURLInterface myUrls;
    private LinearLayoutManager layoutManager;
    private ArrayList<String> all_url;
    private ImageView iv;
    private RecyclerView recyclerView;
    private int current;
    private String[] url;
    private boolean index;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //2
        return inflater.inflate(R.layout.manga_layout, container, false);
    }

    public ImageView getIv() {
        return iv;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //3
        all_url = ((Reading) this.getActivity()).getAll_url();
        current = ((Reading) this.getActivity()).getCurrent();
        url = new String[1];
        url[0] = all_url.get(all_url.size() - 1  - current);
        int index1 = url[0].indexOf("mangakakalot");
        int index2 = url[0].indexOf("manganelo");
        index = index1 == -1 && index2 == -1;
        if(index){
            url[0] = url[0].substring(0,8) + url[0].substring(10);
        }
        System.out.println("get url" + url[0]);
        myUrls = ImageURLInterface.create(url[0]);

        //default image
        iv = view.findViewById(R.id.empty_view);
        iv.setImageResource(R.drawable.error);
        iv.setClickable(true);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("you picked");
                getActivity().getFragmentManager().beginTransaction().remove(Manga.this).commit();
            }
        });
        setup();
    }

    private void setup(){
        //set up recycler view
        ListPreloader.PreloadSizeProvider sizeProvider = new FixedPreloadSizeProvider(imageWidthPixels, imageHeightPixels);
        ListPreloader.PreloadModelProvider modelProvider = new MyPreloadModelProvider();

        RecyclerViewPreloader<Image> preloader =
                new RecyclerViewPreloader<>(Glide.with(this), modelProvider, sizeProvider, 20);
        recyclerView = this.getView().findViewById(R.id.rv_images);
        recyclerView.addOnScrollListener(preloader);

        //set layout
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        //5dp space between item
        itemDecorator.setDrawable(getActivity().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(new ImageAdapter(myUrls, this, recyclerView));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }


    public String next(){
        String url_next = "";
        if (current + 1 < all_url.size()){
            int next = current + 1;
            url_next = all_url.get(all_url.size() - 1  - next);
            if(index) {
                url_next = url_next.substring(0, 8) + url_next.substring(10);
            }
        }
        return url_next;
    }

    public String prev(){
        String url_prev = "";
        if (current > 0){
            int prev = current - 1;
            url_prev = all_url.get(all_url.size() - 1  - prev);
            if(index){
                url_prev = url_prev.substring(0,8) + url_prev.substring(10);
            }
        }
        return url_prev;
    }

    public void setMyUrls(ImageURLInterface myUrls) {
        this.myUrls = myUrls;
    }

    public void setCurrent(int current) {
        this.current += current;
        ((Reading) getActivity()).setCurrent(current);
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }
}
