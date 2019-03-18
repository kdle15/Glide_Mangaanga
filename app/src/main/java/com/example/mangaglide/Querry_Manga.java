package com.example.mangaglide;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Querry_Manga extends AsyncTask<String, Void, ArrayList<Link_info>> {
    private final String prefix1 = "https://m.blogtruyen.com";
    private int max_querry_item = 10;

    @Override
    protected ArrayList<Link_info> doInBackground(String... urls) {
        ArrayList<Link_info> list = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();

        try {
            Document doc = Jsoup.connect(urls[0]).get();
            String title = doc.title();
            Elements links = doc.select("table");
            org.jsoup.nodes.Element table = links.first();
            if(table != null){
                Element table_body = table.child(0);
                int i = 0;
                for (Element link : table_body.children()) {
                    Element l = link.getElementsByTag("a").first();
                    Link_info q = new Link_info(l.attr("title"), prefix1 + l.attr("href"));
                    list.add(q);
                    i++;
                    if(i == max_querry_item) break;
                }
            }else{
                return null;
            }
        } catch (IOException e) {
            builder.append("Error : ").append(e.getMessage()).append("\n");
        }

        return list;
    }
}
