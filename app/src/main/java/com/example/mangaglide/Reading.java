package com.example.mangaglide;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class Reading extends FragmentActivity {
    private ArrayList<String> all_url;
    private int current;

    public ArrayList<String> getAll_url() {
        return all_url;
    }

    public int getCurrent() {
        return current;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        //get the fragment
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null){
            all_url = b.getStringArrayList("ALLURLs");
            current = Integer.parseInt(b.getString("CURRENT_INDEX"));
        }

        System.out.println("what is the urls" + all_url.get(0));

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.Frame, new Manga(),"MANGA");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
