package com.guyue.proj1.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.activity.NewsDetailActivity;
import com.guyue.proj1.adapter.NewsAdapter;
import com.guyue.proj1.bean.NewsBean;
import com.guyue.proj1.utils.HttpUtils;

import java.util.List;

/**
 * Created by huyun on 2018/3/22.
 */

public class NewsView {

    private static final String TAG = "NewsView";
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
        mCurrentView=mInflater.inflate(R.layout.newsview,null);
        lv_list=mCurrentView.findViewById(R.id.lv_list);
        newsAdapter=new NewsAdapter(mContext);




    }

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
                msg.obj=newses;
                mHandler.sendMessage(msg);

            }
        }).start();


    }

    class MHandler extends Handler {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
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
