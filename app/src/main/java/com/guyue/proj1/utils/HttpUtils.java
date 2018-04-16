package com.guyue.proj1.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.guyue.proj1.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huyun on 2018/4/4.
 */

public class HttpUtils {
    private static final String TAG = "HttpUtils";

    public static List<NewsBean> getNewses(int count) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String access_key = "0lGiHehD7L457ORY";
        String secret_key = "a74922ed823d4d25b7d09db39024cd6c";
        String signature = MD5Utils.md5(secret_key + timestamp + access_key);
        String url = "https://api.xinwen.cn/news/hot?size=500" + "&category=Sport&access_key=" + access_key + "&timestamp=" + timestamp + "&signature=" + signature;
        Log.d(TAG, "URL链接：" + url);


        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.build();

        OkHttpClient client = new OkHttpClient.Builder().build();
        final Call call = client.newCall(request);
        List<NewsBean> newses=null;
        try {
            newses=new ArrayList<>();
            Response response = call.execute();
            String jsonString = response.body().string();
            newses=parseToNewses(jsonString,count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newses;
    }

    public static List<NewsBean> parseToNewses(String jsonString,int size) {

        List<NewsBean> newses = new ArrayList<>();
        if (TextUtils.isEmpty(jsonString)) {
            Log.d(TAG, "jsonString 是空的");
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            boolean isSuccess = jsonObject.getBoolean("success");
            String msg = jsonObject.getString("msg");
            if (!isSuccess) {
                Log.d(TAG, "parseToNewses: "+msg);
                return null;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            int count = data.getInt("count");
            JSONArray news = data.getJSONArray("news");
            for (int i = 0; i < count; i++) {
                if (newses.size() == size) {
                    break;
                }
                JSONObject newsObject = news.getJSONObject(i);
                NewsBean newsBean = new NewsBean();
                String id = newsObject.getString("news_id");
                String title = newsObject.getString("title");
                String from = newsObject.getString("source");
                String publishTime=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(newsObject.getLong("gmt_publish"));

                JSONArray thumbnail_img = newsObject.getJSONArray("thumbnail_img");

                if (thumbnail_img.isNull(0)) {
                    continue;
                }

                String imageUrl = thumbnail_img.getString(0);
                String contentUrl = newsObject.getString("url");


                newsBean.setId(id);
                newsBean.setTitle(title);
                newsBean.setFrom(from);
                newsBean.setPublishTime(publishTime);
                newsBean.setImageUrl(imageUrl);
                newsBean.setContentUrl(contentUrl);

                newses.add(newsBean);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newses;
    }


    /**
     * @param category 'hot','all'
     */
    public static List<NewsBean> parseJsonString(String category, String jsonString, Context context) {

        List<NewsBean> newses = null;

        if ("hot".equals(category)) {
            newses = new ArrayList<>();
            if (TextUtils.isEmpty(jsonString)) {
                Log.d("HttpUtils", "category 'hot' jsonString 是空的");
                return null;
            }

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean isSuccess = jsonObject.getBoolean("success");
                String msg = jsonObject.getString("msg");
                if (!isSuccess) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return null;
                }
                JSONObject data = jsonObject.getJSONObject("data");
                int count = data.getInt("count");
                JSONArray news = data.getJSONArray("news");
                for (int i = 0; i < count; i++) {
                    if (newses.size() == 3) {
                        break;
                    }

                    JSONObject newsObject = news.getJSONObject(i);
                    NewsBean newsBean = new NewsBean();
                    String id = newsObject.getString("news_id");
                    String title = newsObject.getString("title");
                    String from = newsObject.getString("source");
                    JSONArray thumbnail_img = newsObject.getJSONArray("thumbnail_img");

                    if (thumbnail_img.isNull(0)) {
                        continue;
                    }

                    String imageUrl = thumbnail_img.getString(0);
                    String contentUrl = newsObject.getString("url");


                    newsBean.setId(id);
                    newsBean.setTitle(title);
                    newsBean.setFrom(from);
                    newsBean.setImageUrl(imageUrl);
                    newsBean.setContentUrl(contentUrl);

                    newses.add(newsBean);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if ("all".equals(category)) {
            newses = new ArrayList<>();
            if (TextUtils.isEmpty(jsonString)) {
                Log.d("HttpUtils", "category 'all' jsonString 是空的");
                return null;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean isSuccess = jsonObject.getBoolean("success");
                String msg = jsonObject.getString("msg");
                if (!isSuccess) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return null;
                }
                JSONObject data = jsonObject.getJSONObject("data");
                int count = data.getInt("count");
                JSONArray news = data.getJSONArray("news");
                for (int i = 0; i < count; i++) {
                    if (newses.size() == 20) {
                        break;
                    }

                    JSONObject newsObject = news.getJSONObject(i);
                    NewsBean newsBean = new NewsBean();
                    String id = newsObject.getString("news_id");
                    String title = newsObject.getString("title");
                    String from = newsObject.getString("source");
                    JSONArray thumbnail_img = newsObject.getJSONArray("thumbnail_img");

                    if (thumbnail_img.isNull(0)) {
                        continue;
                    }

                    String imageUrl = thumbnail_img.getString(0);
                    String contentUrl = newsObject.getString("url");


                    newsBean.setId(id);
                    newsBean.setTitle(title);
                    newsBean.setFrom(from);
                    newsBean.setImageUrl(imageUrl);
                    newsBean.setContentUrl(contentUrl);

                    newses.add(newsBean);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return newses;
    }
}
