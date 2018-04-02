package com.guyue.proj1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.guyue.proj1.utils.AnalysisUtils;
import com.guyue.proj1.utils.MD5Utils;

public class ModifyPasswordActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private TextView tv_back;
    private EditText et_original_password,et_new_password,et_new_password_again;
    private Button btn_save;
    private String originalPassword,newPassword,newPasswordAgain;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        init();
        username= AnalysisUtils.readLoginUserName(this);

    }

    private void init(){
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_back=findViewById(R.id.tv_back);
        et_original_password=findViewById(R.id.et_original_password);
        et_new_password=findViewById(R.id.et_new_password);
        et_new_password_again=findViewById(R.id.et_new_password_again);
        btn_save=findViewById(R.id.btn_save);

        tv_main_title.setText("修改密码");
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPasswordActivity.this.finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalPassword=et_original_password.getText().toString().trim();
                newPassword=et_new_password.getText().toString().trim();
                newPasswordAgain=et_new_password_again.getText().toString().trim();

                if(TextUtils.isEmpty(originalPassword)){
                    Toast.makeText(ModifyPasswordActivity.this,"请输入原始密码",Toast.LENGTH_SHORT).show();
                    return;

                }else if(TextUtils.isEmpty(newPassword)){
                    Toast.makeText(ModifyPasswordActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(newPasswordAgain)){
                    Toast.makeText(ModifyPasswordActivity.this,"请再次输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!newPassword.equals(newPasswordAgain)){
                    Toast.makeText(ModifyPasswordActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!MD5Utils.md5(originalPassword).equals(readPassword())){
                    Toast.makeText(ModifyPasswordActivity.this,"原始密码输入不正确",Toast.LENGTH_SHORT).show();
                    return;
                }else if(MD5Utils.md5(newPassword).equals(readPassword())){
                    Toast.makeText(ModifyPasswordActivity.this,"输入的新密码和原始密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(ModifyPasswordActivity.this,"新密码设置成功",Toast.LENGTH_SHORT).show();
                    modifyPassword(newPassword);
                    Intent intent=new Intent(ModifyPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    //退出登陆操作
                    SettingActivity.instance.clearLoginStatus();
                    Intent data=new Intent();
                    data.putExtra("isLogin",false);
                    SettingActivity.instance.setResult(RESULT_OK,data);
                    SettingActivity.instance.finish();
                    ModifyPasswordActivity.this.finish();

                }
            }
        });
    }

    /**
     * 修改登陆成功时保存在SharedPreferences中的密码
     *
     */
    private void modifyPassword(String newPassword){
        String md5Password= MD5Utils.md5(newPassword);
        SharedPreferences.Editor editor=getSharedPreferences("loginInfo",MODE_PRIVATE).edit();
        editor.putString(username,md5Password);
        editor.commit();
    }

    /**
     * 从SharedPreferences中读取原始密码
     */
    private String readPassword(){
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getString(username,"");
    }
}
