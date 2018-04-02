package com.guyue.proj1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;

import com.guyue.proj1.bean.NewsBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyun on 2018/3/19.
 */

public class AnalysisUtils {
    public static String readLoginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String username = sp.getString("loginUserName", "");
        return username;
    }

    /**
     * 解析新闻信息
     */
    public static List<NewsBean> getNewsInfo(InputStream is) throws Exception {

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        List<NewsBean> newses = null;
        NewsBean news = null;
        int count = 0;
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("infos".equals(parser.getName())) {
                        newses = new ArrayList<>();
                    } else if ("news".equals(parser.getName())) {
                        news = new NewsBean();
                        String ids = parser.getAttributeValue(0);
                        news.setId(Integer.parseInt(ids));
                    } else if ("title".equals(parser.getName())) {
                        news.setTitle(parser.nextText());
                    } else if ("from".equals(parser.getName())) {
                        news.setFrom(parser.nextText());
                    }else if("imageUrl".equals(parser.getName())){
                        news.setImageUrl(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("news".equals(parser.getName())) {
                        count++;
                        newses.add(news);
                        news = null;
                    }
                    break;

            }
            type = parser.next();
        }

        return newses;
    }
}
