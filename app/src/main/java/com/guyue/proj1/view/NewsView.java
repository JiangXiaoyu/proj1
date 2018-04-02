package com.guyue.proj1.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.guyue.proj1.R;
import com.guyue.proj1.activity.NewsDetailActivity;
import com.guyue.proj1.adapter.NewsAdapter;
import com.guyue.proj1.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huyun on 2018/3/22.
 */

public class NewsView {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewsBean> newses;
    private MHandler mHandler;

    private View mCurrentView;
    private ListView lv_list;
    private NewsAdapter newsAdapter;

    public NewsView(Activity context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    private void createView(){
        mHandler=new MHandler();

        getNewsData();
        initView();

    }

    private void initView(){
        Log.d("guyue","VVVVVVVVVVVVVVVVVVV-1");
        mCurrentView=mInflater.inflate(R.layout.newsview,null);
        lv_list=mCurrentView.findViewById(R.id.lv_list);
        newsAdapter=new NewsAdapter(mContext);




    }

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
                        msg.what = 2;
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

    class MHandler extends Handler {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                 case 2:
                    String jsonString=(String) msg.obj;
                    parseJsontoLotteryInfo(jsonString);
                    newsAdapter.setData(newses);
                    lv_list.setAdapter(newsAdapter);
                    //设置lv_list高度
//                    ViewGroup.LayoutParams params = lv_list.getLayoutParams();
//                    params.height=dp2px(mContext,newses.size()*120);
//                    lv_list.setLayoutParams(params);

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

    public View getView(){
        if(mCurrentView==null){
            createView();
        }
        return mCurrentView;

    }

    public void showView(){
        if(mCurrentView==null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
}
