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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.activity.LotteryInfoActivity;
import com.guyue.proj1.activity.NewsDetailActivity;
import com.guyue.proj1.adapter.AdBannerAdapter;
import com.guyue.proj1.adapter.NewsAdapter;
import com.guyue.proj1.bean.LotteryInfo;
import com.guyue.proj1.bean.LotterySort;
import com.guyue.proj1.bean.NewsBean;
import com.guyue.proj1.utils.AnalysisUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by huyun on 2018/3/20.
 */

public class MainpageView{

    private ListView lv_list;
    private NewsAdapter newsAdapter;
    private List<NewsBean> newses;

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
        initAdData(); //初始化广告数据
        getNewsData();
        initLotteryData();
        initView();
        new AdAutoSlidThread().start();

    }

    /**
     * 初始化广告中的数据
     */
    private void initAdData() {
        lotterySorts = new ArrayList<LotterySort>();
        for (int i = 0; i < 3; i++) {
            LotterySort lotterySort = new LotterySort();
            lotterySort.id = (i + 1);
            switch (i) {

                case 0:
                    lotterySort.icon = "banner_1";
                    break;
                case 1:
                    lotterySort.icon = "banner_2";
                    break;
                case 2:
                    lotterySort.icon = "banner_3";
                    break;
                default:
                    break;
            }
            lotterySorts.add(lotterySort);

        }
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
                    String jsonString=(String) msg.obj;
                    parseJsontoLotteryInfo(jsonString);
                    newsAdapter.setData(newses);
                    lv_list.setAdapter(newsAdapter);
                    //设置lv_list高度
                    ViewGroup.LayoutParams params = lv_list.getLayoutParams();
                    params.height=dp2px(mContext,newses.size()*120);
                    lv_list.setLayoutParams(params);

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
     * dp转换成px
     */
    private int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    private void parseJsontoLotteryInfo(String jsonString) {
        newses = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray data=result.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                NewsBean newsBean = new NewsBean();
                JSONObject item = data.getJSONObject(i);
                newsBean.setTitle(item.getString("title"));
                newsBean.setFrom(item.getString("author_name")+"  "+item.getString("date"));
                newsBean.setImageUrl(item.getString("thumbnail_pic_s"));
                newsBean.setContentUrl(item.getString("url"));
                newses.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
     * 初始化控件
     */
    private void initView() {
        mCurrentView = mInflater.inflate(R.layout.mainpage, null);

        //新闻列表
        lv_list = mCurrentView.findViewById(R.id.lv_list);
        newsAdapter = new NewsAdapter(mContext);

        //显示广告的ViewPager
        adPager = mCurrentView.findViewById(R.id.vp_advertBanner);
//        adPager.setLongClickable(false);
        ada = new AdBannerAdapter(mContext.getSupportFragmentManager(), mHandler);
        adPager.setAdapter(ada);                //为adPager(ViewPager)设置适配器
        adPager.setOnTouchListener(ada);




        vpi = mCurrentView.findViewById(R.id.vpi_advert_indicator);
        vpi.setCount(ada.getSize()); //设置小圆点的个数
        adBannerLay = mCurrentView.findViewById(R.id.rl_adBanner); //广告条容器

        gv_lotterysort = mCurrentView.findViewById(R.id.gv_lotterysort);
        String[] from = {"img", "text"};
        int[] to = {R.id.img, R.id.text};
        simpleAdapter = new SimpleAdapter(mContext, lotterys, R.layout.lottery_sort_gradview_item, from, to);
        gv_lotterysort.setAdapter(simpleAdapter);
        gv_lotterysort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Toast.makeText(mContext, "点击-"+arg2+"-"+arg3, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, LotteryInfoActivity.class);
                intent.putExtra("flag", arg2);
                mContext.startActivity(intent);

            }
        });


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

        if (lotterySorts != null) {
            if (lotterySorts.size() > 0) {
                vpi.setCount(lotterySorts.size());
                vpi.setCurrentPosition(0);
            }
            ada.setDatas(lotterySorts);
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

//    public void getNewsData() {
//        try {
//            InputStream is = mContext.getResources().getAssets().open("news.xml");
//            newses = AnalysisUtils.getNewsInfo(is);
//            Log.d("MainpageView", newses.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public void getNewsData() {
        String url = "http://toutiao-ali.juheapi.com/toutiao/index";
        String appcode = "1b2556a6a5a0446d8be17f74d033abfb";
        final Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("Authorization", "APPCODE " + appcode);  //将请求头以键值对形式添加，可添加多个请求头
        RequestBody requestBody = new FormBody.Builder().add("type", "tiyu").build();
        final Request request = builder.build();
        builder.post(requestBody);
        final OkHttpClient client = new OkHttpClient.Builder().build(); //设置各种超时时间
        final Call call = client.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String jsonString=response.body().string();

                    if (response != null) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj =jsonString;
                        mHandler.sendMessage(msg);
                    } else {
                        Log.d("guyue", "response is null");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


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
