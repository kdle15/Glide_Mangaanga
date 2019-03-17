package com.example.mangaglide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Initiate fragment
 */
public class MainActivity extends FragmentActivity {

    ImageView tv;
    Spinner spinner;
    Button submit;
    EditText text;
    String Blog = null;
    String onClik_manga = null;
    LinearLayout querry;
    private final String querry1 = "https://m.blogtruyen.com/timkiem?keyword=";
    ArrayList<Link_info> ar_querry_item = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
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
                String content = text.getText().toString();
                switch (Blog) {
                    case "BlogTruyen":
                        try {
                            ar_querry_item = new Querry_Manga().execute(querry1 + content).get();
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
                                    tv.setClickable(true);
                                    tv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            onClik_manga = q.getUrl();
                                            //new intent go here
                                            Intent i = new Intent(MainActivity.this, Manga_Page.class);
                                            onClik_manga = onClik_manga.substring(0, 8) + onClik_manga.substring(10);
                                            i.putExtra("EXTRA_MESSAGE", onClik_manga);
                                            startActivity(i);

                                            System.out.println("all link go here " + onClik_manga);
                                        }
                                    });
                                    // Set a text color for TextView text
                                    tv.setTextColor(Color.WHITE);
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
    }
}
