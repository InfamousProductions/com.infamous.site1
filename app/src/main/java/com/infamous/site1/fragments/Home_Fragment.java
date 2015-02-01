package com.infamous.site1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

import com.infamous.site1.R;
import com.infamous.site1.activities.MainActivity;

import java.util.ArrayList;
import android.net.*;

/**
 * Created by Daniel on 09.11.2014.
 */
public class Home_Fragment extends Fragment implements View.OnClickListener {

	public static final int APPLY = 0;
    ArrayList<String> dialogItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Button dialog = (Button) rootView.findViewById(R.id.site);
        Button singleDialog = (Button) rootView.findViewById(R.id.apps);
        Button persSingleDialog = (Button) rootView.findViewById(R.id.blog);
        Button multiDialog = (Button) rootView.findViewById(R.id.downloads);

		
        dialog.setOnClickListener(this);
        singleDialog.setOnClickListener(this);
        persSingleDialog.setOnClickListener(this);
        multiDialog.setOnClickListener(this);

        dialogItems = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            dialogItems.add("Item No. "+i);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.site:
                Intent forum = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
				("http://infamousdevelopment.com/forum")); 
				startActivity(forum);
                break;
            case R.id.apps:
                Intent black = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
				("http://infamousgit.com"));
				startActivity(black);
				break;
            case R.id.blog:
                Intent request = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
				("http://infamousdevelopment.com"));
				startActivity(request);
				break;
            case R.id.downloads:
                Intent downloads = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
				("http://infamous-downloads.github.io/"));
				startActivity(downloads);
				break;
        }
    }
}
