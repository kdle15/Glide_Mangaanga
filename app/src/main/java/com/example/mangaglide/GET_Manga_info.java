package com.example.mangaglide;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GET_Manga_info extends AsyncTask<String, Void, Manga_info> {
    private final String prefix1 = "https://m.blogtruyen.com";
    @Override
    protected Manga_info doInBackground(String... urls) {
        Manga_info c = null;
        final StringBuilder builder = new StringBuilder();
        try {
            Document doc = Jsoup.connect(urls[0]).get();
            //get the intro
            //Element links = doc.select(".manga-detail 1 bigclass ng-scope").first();
            Element title = doc.select(".entry-title").first();
            String title_name = title.getElementsByTag("a").first().attr("title");

            //list chapter
            Element listchapter = doc.select("#list-chapters").first();
            ArrayList<String> ar = new ArrayList<>();
            ArrayList<String> name = new ArrayList<>();
            for(Element p: listchapter.children()){
                Element a = p.getElementsByTag("a").first();
                ar.add(prefix1+a.attr("href"));
                name.add(a.attr("title"));
            }

            //get category
            Elements category = doc.select(".category");
            String cate = "";
            for(Element p: category){
                Element a = p.getElementsByTag("a").first();
                cate += a.text() + " ";
            }

            //getIntroduction
            Element content = doc.select(".content").first();
            String intro = "";
            for(Element p: content.getElementsByTag("p")){
                intro += p.text() + " ";
            }

            System.out.println("category is" + intro);

            c = new Manga_info(title_name, String.valueOf(ar.size()), cate, intro, ar, name);

        } catch (IOException e) {
            builder.append("Error : ").append(e.getMessage()).append("\n");
        }

        return c;
    }
}

