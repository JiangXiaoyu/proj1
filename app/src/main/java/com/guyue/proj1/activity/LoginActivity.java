package com.guyue.proj1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.utils.MD5Utils;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_main_title,tv_back,tv_register,tv_find_password;
    private EditText et_username,et_password;
    private Button btn_login;
    private String username,password,spPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init(){
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("登 录");
        tv_back=findViewById(R.id.tv_back);
        tv_register=findViewById(R.id.tv_register);
        tv_find_password=findViewById(R.id.tv_find_password);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });

        tv_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,FindPassActivity.class);
                startActivity(intent);

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=et_username.getText().toString().trim();
                password=et_password.getText().toString().trim();
                String md5Password= MD5Utils.md5(password);
                spPassword=readPssword(username);
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!TextUtils.isEmpty(spPassword) && !md5Password.equals(spPassword)){
                    Toast.makeText(LoginActivity.this,"密码输入错误",Toast.LENGTH_SHORT).show();
                    return;
                }else if(md5Password.equals(spPassword)){
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    saveLoginStatus(true,username);
                    //把登陆成功的状态传到MainActivity
                    Intent intent=new Intent();
                    intent.putExtra("isLogin",true);
                    setResult(RESULT_OK,intent);
                    LoginActivity.this.finish();
                    return;
                }else{
                    Toast.makeText(LoginActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    private String readPssword(String username){
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getString(username,"");
    }

    private void saveLoginStatus(boolean status,String username){
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("isLogin",true);
        editor.putString("loginUserName",username);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String username=data.getStringExtra("username");
            if(!TextUtils.isEmpty(username)){
                et_username.setText(username);
                et_password.setSelection(0);
            }
        }
    }
}
