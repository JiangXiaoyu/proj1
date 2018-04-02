package com.guyue.proj1.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;

import com.guyue.proj1.bean.LotterySort;
import com.guyue.proj1.fragment.AdBannerFragment;
import com.guyue.proj1.view.MainpageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyun on 2018/3/20.
 */

public class AdBannerAdapter extends FragmentStatePagerAdapter implements View.OnTouchListener {

    private Handler mHandler;
    private List<LotterySort> lotterySorts;


    public AdBannerAdapter(FragmentManager fm) {
        super(fm);
        lotterySorts = new ArrayList<LotterySort>();
    }

    public AdBannerAdapter(FragmentManager fm, Handler mHandler) {
        super(fm);
        this.mHandler = mHandler;
        lotterySorts = new ArrayList<LotterySort>();
    }

    /**
     * 设置数据更新界面
     */
    public void setDatas(List<LotterySort> lotterySorts) {
        this.lotterySorts = lotterySorts;
        notifyDataSetChanged();
    }

    /**
     * 返回数据集的真实容量大小
     * @return
     */
    public int getSize() {
        return lotterySorts == null ? 0 : lotterySorts.size();
    }


    @Override
    public int getItemPosition(Object object) {
        //防止刷新结果显示列表时出现缓存数据，重载这个函数，使之默认返回POSITION_NONE
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args=new Bundle();
        if(lotterySorts.size()>0){
            args.putString("ad",lotterySorts.get(position % lotterySorts.size()).icon);
        }
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
