package com.example.mangaglide;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Manga_link extends ImageURLInterface {
    private int curr = -1;

    private ArrayList<String> images = null;


    public Manga_link(String urls) {
        final String[] url = new String[]{urls};
        try {
            images = new Link().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String next() {
        curr = (curr + 1) % images.size();
        return images.get(curr);
    }

    @Override
    public String prev() {
        return null;
    }

    @Override
    public int count() {
        return images.size();
    }

    @Override
    public String get(int i) {
        return images.get(i);
    }
}
