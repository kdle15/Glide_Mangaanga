package com.example.mangaglide;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Link extends AsyncTask<String, Void, ArrayList<String>> {
    private int site;
    public Link(int site){
        this.site = site;
    }
    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        ArrayList<String> images = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();
        if(site == 0){
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                String title = doc.title();
                Elements links = doc.select("article");
                org.jsoup.nodes.Element article = links.first();


                builder.append(title).append("\n");

                for (Element link : article.children()) {
                    if(link.tagName().equals("img")){
                        String link_img = link.attr("src");
                        if(link_img.charAt(4) != 's'){
                            link_img = link_img.substring(0,4) + "s" + link_img.substring(4);
                        }
                        images.add(link_img);
                    }
                }
            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }
        }else{
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                Elements links = doc.select("#vungdoc");
                org.jsoup.nodes.Element article = links.first();

                for (Element link : article.children()) {
                    if(link.tagName().equals("img")){
                        String link_img = link.attr("src");
                        images.add(link_img);
                    }
                }
            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }
        }
        return images;
    }

}
