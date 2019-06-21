package com.ccnu.hiic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccnu.hjjc.R;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;


public class ChangeMailActivity extends AppCompatActivity{

    private EditText et_mail_old;
    private EditText et_mail_new;
    private Button btn_mail;
    private String mail_old;
    private String mail_new;
    private String user_name;

    private HttpLoader httpLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changemail);
//        System.out.println("修改邮箱");
        mail_old = getIntent().getStringExtra("mail_old");
        user_name = getIntent().getStringExtra("user_name");
        et_mail_old = (EditText)findViewById(R.id.mail_old);
        et_mail_old.setText(mail_old);
        et_mail_new = (EditText)findViewById(R.id.mail_new);
        btn_mail = (Button)findViewById(R.id.btn_mail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("修改邮箱");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        httpLoader = new HttpLoader();

        btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_new = et_mail_new.getText().toString().trim();
                if (mail_new.equals("")){
                    Toast.makeText(ChangeMailActivity.this, "邮箱不能为空！",Toast.LENGTH_LONG).show();
                }else{
                    if(!isEmail(mail_new)){
//                        Toast.makeText(getApplicationContext(), "手机号码格式有误！",Toast.LENGTH_LONG).show();
                        Toast.makeText(ChangeMailActivity.this, "邮箱格式有误！",Toast.LENGTH_LONG).show();
                    }else{
                        change_email(UserManage.getInstance().getUserInfo(ChangeMailActivity.this).getUserName(),mail_new);
                    }
                }

            }
        });
    }

    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //网络通信，修改邮箱
    public void change_email (String name, String mail){
        httpLoader.change_email(name, mail).subscribe(new Action1<String>() {
            @Override
            public void call(String flag) {
                System.out.println("服务器返回数据" +flag);
                //获取成功，数据在loginReturnObject
//                Toast.makeText(ChangeMailActivity.this, "获取数据" + flag ,
//                        Toast.LENGTH_LONG).show();
//                flag = changePhoneObject.getFlag();
                if(flag.equals("1")){
                    Toast.makeText(ChangeMailActivity.this, "修改成功",Toast.LENGTH_LONG).show();
                    finish();
                }else if(flag.equals("2")){
                    Toast.makeText(ChangeMailActivity.this, "修改失败",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //获取失败
                Log.e("TAG", "error message:" + throwable.getMessage());
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
