package com.guyue.proj1.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.activity.LotteryInfoActivity;
import com.guyue.proj1.activity.NewsDetailActivity;
import com.guyue.proj1.adapter.AdBannerAdapter;
import com.guyue.proj1.adapter.NewsAdapter;
import com.guyue.proj1.bean.LotterySort;
import com.guyue.proj1.bean.NewsBean;
import com.guyue.proj1.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * Created by huyun on 2018/3/20.
 */

public class MainpageView{
    private static final String TAG = "MainpageView";

    private ListView lv_list;
    private NewsAdapter newsAdapter;
    private List<NewsBean> adNewses; //主页广告数据
    private List<NewsBean> newses; //主页显示的新闻数据


    private FragmentActivity mContext;
    private LayoutInflater mInflater;

    private View mCurrentView;
    private ViewPager adPager; //广告
    private View adBannerLay; //广告条容器
    private AdBannerAdapter ada; //适配器
    public static final int MSG_AD_SLID = 002; //广告自动滑动
    private ViewPagerIndicator vpi; //小圆点
    private MHandler mHandler;  //事件捕获
    private List<LotterySort> lotterySorts;

    private GridView gv_lotterysort; //彩票分类网格视图
    private List<Map<String, Object>> lotterys;
    private SimpleAdapter simpleAdapter;


    /**
     * 构造方法
     * @param context
     */
    public MainpageView(FragmentActivity context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * createView()
     */
    private void createView() {
        mHandler = new MHandler();
        getNewsData(); //获取新闻数据
        initLotteryData(); //初始化彩票数据
        init();

        new AdAutoSlidThread().start();

    }


    /**
     * 事件捕获
     */
    class MHandler extends Handler {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_AD_SLID:
                    if (ada.getCount() > 0) {
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);
                    }

                    break;

                case 1:
                    newsAdapter.setData(newses);
                    lv_list.setAdapter(newsAdapter);
                    lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(mContext, NewsDetailActivity.class);
                            String url=newses.get(i).getContentUrl();
                            intent.putExtra("url",url);
                            mContext.startActivity(intent);
                        }
                    });

            }
        }
    }


    /**
     * 获取首页新闻数据
     */
    public void getNewsData() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                newses= HttpUtils.getNewses(20);
                if(newses==null){
                    Toast.makeText(mContext,"加载新闻失败",Toast.LENGTH_SHORT).show();
                    return;
                }

                for(int i=3;i<newses.size();i++){
                    Log.d(TAG, "getNewsesData: "+newses.get(i).getTitle());
                }


                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

            }
        }).start();




    }

    /**
     * 初始化广告中的数据
     */
    private void initAdNewses() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                adNewses= HttpUtils.getNewses(3);
                if(adNewses==null){
                    Toast.makeText(mContext,"加载广告失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i=0;i<adNewses.size();i++){
                    Log.d(TAG,"initAdNewses:"+adNewses.get(i).getTitle());
                }
                countDownLatch.countDown();

            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (adNewses != null) {
            if (adNewses.size() > 0) {
                vpi.setCount(adNewses.size());
                vpi.setCurrentPosition(0);
            }
            ada.setDatas(adNewses);
        }




    }



    /**
     * 初始化控件
     */
    private void init() {
        mCurrentView = mInflater.inflate(R.layout.mainpage, null);

        //新闻列表
        lv_list = mCurrentView.findViewById(R.id.lv_list);
        newsAdapter = new NewsAdapter(mContext);

        //显示广告的ViewPager
        adPager = mCurrentView.findViewById(R.id.vp_advertBanner);
        adPager.setLongClickable(false);
        ada = new AdBannerAdapter(mContext.getSupportFragmentManager(),mHandler);
        adPager.setAdapter(ada);                //为adPager(ViewPager)设置适配器
        adPager.setOnTouchListener(ada);
        vpi = mCurrentView.findViewById(R.id.vpi_advert_indicator);
        vpi.setCount(ada.getSize()); //设置小圆点的个数
        adBannerLay = mCurrentView.findViewById(R.id.rl_adBanner); //广告条容器
        initAdNewses();  //初始化ViewPager相关的数据


        gv_lotterysort = mCurrentView.findViewById(R.id.gv_lotterysort);
        String[] from = {"img", "text"};
        int[] to = {R.id.img, R.id.text};
        simpleAdapter = new SimpleAdapter(mContext, lotterys, R.layout.lottery_sort_gradview_item, from, to);
        gv_lotterysort.setAdapter(simpleAdapter);
        gv_lotterysort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(mContext, LotteryInfoActivity.class);
                intent.putExtra("flag", arg2);
                mContext.startActivity(intent);

            }
        });



        //设置ViewPager页面改变监听器
        adPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (ada.getSize() > 0) {
                    //由于index数据在滑动时是累加的，因此用index%ada.getSize()来标记滑动到的当前位置
                    vpi.setCurrentPosition(position % ada.getSize());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        resetSize();



    }


    /**
     * 广告自动滑动
     */
    class AdAutoSlidThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(MSG_AD_SLID);
                }
            }
        }
    }





    /**
     * 计算控件大小
     */
    private void resetSize() {
        int sw = getScreenWidth(mContext);

    }

    /**
     * 读取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }



    /**
     * 初始化彩票分类的数据
     */
    private void initLotteryData() {

        //图标
        int icon[] = {R.drawable.ssq, R.drawable.fc3d, R.drawable.dlt, R.drawable.pl3, R.drawable.pl5, R.drawable.qxc, R.drawable.qlc};
        //图标下的文字
        String name[] = {"双色球", "福彩3D", "大乐透", "排列3", "排列5", "七星彩", "七乐彩"};
        lotterys = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("text", name[i]);
            lotterys.add(map);
        }

    }





    /**
     * 获取当前在导航栏上方显示对应的View
     */
    public View getView() {
        if (mCurrentView == null) {
            createView();

        }
        return mCurrentView;
    }

    /**
     * 显示当前在导航栏上方所对应的View界面
     */
    public void showView() {
        if (mCurrentView == null) {
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }



}
