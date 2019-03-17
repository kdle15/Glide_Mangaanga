package com.example.mangaglide;

import java.util.ArrayList;

public class Manga_info {
    private String title;
    private String total_chap;
    private String category;
    private String introduction;
    private ArrayList<String> chaps;
    private ArrayList<String> namechap;

    public ArrayList<String> getNamechap() {
        return namechap;
    }

    public Manga_info(String title, String total_chap, String category, String introduction, ArrayList<String> chaps
            , ArrayList<String> namechap){
        this.title = title;
        this.total_chap = total_chap;
        this.category = category;
        this.introduction = introduction;
        this.chaps = new ArrayList<>(chaps);
        this.namechap = namechap;
    }

    public String getTitle() {
        return title;
    }

    public String getTotal_chap() {
        return total_chap;
    }

    public String getCategory() {
        return category;
    }

    public String getIntroduction() {
        return introduction;
    }

    public ArrayList<String> getChaps() {
        return chaps;
    }
}
