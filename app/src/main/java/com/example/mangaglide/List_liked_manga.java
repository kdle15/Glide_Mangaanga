package com.example.mangaglide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class List_liked_manga extends Fragment {
    private TextView tv;
    private File f;
    private final String filename = "myfile";
    private String s = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        f = new File(getActivity().getFilesDir(), filename);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append(System.getProperty("line.separator"));
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        s = text.toString();
        return inflater.inflate(R.layout.fragment_list_liked_manga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = view.findViewById(R.id.test);
        tv.setText(s);

    }
}
