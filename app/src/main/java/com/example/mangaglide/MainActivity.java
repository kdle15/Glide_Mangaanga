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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Initiate fragment
 */
public class MainActivity extends FragmentActivity {
    private Spinner spinner;
    private Button submit;
    private Button currentChap;
    private Button accessFile;
    private Button addtoFile;
    private EditText text;
    private String Blog = null;
    private String onClik_manga = null;
    private LinearLayout querry;
    private TextView title;
    private TextView totalChap;
    private TextView category;
    private TextView content;
    private SeekBar seekBar;
    private int total_chapter_int = 0;
    private String current_chap = "0";
    private Manga_info manga = null;
    private final String querry1 = "https://m.blogtruyen.com/timkiem?keyword=";
    private final static int REQUEST_CODE_1 = 1;
    private ArrayList<Link_info> ar_querry_item = null;
    String filename = "myfile";
    private File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //normal stuff
        spinner = (Spinner) findViewById(R.id.spinner);
        title = findViewById(R.id.title);
        totalChap = findViewById(R.id.totalchap);
        category = findViewById(R.id.cate);
        content = findViewById(R.id.content);
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
        querry = findViewById(R.id.querryLinear);

        //File access
        accessFile = findViewById(R.id.accessfile);
        addtoFile = findViewById(R.id.addtolist);

        //TODO: remove when done
        f = new File(MainActivity.this.getFilesDir(), filename);
        f.delete();
        f = new File(MainActivity.this.getFilesDir(), filename);
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
                System.out.println("hello123 " + resultCode);
                if(resultCode == RESULT_OK)
                {
                    int messageReturn = data.getIntExtra("Return",0);
                    System.out.println("Return message is" + messageReturn);
                    current_chap = Integer.toString(messageReturn);
                    seekBar.setProgress(Integer.parseInt(current_chap), true);
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
    void manga_info(String url){
        //change link from mobile to desktop
        url = url.substring(0, 8) + url.substring(10);
        System.out.println("URL is" + url);
        String[] urls = new String[]{url};
        try {
            manga = new GET_Manga_info().execute(urls).get();
            if(manga != null){
                title.setText(manga.getTitle());
                totalChap.setText("Chap " + manga.getTotal_chap());
                total_chapter_int = Integer.parseInt(manga.getTotal_chap()) - 1;
                seekBar.setMax(total_chapter_int);
                seekBar.setProgress(0);
                currentChap.setText(manga.getNamechap().get(total_chapter_int - Integer.parseInt(current_chap)));
                category.setText(manga.getCategory());
                content.setText(manga.getIntroduction());

                //add manga to file
                addtoFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //create a file
                        String fileContents = manga.getTitle()+"/n";
                        String URLcontents = onClik_manga+"/n";
                        FileOutputStream outputStream;
                        try {
                            outputStream = openFileOutput(filename, Context.MODE_APPEND);
                            outputStream.write(fileContents.getBytes());
                            outputStream.write(URLcontents.getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        Intent i = new Intent(getApplicationContext(), Reading.class);
                        i.putStringArrayListExtra("ALLURLs", manga.getChaps());
                        i.putExtra("CURRENT_INDEX", current_chap);
                        startActivityForResult(i, REQUEST_CODE_1);
                    }
                });
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
