package com.guyue.proj1.activity;

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

public class SetPassProtectActivity extends AppCompatActivity {

    private TextView tv_main_title,tv_back;
    private EditText et_validate_name,et_validate_hometown,et_validate_age;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass_protect);

        init();

    }

    private void init(){
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置密保");
        tv_back=findViewById(R.id.tv_back);
        et_validate_name=findViewById(R.id.et_validate_name);
        et_validate_hometown=findViewById(R.id.et_validate_hometown);
        et_validate_age=findViewById(R.id.et_validate_age);
        btn_save=findViewById(R.id.btn_save);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetPassProtectActivity.this.finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validateName=et_validate_name.getText().toString().trim();
                String validateHometown=et_validate_hometown.getText().toString().trim();
                String validateAge=et_validate_age.getText().toString().trim();
                if(TextUtils.isEmpty(validateName) || TextUtils.isEmpty(validateHometown) || TextUtils.isEmpty(validateAge)){
                    Toast.makeText(SetPassProtectActivity.this,"请补全密保问题",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(SetPassProtectActivity.this,"密保问题设置成功",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=getSharedPreferences("loginInfo",MODE_PRIVATE).edit();
                    editor.putString("validateName",validateName);
                    editor.putString("validateHometown",validateHometown);
                    editor.putString("validateAge",validateAge);
                    editor.commit();
                    SetPassProtectActivity.this.finish();

                }
            }
        });
    }
}
