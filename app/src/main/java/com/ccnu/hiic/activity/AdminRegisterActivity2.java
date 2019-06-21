package com.ccnu.hiic.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.Bean.RegistReturnObject;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;

public class AdminRegisterActivity2 extends AppCompatActivity{
    private TextView t_username;

    private EditText et_pwd;
    private EditText et_pwd_again;
    private EditText et_phone;
    private EditText et_email;
    private Button reg_ok;
    private HttpLoader httpLoader;
    public int regist_get;
    private String username;
    private String area;
    private String area_name;
    private String code;
    private String company_name;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminregister2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置禁止横屏
        initView();

    }

    private void initView(){
        t_username=(TextView) findViewById(R.id.t_username);
        et_pwd=(EditText) findViewById(R.id.et_pwd);
        et_pwd_again=(EditText) findViewById(R.id.et_pwd_again);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_email=(EditText)findViewById(R.id.et_email);
        reg_ok=(Button)findViewById(R.id.reg_ok);
        Intent getIntent =getIntent();
        t_username.setText("   "+ getIntent.getStringExtra("username"));
        username=t_username.getText().toString().trim();
        area=getIntent.getStringExtra("area");
        area_name=getIntent.getStringExtra("area_name");
        company_name=getIntent.getStringExtra("company_name");
        code=getIntent.getStringExtra("code");

        //注册
        reg_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                httpLoader = new HttpLoader();
                String password=et_pwd.getText().toString().trim();
                String passwordconf=et_pwd_again.getText().toString().trim();
                String phone=et_phone.getText().toString().trim();
                String email=et_email.getText().toString().trim();
                int pwdlength=et_pwd.getText().length();

                if("".equals(password)){
                    Toast.makeText(AdminRegisterActivity2.this, "请输入密码", Toast.LENGTH_LONG).show();
                }else if(pwdlength<6){
                    Toast.makeText(AdminRegisterActivity2.this, "密码长度应为6到20之间", Toast.LENGTH_LONG).show();
                }else if(!ispsd(password)){
                    Toast.makeText(AdminRegisterActivity2.this, "密码不能全为数字或字母", Toast.LENGTH_LONG).show();
                }else if("".equals(passwordconf)){
                    Toast.makeText(AdminRegisterActivity2.this, "请确认密码", Toast.LENGTH_LONG).show();
                }else if(!password.equals(passwordconf)){
                    Toast.makeText(AdminRegisterActivity2.this, "密码输入不一致，请重新输入", Toast.LENGTH_LONG).show();
                }else if(!phone.isEmpty()&&!isMobileNO(phone)){
                    Toast.makeText(AdminRegisterActivity2.this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();

                }else if(!email.isEmpty()&&!isEmail(email)){
                    Toast.makeText(AdminRegisterActivity2.this, "请输入正确的邮箱", Toast.LENGTH_LONG).show();

                }
//               else if(!isMobileNO(phone)){
//                    Toast.makeText(AdminRegisterActivity2.this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();
//                }else if(!isEmail(email)){
//                    Toast.makeText(AdminRegisterActivity2.this, "请输入正确的邮箱", Toast.LENGTH_LONG).show();
//                }
                else{
                    adminregist(area,area_name,company_name,code,username,password,phone,email);
                }

            }
        });


    }

    public void adminregist(String area,String area_name,String company_name,String code,String name, String password,String phone,String email) {
        httpLoader.adminregist(area,area_name,company_name,code,name,password,phone,email).subscribe(new Action1<RegistReturnObject>() {
            @Override
            public void call(RegistReturnObject registReturnObject) {
                System.out.println("数据" + new Gson().toJson(registReturnObject));
                //获取成功，数据在registReturnObject
                regist_get=registReturnObject.getRegist();
                if(regist_get==1) {
                    System.out.println("注册成功");
                    Intent intent=new Intent(AdminRegisterActivity2.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(AdminRegisterActivity2.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(regist_get==2){
                    Toast.makeText(AdminRegisterActivity2.this, "注册失败—服务器操作失败", Toast.LENGTH_SHORT).show();
                }else if(regist_get==3){
                    Toast.makeText(AdminRegisterActivity2.this, "注册失败—该用户名已存在", Toast.LENGTH_SHORT).show();
                }else if(regist_get==4){
                    Toast.makeText(AdminRegisterActivity2.this, "注册失败—该区域编号已存在", Toast.LENGTH_SHORT).show();
                }else if(regist_get==5){
                    Toast.makeText(AdminRegisterActivity2.this, "非法注册—该注册码不存在", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(AdminRegisterActivity2.this, "获取数据" + registReturnObject.getRegist(),
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
    //判断手机格式是否正确

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean ispsd(String psd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(psd);
        return m.matches();
    }
//    /**
//     * 判断字符串是否只包含数字
//     * @param str
//     * @return
//     */
//    public static boolean isNumeric(String str){
//        Pattern pattern = Pattern.compile("[0-9]*");
//        return pattern.matcher(str).matches();
//    }
//    /** * 纯字母
//     * @param fstrData
//     * @return */
//    public static boolean isChar(String fstrData){
//        char c = fstrData.charAt(0);
//        if(((c>='a'&&c<='z')   ||   (c>='A'&&c<='Z')))
//        {        return   true;
//        }else{
//            return   false;
//        }
//    }
}












