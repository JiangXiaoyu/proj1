package com.guyue.proj1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout rl_title_bar;
    private RelativeLayout rl_modify_password,rl_security_setting,rl_exit_login;

    public static SettingActivity instance=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        instance=this;
        init();

    }

    private void init(){
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置");
        tv_back=findViewById(R.id.tv_back);
        rl_title_bar=findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        rl_modify_password=findViewById(R.id.rl_modify_password);
        rl_security_setting=findViewById(R.id.rl_security_setting);
        rl_exit_login=findViewById(R.id.rl_exit_login);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });

        rl_modify_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });

        rl_security_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,SetPassProtectActivity.class);
                startActivity(intent);
            }
        });

        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this,"退出登陆成功",Toast.LENGTH_SHORT).show();
                clearLoginStatus();
                Intent data=new Intent();
                data.putExtra("isLogin",false);
                setResult(RESULT_OK,data);
                SettingActivity.this.finish();
            }
        });
    }

    /**
     * 清除SharedPreferences中登陆状态和登陆时的用户名
     */

    public void clearLoginStatus(){
        SharedPreferences.Editor editor=getSharedPreferences("loginInfo",MODE_PRIVATE).edit();
        editor.putBoolean("isLogin",false);
        editor.putString("loginUserName","");
        editor.commit();

    }
}
