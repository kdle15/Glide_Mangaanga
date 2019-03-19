package com.example.mangaglide;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class List_liked_manga extends Fragment {
    private TextView tv;
    private File f;
    private LinearLayout frame;
    private final String filename = "myfile";
    private String s = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_liked_manga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frame = view.findViewById(R.id.list_frame);
        frame.removeAllViews();
        f = new File(getActivity().getFilesDir(), filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                if(i%2 != 0){
                    TextView b = new TextView(this.getContext());
                    b.setTextSize(20);
                    b.setText(line);
                    b.setTextColor(Color.WHITE);
                    b.setClickable(true);
                    final String r = line;
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).setOnClik_manga(s);
                            String[] urls = new String[]{s};
                            try {
                                Manga_info manga = new GET_Manga_info(List_liked_manga.this.getActivity()).execute(urls).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    frame.addView(b);
                }else {
                    s = line;
                }
                i++;
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }
}
