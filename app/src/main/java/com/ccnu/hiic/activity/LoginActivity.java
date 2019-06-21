package com.ccnu.hiic.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;


import com.ccnu.hiic.Bean.LoginReturnObject;
import com.ccnu.hiic.Bean.UserInfo;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import rx.functions.Action1;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username, et_pwd;
    private Button btn_login;
    private TextView tv_admin_register, tv_register;
    private HttpLoader httpLoader;
    public int login_get;
    public int monitor_get;
    public String areaName_get;
    public String companyName_get;
    public String username;
    public String password;
    private long exitTime = 0;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置禁止横屏
        initView();
    }

    private void initView() {
        et_username = (EditText) this.findViewById(R.id.et_username);
        et_pwd = (EditText) this.findViewById(R.id.et_pwd);
        tv_admin_register = (TextView) this.findViewById(R.id.tv_admin_register);
        tv_register = (TextView) this.findViewById(R.id.tv_register);
        btn_login = (Button) this.findViewById(R.id.btn_login);
        UserInfo userInfo= UserManage.getInstance().getUserInfo(LoginActivity.this);
        et_username.setText(userInfo.getUserName());
        et_pwd.setText(userInfo.getPassword());
        //有用户和密码
        if(UserManage.getInstance().hasUserInfo(LoginActivity.this)){
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            Intent intent = new Intent(LoginActivity.this,AreaDetialActivity.class);
            startActivity(intent);
            finish();
        }


        //登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpLoader = new HttpLoader();
                username=et_username.getText().toString().trim();
                password=et_pwd.getText().toString().trim();
                if (username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
                }else if ("".equals(password)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                }else{
                    System.out.println("用户名："+username);
                    System.out.println("密码："+password);
                    login(username,password);
                }
            }
        });

        tv_admin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdminRegisterActivity.class);
                startActivity(intent);

            }
        });

        //普通用户注册
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }




    //网络通信，登陆
    public void login(String name, final String password) {
        httpLoader.login(name, password).subscribe(new Action1<LoginReturnObject>() {
            @Override
            public void call(LoginReturnObject loginReturnObject) {
                System.out.println("数据" + new Gson().toJson(loginReturnObject));
                login_get=loginReturnObject.getLogin();
                monitor_get=loginReturnObject.getMonitor();
                areaName_get=loginReturnObject.getArea_name();
                companyName_get=loginReturnObject.getCompany_name();
                if(login_get==0) {
                    Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                }else if(login_get==1){
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }else if(login_get==2){
                    System.out.println("登录成功");
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    UserManage.getInstance().saveUserInfo(LoginActivity.this, username, password,monitor_get,areaName_get,companyName_get);

//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    Intent intent = new Intent(LoginActivity.this,AreaDetialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("areaname", areaName_get);
                    bundle.putString("companyname", companyName_get);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
//                Toast.makeText(LoginActivity.this, "获取数据" + loginReturnObject.getLogin() + "&" + loginReturnObject.getMonitor(),
//                        Toast.LENGTH_LONG).show();
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

    /**
     * 返回键
     * @param keyCode
     * @param event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 连续点击两次返回键退出应用
     */
    private void exit(){
        if((System.currentTimeMillis() - exitTime)>2000){
            Log.e("再按一次退出程序","app");
            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
            Log.e("exitTime","app");
        }else {
            finish();
            Log.e("退出","app");
            System.exit(0);
        }
    }
}