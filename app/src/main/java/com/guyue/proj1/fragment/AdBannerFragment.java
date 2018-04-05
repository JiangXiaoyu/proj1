package com.guyue.proj1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guyue.proj1.R;
import com.guyue.proj1.activity.NewsDetailActivity;
import com.guyue.proj1.adapter.AdBannerAdapter;
import com.guyue.proj1.bean.NewsBean;

/**
 * Created by huyun on 2018/3/20.
 */

public class AdBannerFragment extends Fragment implements View.OnClickListener{

    private static final String TAG="AdBannerFragment";
    private NewsBean news;
    private ImageView iv;
    private TextView tv;


    public static AdBannerFragment newInstance(Bundle args){
        AdBannerFragment af=new AdBannerFragment();
        af.setArguments(args);
        return af;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg=getArguments();
        news= (NewsBean) arg.getSerializable("news");

    }

    @Override
    public void onResume() {
        super.onResume();
        //加载广告图片
        Glide.with(getActivity()).load(news.getImageUrl()).into(iv);
        tv.setText(news.getTitle());
        iv.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(iv!=null){
            iv.setImageDrawable(null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.adbanner_fragment_layout,container,false);
        iv=view.findViewById(R.id.imageview);
        tv=view.findViewById(R.id.textview);


        return view;

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),NewsDetailActivity.class);
        String url=news.getContentUrl();
        intent.putExtra("url",url);
        getActivity().startActivity(intent);
    }
}
