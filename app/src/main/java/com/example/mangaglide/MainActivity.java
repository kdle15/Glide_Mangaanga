package com.example.mangaglide;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Initiate fragment
 */
public class MainActivity extends FragmentActivity {
    private Spinner spinner;
    private Button submit;
    private Button currentChap;
    private Button accessFile;
    private EditText text;
    private String Blog = null;
    private String onClik_manga = null;
    private LinearLayout querry;
    private SeekBar seekBar;
    private int total_chapter_int = 0;
    private String current_chap = "0";
    private Manga_info manga = null;
    private final String querry1 = "https://m.blogtruyen.com/timkiem?keyword=";
    private final static int REQUEST_CODE_1 = 1;
    private ArrayList<Link_info> ar_querry_item = null;
    private HashMap<String, String> file_content;
    String filename = "myfile";
    private File f;

    public String getOnClik_manga() {
        return onClik_manga;
    }

    public HashMap<String, String> getFile_content() {
        return file_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //normal stuff
        spinner = (Spinner) findViewById(R.id.spinner);
        seekBar = findViewById(R.id.seaker);
        currentChap = findViewById(R.id.currentchap);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Blog, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Blog = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //TextBox
        text = findViewById(R.id.edit_text);
        submit = findViewById(R.id.button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content_string= text.getText().toString();
                InputMethodManager inputManager =
                        (InputMethodManager) MainActivity.this.getBaseContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                switch (Blog) {
                    case "BlogTruyen":
                        try {
                            //using querry manga class to get all the manga with keyword
                            ar_querry_item = new Querry_Manga().execute(querry1 + content_string).get();
                            querry.removeAllViews();
                            if(ar_querry_item != null){
                                System.out.println("How many " + ar_querry_item.size());
                                for(final Link_info q: ar_querry_item) {
                                    TextView tv = new TextView(getApplicationContext());
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                                            LinearLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
                                    // Apply the layout parameters to TextView widget
                                    tv.setLayoutParams(lp);
                                    tv.setText(q.getTitle());
                                    tv.setTextSize(20);
                                    tv.setTextColor(Color.WHITE);
                                    tv.setClickable(true);
                                    tv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //get the link of the manga from search
                                            onClik_manga = q.getUrl();
                                            manga_info(onClik_manga);
                                        }
                                    });
                                    // Set a text color for TextView text
                                    querry.addView(tv);
                                }
                            }else{
                                //no such item exists
                                TextView tv = new TextView(getApplicationContext());
                                tv.setText("NO SUCH MANGA");
                                tv.setTextSize(22);
                                tv.setTextColor(Color.WHITE);
                                querry.addView(tv);

                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "NetTruyen":

                        break;
                    default:

                        break;
                }
            }
        });
        ar_querry_item = new ArrayList<>();
        file_content = new HashMap<>();
        querry = findViewById(R.id.querryLinear);

        //File access
        accessFile = findViewById(R.id.accessfile);
        //enable this 2 lines when fixing
//        f = new File(MainActivity.this.getFilesDir(), filename);
//        f.delete();
        f = new File(MainActivity.this.getFilesDir(), filename);
        ReadFile();
        //Read text from file
        accessFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                //first time go here
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                List_liked_manga homeFragment = new List_liked_manga();
                fragmentTransaction.replace(R.id.Main, homeFragment, "LL").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //handle send from chrome
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE_1:
                if(resultCode == RESULT_OK)
                {
                    int messageReturn = data.getIntExtra("Return",0);
                    System.out.println("Return message is" + messageReturn);
                    current_chap = Integer.toString(messageReturn);
                    seekBar.setProgress(Integer.parseInt(current_chap), true);
                    total_chapter_int = Integer.parseInt(manga.getTotal_chap()) - 1;
                    currentChap.setText(manga.getNamechap().get(total_chapter_int - Integer.parseInt(current_chap)));
                }
        }
    }

    //handle back press on fragment
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            manga_info(sharedText);
        }
    }

    //input a link out put update ui with manga detail coresspond to the passed link
    public void manga_info(String url){
        //change link from mobile to desktop
        url = url.substring(0, 8) + url.substring(10);
        onClik_manga = url;
        System.out.println("URL is" + url);
        String[] urls = new String[]{onClik_manga};
        try {
            manga = new GET_Manga_info(this).execute(urls).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Read a File into HashMap
    void ReadFile(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                file_content.put(line, br.readLine());
            }
            br.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }

    public void setOnClik_manga(String onClik_manga) {
        this.onClik_manga = onClik_manga;
    }

}
