package com.guyue.proj1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.utils.MD5Utils;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private TextView tv_back;
    private Button btn_register;
    private EditText et_username,et_password,et_password_again;
    private String username,password,passwordAgain;
    //标题布局
    private RelativeLayout rl_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        //main_title_bar.xml
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("注 册");
        tv_back=findViewById(R.id.tv_back);
        rl_title_bar=findViewById(R.id.title_bar);

        //activity_register.xml
        btn_register=findViewById(R.id.btn_register);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        et_password_again=findViewById(R.id.et_password_again);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=et_username.getText().toString().trim();
                password=et_password.getText().toString().trim();
                passwordAgain=et_password_again.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this,"请输入用户名！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"请输入用密码！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(passwordAgain)){
                    Toast.makeText(RegisterActivity.this,"请再次输入密码！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.equals(passwordAgain)){
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一样！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(isUsernameExist(username)){
                    Toast.makeText(RegisterActivity.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                    saveRegisterInfo(username,password);
                    Intent intent=new Intent();
                    intent.putExtra("username",username);
                    setResult(RESULT_OK,intent);
                    RegisterActivity.this.finish();
                }
            }
        });
    }

    private boolean isUsernameExist(String username){
        boolean has_username=false;
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        String spUsername=sp.getString(username,"");
        if(!TextUtils.isEmpty(spUsername)){
            has_username=true;
        }
        return has_username;
    }

    private void saveRegisterInfo(String username,String password){
        String md5Password= MD5Utils.md5(password);
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(username,md5Password);
        editor.commit();
    }
}
