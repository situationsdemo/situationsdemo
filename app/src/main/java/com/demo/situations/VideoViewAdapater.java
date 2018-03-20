package com.demo.situations;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

interface onItemSelectedListener{
    void getItemSelected(GridVideoView videoView, String VideoUrl);
}
class ViewHolder{
    GridVideoView videoView;
    ImageView dummyImage;
    ImageView checkMark;

    public ViewHolder(GridVideoView InVideoView, ImageView InDummyImage, ImageView InCheckMark){
        this.videoView = InVideoView;
        this.dummyImage = InDummyImage;
        this.checkMark = InCheckMark;
    }
}
public class VideoViewAdapater extends BaseAdapter {
    private Context context;
    private ArrayList<String> dataUrls;
    private LayoutInflater layoutInflater;
    private GridVideoView lastVideoClicked;
    private ImageView lastSelectedCheckmark;
    private onItemSelectedListener itemSelectedListener;

    VideoViewAdapater(Context inContext, ArrayList<String> data){
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
        return dataUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final GridVideoView videoView;
        final ImageView dummyImageView;
        final ImageView checkmark;
        final ViewHolder viewHolder;
        final int index = i;
        Uri uri = Uri.parse(dataUrls.get(i));
        if(view == null) {
            view = layoutInflater.inflate(R.layout.videoview, viewGroup, false);
            videoView = view.findViewById(R.id.videoview);
            dummyImageView = view.findViewById(R.id.dummyImage);
            checkmark = view.findViewById(R.id.checkmark);
            viewHolder = new ViewHolder(videoView, dummyImageView, checkmark);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
            videoView = viewHolder.videoView;
            checkmark = viewHolder.checkMark;
            dummyImageView = viewHolder.dummyImage;
        }

        checkmark.setVisibility(View.GONE);
        dummyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastSelectedCheckmark == null){
                    checkmark.setVisibility(View.VISIBLE);
                    lastSelectedCheckmark = checkmark;
                }
                else{
                    checkmark.setVisibility(View.VISIBLE);
                    lastSelectedCheckmark.setVisibility(View.GONE);
                    lastSelectedCheckmark = checkmark;
                }
                if(lastVideoClicked == null){
                    lastVideoClicked = videoView;
                    videoView.start();
                }
                else if(lastVideoClicked == videoView){
                    videoView.pause();
                }
                else{
                    lastVideoClicked.pause();
                    videoView.start();
                    lastVideoClicked = videoView;
                }
                if(itemSelectedListener != null){
                    itemSelectedListener.getItemSelected(videoView,dataUrls.get(index));
                }
            }
        });
        videoView.setVideoURI(uri);
        videoView.seekTo(2);//load first frame as thumbnail

        return view;
    }

    public void setOnItemSelectedListener(onItemSelectedListener listener){
        itemSelectedListener = listener;
    }

}
