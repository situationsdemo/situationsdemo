package com.demo.situations;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created on 03/05/2018 at 20:00.
 */

public class ImageviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> dataUrls;
    private LayoutInflater layoutInflater;

    ImageviewAdapter(Context inContext, ArrayList<String> data){
        context = inContext;
        dataUrls = data;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return dataUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GridImageView imageView;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.imageview, viewGroup, false);

        }
        imageView = view.findViewById(R.id.imageView);
        Picasso.with(context).load(dataUrls.get(i)).into(imageView);

        return view;
    }

}
