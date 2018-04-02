package com.guyue.proj1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guyue.proj1.R;
import com.guyue.proj1.bean.NewsBean;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by huyun on 2018/3/21.
 */

public class NewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewsBean> newses;

    public NewsAdapter(Context context){
        this.mContext=context;
    }

    /**
     * 设置数据更新界面
     * @return
     */
    public void setData(List<NewsBean> newses){
        this.newses=newses;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return newses==null?0:newses.size();
    }



    @Override
    public NewsBean getItem(int i) {
        return newses==null?null:newses.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 得到相应的position对应的视图
     * position是当前Item的位置，convertView参数就是滑出屏幕的Item的View
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.newslist_item,null);
            vh.tv_title=convertView.findViewById(R.id.tv_title);
            vh.tv_from=convertView.findViewById(R.id.tv_from);
            vh.iv_newsimage=convertView.findViewById(R.id.iv_newsimage);
            convertView.setTag(vh);

        }else{
            //复用convertView
            vh= (ViewHolder) convertView.getTag();
        }

        final NewsBean news=getItem(position);


        if(news!=null){
            vh.tv_title.setText(news.getTitle());
            vh.tv_from.setText(news.getFrom());

            final MyHandler handler=new MyHandler(vh.iv_newsimage);

            //设置图片显示
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Bitmap bmp = getURLimage(news.getImageUrl());
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bmp;
                    Log.d("guyue","thread");
                    handler.sendMessage(msg);

                }
            }).start();
        }


        return convertView;
    }

    class ViewHolder{
        public TextView tv_title,tv_from;
        public ImageView iv_newsimage;
    }


    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    //在消息队列中实现对控件的更改
    class MyHandler extends Handler {
        ImageView iv;
        public MyHandler(ImageView iv){
            this.iv=iv;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d("guyue","handler");
                    Bitmap bmp=(Bitmap)msg.obj;
                    iv.setImageBitmap(bmp);
                    break;
            }
        }
    }




}
