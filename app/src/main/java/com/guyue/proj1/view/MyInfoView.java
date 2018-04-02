package com.guyue.proj1.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.activity.LoginActivity;
import com.guyue.proj1.activity.MainActivity;
import com.guyue.proj1.activity.SettingActivity;
import com.guyue.proj1.utils.AnalysisUtils;

/**
 * Created by huyun on 2018/3/19.
 */

public class MyInfoView {

    public ImageView iv_head_icon;
    private LinearLayout ll_head;
    private RelativeLayout rl_order_history, rl_setting;
    private TextView tv_username;
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;

    public MyInfoView(Activity context) {
        mContext = context;
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);

    }

    private void createView() {
        initView();
    }

    /**
     * 获取界面控件
     *
     * @return
     */
    private void initView() {
        //设置布局文件
        mCurrentView = mInflater.inflate(R.layout.main_view_my, null);

        ll_head = mCurrentView.findViewById(R.id.ll_head);
        iv_head_icon = mCurrentView.findViewById(R.id.iv_head_icon);
        tv_username = mCurrentView.findViewById(R.id.tv_username);
        rl_order_history = mCurrentView.findViewById(R.id.rl_order_history);
        rl_setting = mCurrentView.findViewById(R.id.rl_setting);

        mCurrentView.setVisibility(View.VISIBLE);
        setLoginParams(readLoginStatus());// 设置登陆时界面控件的状态

        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否已经登陆
                if(readLoginStatus()){
                    Toast.makeText(mContext,"你已经登陆",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //未登录
                    Intent intent=new Intent(mContext,LoginActivity.class);
                    mContext.startActivityForResult(intent,1);
                }

            }
        });

        rl_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(readLoginStatus()){
                    //

                }else{
                    Toast.makeText(mContext,"你还未登陆,请先登陆",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext,LoginActivity.class);
                    mContext.startActivityForResult(intent,1);
                }
            }
        });

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(readLoginStatus()){
                    Intent intent=new Intent(mContext, SettingActivity.class);
                    mContext.startActivityForResult(intent,1);
                }else{
                    Toast.makeText(mContext, "你还未登陆,请先登陆", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext,LoginActivity.class);
                    mContext.startActivityForResult(intent,1);
                }
            }
        });
    }


    /**
     * 登陆成功后设置我的界面
     * @param isLogin
     */
    public void setLoginParams(boolean isLogin) {
        if (isLogin) {
            tv_username.setText(AnalysisUtils.readLoginUserName(mContext));
        } else {
            tv_username.setText("点击登陆");
        }
    }

    /**
     * 获取当前在导航栏上方显示对应的View
     */
    public View getView(){
        if(mCurrentView==null){
            createView();
        }

        return mCurrentView;
    }

    /**
     * 显示当前导航栏上方所对应的View界面
     */
    public void showView(){
        if(mCurrentView==null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }

    /**
     * 从SharedPreferences中获取登陆状态
     *
     * @return
     */
    private boolean readLoginStatus() {
        SharedPreferences sp = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);

        return sp.getBoolean("isLogin", false);
    }
}
