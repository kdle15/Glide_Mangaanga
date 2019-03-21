package com.example.mangaglide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GET_Manga_info extends AsyncTask<String, Void, Manga_info> {
    private final String prefix1 = "https://m.blogtruyen.com";
    @SuppressLint("StaticFieldLeak")
    private Activity mAct;
    private String current_chap = "0";
    private boolean isLiked_global;
    private final static int REQUEST_CODE_1 = 1;
    private int site;

    public GET_Manga_info(Activity activity, int site){
        mAct = activity;
        this.site = site;
    }

    @Override
    protected Manga_info doInBackground(String... urls) {
        Manga_info c = null;
        String title_name = urls[0];
        final StringBuilder builder = new StringBuilder();
        if(site == 0){
            //blog truyen
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                //get the intro
                //Element links = doc.select(".manga-detail 1 bigclass ng-scope").first();
                Element title = doc.select(".entry-title").first();
                title_name = title.getElementsByTag("a").first().attr("title");

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
        }

        if(site == 1){
            System.out.println("here");
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                //get the intro
                Element title = doc.select(".manga-info-text").first();
                title_name = title.getElementsByTag("h1").first().text();


                //list chapter
                Element listchapter = doc.select(".chapter-list").first();
                ArrayList<String> ar = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                for(Element p: listchapter.children()){
                    Element a = p.getElementsByTag("a").first();
                    ar.add(a.attr("href"));
                    name.add(a.text());
                }

                //get category
                Elements category = title.getElementsByTag("li");
                String cate = "";
                for(Element p: category){
                    System.out.println("category is here" + p.text());
                    if(p.text().indexOf("Genres :") != -1){
                       cate = p.text();
                    }
                }

                //getIntroduction
                Element content = doc.select("#noidungm").first();
                String intro = content.text();

                c = new Manga_info(title_name, String.valueOf(ar.size()), cate, intro, ar, name);

            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }
        }
        return c;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(final Manga_info manga) {
        TextView title = mAct.findViewById(R.id.title);
        TextView totalChap = mAct.findViewById(R.id.totalchap);
        TextView category = mAct.findViewById(R.id.cate);
        TextView content = mAct.findViewById(R.id.content);
        SeekBar seekBar = mAct.findViewById(R.id.seaker);
        final Button currentChap = mAct.findViewById(R.id.currentchap);
        final Button addtoFile = mAct.findViewById(R.id.addtolist);

        if(manga != null){
            //set back the manga clicked on the main activity
            ((MainActivity) mAct).setManga(manga);
            title.setText(manga.getTitle());
            totalChap.setText("Chap " + manga.getTotal_chap());
            final int total_chapter_int = Integer.parseInt(manga.getTotal_chap()) - 1;
            seekBar.setMax(total_chapter_int);
            seekBar.setProgress(0);
            currentChap.setText(manga.getNamechap().get(total_chapter_int - Integer.parseInt(current_chap)));
            category.setText(manga.getCategory());
            content.setText(manga.getIntroduction());

            //here access to see if it is favorite or not.
            isLiked_global = ((MainActivity)mAct).getFile_content().containsKey(((MainActivity)mAct).getOnClik_manga());
            if(isLiked_global) {
                addtoFile.setBackgroundColor(Color.GREEN);
            }else{
                addtoFile.setBackgroundColor(Color.RED);
            }

            addtoFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isLiked_global){
                        //create a file
                        String fileContents = manga.getTitle();
                        ((MainActivity)mAct).getFile_content().put(((MainActivity)mAct).getOnClik_manga(), fileContents);
                        addtoFile.setBackgroundColor(Color.GREEN);
                        isLiked_global = true;
                    }else{
                        ((MainActivity)mAct).getFile_content().remove(((MainActivity)mAct).getOnClik_manga());
                        addtoFile.setBackgroundColor(Color.RED);
                        isLiked_global = false;
                    }
                    WriteFile();
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    current_chap = String.valueOf(progress);
                    currentChap.setText(manga.getNamechap().get(total_chapter_int - Integer.parseInt(current_chap)));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            currentChap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mAct.getApplicationContext(), Reading.class);
                    i.putStringArrayListExtra("ALLURLs", manga.getChaps());
                    i.putExtra("CURRENT_INDEX", current_chap);
                    mAct.startActivityForResult(i, REQUEST_CODE_1);
                }
            });

            remove_all_fragment();
            super.onPostExecute(manga);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void remove_all_fragment(){
        for (Fragment fragment: mAct.getFragmentManager().getFragments()) {
            mAct.getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    //always update file after tap the bookmark button
    private void WriteFile(){
        String filename = "myfile";
        File f = f = new File(mAct.getFilesDir(), filename);
        f.delete();
        f = new File(mAct.getFilesDir(), filename);
        final FileOutputStream outputStream;
        try {
            outputStream = mAct.openFileOutput(filename, Context.MODE_APPEND);
            for(final String line : ((MainActivity)mAct).getFile_content().keySet()){
                outputStream.write(line.getBytes());
                outputStream.write(System.getProperty("line.separator").getBytes());
                outputStream.write(((MainActivity)mAct).getFile_content().get(line).getBytes());
                outputStream.write(System.getProperty("line.separator").getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
