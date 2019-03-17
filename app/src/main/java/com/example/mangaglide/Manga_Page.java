package com.example.mangaglide;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.Reader;
import java.util.concurrent.ExecutionException;

public class Manga_Page extends Activity {

    Manga_info manga = null;
    TextView title;
    TextView totalChap;
    TextView category;
    TextView content;
    TextView currentChap;
    SeekBar seekBar;
    Button getchap;
    String current_chap = "0";
    String pass_url = "";
    int total_chapter_int = 0;
    int read_mode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga__page);
        title = findViewById(R.id.title);
        totalChap = findViewById(R.id.totalchap);
        category = findViewById(R.id.cate);
        content = findViewById(R.id.content);
        seekBar = findViewById(R.id.seaker);
        getchap = findViewById(R.id.getChap);
        currentChap = findViewById(R.id.currentchap);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String[] url = new String[1];
        if(b!= null){
            url[0] = (String) b.get("EXTRA_MESSAGE");
        }
        try {
            manga = new GET_Manga_info().execute(url).get();
            title.setText(manga.getTitle());
            totalChap.setText("Chap " + manga.getTotal_chap());
            total_chapter_int = Integer.parseInt(manga.getTotal_chap()) - 1;
            seekBar.setMax(total_chapter_int);
            seekBar.setProgress(0);
            currentChap.setText(manga.getNamechap().get(total_chapter_int - Integer.parseInt(current_chap)));
            category.setText(manga.getCategory());
            content.setText(manga.getIntroduction());
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

            getchap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Reading.class);
                    i.putStringArrayListExtra("ALLURLs", manga.getChaps());
                    i.putExtra("CURRENT_INDEX", current_chap);
                    System.out.println("chap url is" + pass_url);
                    startActivity(i);

                }
            });
        } catch (ExecutionException e) {
            finish();
        } catch (InterruptedException e) {
            finish();
        }
    }
}
