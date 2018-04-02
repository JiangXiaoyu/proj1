package com.guyue.proj1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.view.MainpageView;
import com.guyue.proj1.view.MyInfoView;
import com.guyue.proj1.view.NewsView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{





    //中间内容栏
    private FrameLayout mBodyLayout;
    //底部按钮栏
    private LinearLayout mBottomLayout;
    //底部按钮
    private View mMainpageBtn;
    private View mNewsBtn;
    private View mMyBtn;
    private TextView tv_mainpage;
    private TextView tv_news;
    private TextView tv_my;
    private ImageView iv_mainpage;
    private ImageView iv_news;
    private ImageView iv_my;
    private TextView tv_back;
    private TextView tv_main_title;
    private RelativeLayout rl_title_bar;

    private MyInfoView mMyInfoView;
    private NewsView mNewsView;
    private MainpageView mMainpageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initBottonBar();
        setListener();
        setInitStatus();
    }

    private void init(){
         tv_back=findViewById(R.id.tv_back);
         tv_main_title=findViewById(R.id.tv_main_title);
         rl_title_bar=findViewById(R.id.title_bar);
         tv_back.setVisibility(View.GONE);
         //初始化中间内容容器
         mBodyLayout=findViewById(R.id.main_body);

    }

    private void initBottonBar(){
        mBottomLayout=findViewById(R.id.main_bottom_bar);
        mMainpageBtn=findViewById(R.id.bottom_bar_mainpage_btn);
        mNewsBtn=findViewById(R.id.bottom_bar_news_btn);
        mMyBtn=findViewById(R.id.bottom_bar_my_btn);
        tv_mainpage=findViewById(R.id.bottom_bar_mainpage_btn_text);
        tv_news=findViewById(R.id.bottom_bar_news_btn_text);
        tv_my=findViewById(R.id.bottom_bar_my_btn_text);
        iv_mainpage=findViewById(R.id.bottom_bar_mainpage_btn_image);
        iv_news=findViewById(R.id.bottom_bar_news_btn_image);
        iv_my=findViewById(R.id.bottom_bar_my_btn_image);

    }




    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bottom_bar_mainpage_btn:
                clearBottomImageState();
                selectDisplayView(0);
                break;
            case R.id.bottom_bar_news_btn:
                clearBottomImageState();
                selectDisplayView(1);
                break;
            case R.id.bottom_bar_my_btn:
                clearBottomImageState();
                selectDisplayView(2);
                break;
            default:
                break;
        }
    }

    /**
     * 设置底部三个按钮的点击事件
     */
    private void setListener(){
        for(int i=0;i<mBottomLayout.getChildCount();i++){
            mBottomLayout.getChildAt(i).setOnClickListener(this);

        }
    }

    /**
     * 清除底部按钮的选中状态
     *
     */
    private void clearBottomImageState(){
        tv_mainpage.setTextColor(Color.parseColor("#666666"));
        tv_news.setTextColor(Color.parseColor("#666666"));
        tv_my.setTextColor(Color.parseColor("#666666"));
        iv_mainpage.setImageResource(R.drawable.mainpage_icon);
        iv_news.setImageResource(R.drawable.news_icon);
        iv_my.setImageResource(R.drawable.my_icon);
//        for(int i=0;i<mBottomLayout.getChildCount();i++){
//            mBottomLayout.getChildAt(i).setSelected(false);
//        }
    }

    /**
     * 设置底部按钮的选中状态
     */
    private void setSelectedStatus(int index){
        switch(index){
            case 0:
//                mMainpageBtn.setSelected(true);
                iv_mainpage.setImageResource(R.drawable.mainpage_icon_selected);
                tv_mainpage.setTextColor(Color.parseColor("#0097F7"));
                rl_title_bar.setVisibility(View.VISIBLE);
                tv_main_title.setText("首页");
                break;
            case 1:
//                mNewsBtn.setSelected(true);
                iv_news.setImageResource(R.drawable.news_icon_selected);
                tv_news.setTextColor(Color.parseColor("#0097F7"));
                rl_title_bar.setVisibility(View.VISIBLE);
                tv_main_title.setText("资讯");
                break;
            case 2:
//                mMyBtn.setSelected(true);
                iv_my.setImageResource(R.drawable.my_icon_selected);
                tv_my.setTextColor(Color.parseColor("#0097F7"));
                rl_title_bar.setVisibility(View.VISIBLE);
                tv_main_title.setText("我的");
                break;
        }
    }

    /**
     * 设置界面View的初始状态
     */
    private void setInitStatus(){
        clearBottomImageState();
        setSelectedStatus(0);
        createView(0);
    }

    /**
     * 移除不需要的视图
     *
     */
    private void removeAllView(){
        for(int i=0;i<mBodyLayout.getChildCount();i++){
            mBodyLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    /**
     * 选择视图
     */
    private void createView(int viewIndex){
        switch(viewIndex){
            case 0:
                if(mMainpageView==null){
                    mMainpageView=new MainpageView(this);
                    mBodyLayout.addView(mMainpageView.getView());

                }else{
                    mMainpageView.getView();
                }
                mMainpageView.showView();
                break;

            case 1:
                if(mNewsView==null){
                    mNewsView=new NewsView(this);
                    mBodyLayout.addView(mNewsView.getView());
                }else{
                    mNewsView.getView();
                }
                mNewsView.showView();
                break;

            case 2:
                if(mMyInfoView==null){
                    mMyInfoView=new MyInfoView(this);
                    mBodyLayout.addView(mMyInfoView.getView());

                }else{
                    mMyInfoView.getView();
                }
                mMyInfoView.showView();
                break;
        }
    }

    /**
     * 显示对应的页面
     */
    private void selectDisplayView(int index){
        removeAllView(); //移除所有视图，即mBodyLayout中的三个子View
        createView(index);
        setSelectedStatus(index);
    }

    protected long exitTime; //记录第一次点击时的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else{
                MainActivity.this.finish();
//                if(readLoginStatus()){
//                    clearLoginStatus();
//                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取SharedPreferences中的登陆状态
     */
    private boolean readLoginStatus(){
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getBoolean("isLogin",false);
    }

    /**
     * 清除SharedPreferences的登陆状态
     */
    private void clearLoginStatus(){
        SharedPreferences.Editor editor=getSharedPreferences("loginInfo",MODE_PRIVATE).edit();
        editor.putBoolean("isLogin",false);
        editor.putString("loginUserName","");
        editor.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            //从设置界面或登陆界面传递过来的登陆状态
            boolean isLogin=data.getBooleanExtra("isLogin",false);
            if(isLogin){
                clearBottomImageState();
                selectDisplayView(0);

            }
            if(mMyInfoView!=null){
                mMyInfoView.setLoginParams(isLogin);
            }

        }
    }
}
