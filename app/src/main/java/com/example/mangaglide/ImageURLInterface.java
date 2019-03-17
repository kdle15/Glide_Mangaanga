package com.example.mangaglide;

abstract public class ImageURLInterface {
    public abstract String next();

    public abstract String prev();

    public static ImageURLInterface create(String url) {
        return new Manga_link(url);
    }

    public abstract int count();

    public abstract String get(int i);
}

