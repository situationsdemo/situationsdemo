package com.demo.situations;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by parth on 03/13/2018.
 */

public class GridImageView extends android.support.v7.widget.AppCompatImageView {

    public GridImageView(Context context){
        super(context);
    }

    public GridImageView(Context context, AttributeSet set){
        super(context,set);
    }

    public GridImageView(Context context, AttributeSet set, int defstyle){
        super(context,set,defstyle);
    }

    @Override
    public void onMeasure(int width, int height){
        if(width < height){
            super.onMeasure(width,width);
        }
        else if(height < width){
            super.onMeasure(height,height);
        }
        else{
            super.onMeasure(width,height);
        }
    }
}
