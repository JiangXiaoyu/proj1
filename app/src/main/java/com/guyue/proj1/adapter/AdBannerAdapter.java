package com.guyue.proj1.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.guyue.proj1.activity.NewsDetailActivity;
import com.guyue.proj1.bean.LotterySort;
import com.guyue.proj1.bean.NewsBean;
import com.guyue.proj1.fragment.AdBannerFragment;
import com.guyue.proj1.view.MainpageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyun on 2018/3/20.
 */

public class AdBannerAdapter extends FragmentStatePagerAdapter implements View.OnTouchListener {

    private Handler mHandler;
    private List<NewsBean> newses;
    public static int newsPosition;

    public AdBannerAdapter(FragmentManager fm) {
        super(fm);
        newses=new ArrayList<>();
    }

    public AdBannerAdapter(FragmentManager fm, Handler handler) {
        super(fm);
        this.mHandler = handler;
        newses=new ArrayList<>();

    }

    /**
     * 设置数据更新界面
     */
    public void setDatas(List<NewsBean> newses) {
        this.newses = newses;
        notifyDataSetChanged();
    }

    /**
     * 返回数据集的真实容量大小
     * @return
     */
    public int getSize() {
        return newses == null ? 0 : newses.size();
    }


    @Override
    public int getItemPosition(Object object) {
        //防止刷新结果显示列表时出现缓存数据，重载这个函数，使之默认返回POSITION_NONE
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle args=new Bundle();
        if(newses.size()>0){
            args.putSerializable("news",newses.get(position % newses.size()));
        }
        newsPosition=position % newses.size();
        return AdBannerFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mHandler.removeMessages(MainpageView.MSG_AD_SLID);

        return false;
    }


}
