package com.guyue.proj1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guyue.proj1.R;

import java.util.jar.Attributes;

/**
 * Created by huyun on 2018/3/20.
 */

public class ViewPagerIndicator extends LinearLayout {
    private int mCount;
    private int mIndex;
    private Context context;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context=context;
        setGravity(Gravity.CENTER); //设置布局居中
    }

    public ViewPagerIndicator(Context context){
        this(context,null);
    }

    /**
     * 设置滑动到当前小圆点时，其他原点的位置
     *
     */
    public void setCurrentPosition(int currentIndex){
        mIndex=currentIndex;  //当前小圆点
        removeAllViews();
        int pex=5;
        for(int i=0;i<mCount;i++){
            //创建一个ImageView控件来放置小圆点
            ImageView iv=new ImageView(context);
            if(mIndex==i){
                iv.setImageResource(R.drawable.indicator_on);
            }else{
                iv.setImageResource(R.drawable.indicator_off);
            }

            iv.setPadding(pex,0,pex,0);
            addView(iv);
        }
    }

    /**
     * 设置小圆点的数目
     */
    public void setCount(int count){
        this.mCount=count;
    }
}
