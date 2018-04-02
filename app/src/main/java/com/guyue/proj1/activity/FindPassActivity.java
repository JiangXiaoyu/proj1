package com.guyue.proj1.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.utils.MD5Utils;

public class FindPassActivity extends AppCompatActivity {

    private TextView tv_main_title, tv_back;
    private TextView tv_reset_password;
    private EditText et_username;
    private EditText et_validate_name, et_validate_hometown, et_validate_age;
    private Button btn_validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("找回密码");
        tv_back = findViewById(R.id.tv_back);
        tv_reset_password=findViewById(R.id.tv_reset_password);
        et_username = findViewById(R.id.et_username);
        et_validate_name = findViewById(R.id.et_validate_name);
        et_validate_hometown = findViewById(R.id.et_validate_hometown);
        et_validate_age = findViewById(R.id.et_validate_age);
        btn_validate = findViewById(R.id.btn_validate);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPassActivity.this.finish();
            }
        });

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString().trim();
                String validateName = et_validate_name.getText().toString().trim();
                String validateHometown = et_validate_hometown.getText().toString().trim();
                String validateAge = et_validate_age.getText().toString().trim();
                String sp_validateName = readSecurity("validateName");
                String sp_validateHometown = readSecurity("validateHometown");
                String sp_validateAge = readSecurity("validateAge");

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(FindPassActivity.this, "请输入你的用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isExistUsername(username)) {
                    Toast.makeText(FindPassActivity.this, "你输入的用户名不存在", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(validateName) || TextUtils.isEmpty(validateHometown) || TextUtils.isEmpty(validateAge)) {
                    Toast.makeText(FindPassActivity.this, "请补全密保问题", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validateName.equals(sp_validateName)) {
                    Toast.makeText(FindPassActivity.this, "姓名输入不正确", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!validateHometown.equals(sp_validateHometown)) {
                    Toast.makeText(FindPassActivity.this, "省份输入不正确", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!validateAge.equals(sp_validateAge)) {
                    Toast.makeText(FindPassActivity.this, "年龄输入不正确", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    tv_reset_password.setVisibility(View.VISIBLE);
                    tv_reset_password.setText("已为你设置了密码：000000，为了你的账号的安全，请到设置中去重置密码。");
                    savePassword(username);

                }
            }
        });
    }

    /**
     * 从sp中读取密保问题的答案
     *
     * @param key
     * @return
     */
    private String readSecurity(String key) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }

    private boolean isExistUsername(String username) {
        boolean hasUsername = false;
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String sp_password = sp.getString(username, "");
        if (!TextUtils.isEmpty(sp_password)) {
            hasUsername = true;
        }
        return hasUsername;
    }


    private void savePassword(String username){
        String md5Password= MD5Utils.md5("000000");
        SharedPreferences.Editor editor=getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
        editor.putString(username,md5Password);
        editor.commit();
    }
}
