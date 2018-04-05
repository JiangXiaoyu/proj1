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
    private static final String TAG="HttpUtils";

    public static String okhttpUtil(int count){
        String timestamp=String.valueOf(System.currentTimeMillis());
        String access_key="0lGiHehD7L457ORY";
        String secret_key="a74922ed823d4d25b7d09db39024cd6c";
        String signature = MD5Utils.md5(secret_key+timestamp+access_key);
        String url="https://api.xinwen.cn/news/hot?size="+count+"&category=Sport&access_key="+access_key+"&timestamp="+timestamp+"&signature="+signature;
        Log.d(TAG,"URL链接："+url);



        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.build();

        OkHttpClient client = new OkHttpClient.Builder().build();
        final Call call = client.newCall(request);
        String jsonString="";
        try {
            Response response = call.execute();
            jsonString=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonString;
    }


    public static List<NewsBean> parseJsonString(String category, String jsonString, Context context){

        List<NewsBean> newses=null;
        if ("hot".equals(category)) {
            newses=new ArrayList<>();
            if(TextUtils.isEmpty(jsonString)){
                Log.d("HttpUtils","jsonString 是空的");
                return null;
            }

            try {
                JSONObject jsonObject=new JSONObject(jsonString);
                boolean isSuccess=jsonObject.getBoolean("success");
                String msg=jsonObject.getString("msg");
                if(!isSuccess){
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    return null;
                }
                JSONObject data=jsonObject.getJSONObject("data");
                int count=data.getInt("count");
                JSONArray news=data.getJSONArray("news");
                for(int i=0;i<count;i++){
                    if(newses.size()==3){
                        Log.d("胡云晕","newses满3个数据了");
                        break;
                    }

                    JSONObject newsObject=news.getJSONObject(i);
                    NewsBean newsBean=new NewsBean();
                    String id=newsObject.getString("news_id");
                    String title=newsObject.getString("title");
                    String from=newsObject.getString("source");
                    JSONArray thumbnail_img=newsObject.getJSONArray("thumbnail_img");

                    if(thumbnail_img.isNull(0)){
                        Log.d("胡云晕"+i,i+"的图片是null");
                        continue;
                    }

                    String imageUrl=thumbnail_img.getString(0);
                    String contentUrl=newsObject.getString("url");


                    Log.d("胡云晕",id+"-"+title+"-"+from);
                    Log.d("胡云晕",imageUrl);
                    Log.d("胡云晕",contentUrl);

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
