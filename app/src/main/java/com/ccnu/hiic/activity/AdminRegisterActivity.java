package com.ccnu.hiic.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccnu.hjjc.R;

public class AdminRegisterActivity extends AppCompatActivity {
    private EditText et_area_id;
    private EditText et_area_name;
    private EditText et_username;
    private EditText et_reg_code;
    private EditText et_company_name;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState){
//        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminregister);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置禁止横屏
        initView();
    }

    private void initView(){

        et_username=(EditText) findViewById(R.id.et_username);
        et_area_id=(EditText) findViewById(R.id.et_area_id);
        et_area_name=(EditText) findViewById(R.id.et_area_name);
        et_company_name=(EditText) findViewById(R.id.et_company_name);
        et_reg_code=(EditText)findViewById(R.id.et_reg_code);
        btn_next=(Button)findViewById(R.id.btn_next);

        //注册
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=et_username.getText().toString().trim();
                String area=et_area_id.getText().toString().trim();
                String area_name=et_area_name.getText().toString().trim();
                String company_name=et_company_name.getText().toString().trim();
                String code=et_reg_code.getText().toString().trim();
                if(area.isEmpty()){
                    Toast.makeText(AdminRegisterActivity.this, "请输入区域编号", Toast.LENGTH_LONG).show();
                }else if(area_name.isEmpty()){
                    Toast.makeText(AdminRegisterActivity.this, "请输入区域名称", Toast.LENGTH_LONG).show();
                }else if(area_name.length()>6){
                    Toast.makeText(AdminRegisterActivity.this, "区域名称字数不能多于6个", Toast.LENGTH_LONG).show();
                }else if(company_name.isEmpty()){
                    Toast.makeText(AdminRegisterActivity.this, "请输入单位名称", Toast.LENGTH_LONG).show();
                }else if(company_name.length()>6){
                    Toast.makeText(AdminRegisterActivity.this, "单位名称字数不能多于6个", Toast.LENGTH_LONG).show();
                }else if(username.isEmpty()){
                    Toast.makeText(AdminRegisterActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
                }else if(code.isEmpty()){
                    Toast.makeText(AdminRegisterActivity.this, "请输入注册码", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AdminRegisterActivity.this, "下一步", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(AdminRegisterActivity.this,AdminRegisterActivity2.class);
                    intent.putExtra("username",username);
                    intent.putExtra("area",area);
                    intent.putExtra("area_name",area_name);
                    intent.putExtra("company_name",company_name);
                    intent.putExtra("code",code);
                    startActivity(intent);
                }
            }
        });

    }
}


















