package com.example.mangaglide;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class Reading extends FragmentActivity {
    private ArrayList<String> all_url;
    private int current;
    private final String SIMPLE_FRAGMENT_TAG = "myfragmenttag";

    public ArrayList<String> getAll_url() {
        return all_url;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current += current;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null){
            all_url = b.getStringArrayList("ALLURLs");
            current = Integer.parseInt(b.getString("CURRENT_INDEX"));
        }
        FragmentManager manager = getFragmentManager();
        //first time go here
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Manga homeFragment = new Manga();
        fragmentTransaction.add(R.id.Frame, homeFragment, SIMPLE_FRAGMENT_TAG);
        fragmentTransaction.commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        current = outState.getInt("current");
        all_url = outState.getStringArrayList("allurls");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("allurls", all_url);
        savedInstanceState.putInt("current", current);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //have to pass the data on this method
        Intent intent = new Intent();
        intent.putExtra("Return", current);
        setResult(RESULT_OK, intent);
        System.out.println("Return message is" + current);
        finish();
        super.onBackPressed();
    }
}
