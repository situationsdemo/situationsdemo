package com.demo.situations;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

/**
 * Created by parth on 03/13/2018.
 */

public class GridVideoView extends VideoView {
    public GridVideoView(Context context){
        super(context);
    }

    public GridVideoView(Context context, AttributeSet set){
        super(context,set);
    }

    public GridVideoView(Context context, AttributeSet set, int defstyle){
        super(context,set,defstyle);
    }

    @Override
    public void onMeasure(int width, int height){
        if(width > height){
            super.onMeasure(width,width);
        }
        else if(height > width){
            super.onMeasure(height,height);
        }
        else{
            super.onMeasure(width,height);
        }
        Log.d("Adapter",Integer.toString(width)+" "+Integer.toString(height));
    }
}
