package com.ccnu.hiic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;

import rx.functions.Action1;

public class AddApiaryActivity extends AppCompatActivity {

    private EditText et_apiary_name;
    //    private RadioGroup monitor;
    private Button addApiary;
    private HttpLoader httpLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apiary);
        initView();
    }

    public void initView() {
        et_apiary_name = findViewById(R.id.et_apiaryname);
//        monitor = findViewById(R.id.rg_monitor);
        addApiary = findViewById(R.id.bt_add_apiary);
        addApiary.setEnabled(false);
        addApiary.setClickable(false);
        httpLoader = new HttpLoader();
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("添加蜂场");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_apiary_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!et_apiary_name.getText().toString().isEmpty()) {
                    addApiary.setEnabled(true);
                    addApiary.setClickable(true);
//                    addClient.setBackgroundColor(getResources().getColor(R.color.btn_basic_degree_bg));
                }
            }
        });
//        monitor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.rb_manager) {
//                    monitorValue = 1;
//                } else if (i == R.id.rb_client) {
//                    monitorValue = 0;
//                }
//            }
//        });
        addApiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiaryName = et_apiary_name.getText().toString().trim();
                if (!apiaryName.isEmpty()) {
                    System.out.println("新增蜂场名"+apiaryName);
                    String username=UserManage.getInstance().getUserInfo(AddApiaryActivity.this).getUserName();
                    addApiary(username, apiaryName);
                } else {
                    Toast.makeText(AddApiaryActivity.this, "蜂场名称不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addApiary(String username, String floor_name) {
        System.out.println("用户名AddAPi："+username+"floor_name："+floor_name);
        httpLoader.addApiary(username,floor_name).subscribe(new Action1<String>() {
           @Override
           public void call(String s) {
               System.out.println("返回数据"+s);
               if (s.equals("0")) {
                   Toast.makeText(AddApiaryActivity.this, "该用户名已存在", Toast.LENGTH_LONG).show();
               } else if (s.equals("1")) {
                   Toast.makeText(AddApiaryActivity.this, "新增成功", Toast.LENGTH_LONG).show();
                   et_apiary_name.setText("");
               } else if (s.equals("2")) {
                   Toast.makeText(AddApiaryActivity.this, "新增失败", Toast.LENGTH_LONG).show();
               }
           }
       },new Action1<Throwable>() {
           @Override
           public void call(Throwable throwable) {
               //获取失败
               Log.e("TAG", "error message:" + throwable.getMessage());
               System.out.println("异常信息" + throwable.getMessage());
               Toast.makeText(AddApiaryActivity.this, "请求失败", Toast.LENGTH_LONG).show();
               if (throwable instanceof Fault) {
                   Fault fault = (Fault) throwable;
                   if (fault.getErrorCode() == 404) {
                       //错误处理
                   } else if (fault.getErrorCode() == 500) {
                       //错误处理
                   } else if (fault.getErrorCode() == 501) {
                       //错误处理
                   }
               }
           }
       });


    }



}
