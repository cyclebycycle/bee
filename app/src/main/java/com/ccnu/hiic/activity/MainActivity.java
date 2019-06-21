package com.ccnu.hiic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.Bean.UserInfo;
import com.ccnu.hiic.adapter.ViewPagerFragmentAdapter;
import com.ccnu.hiic.service.NotificationCollectorService;
import com.ccnu.hiic.util.CustomViewPager;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    private RadioGroup group;
    private RadioButton rb_admin;
    private RadioButton rb_home;
    private RadioButton rb_user;
    private CustomViewPager viewPager;
    private long exitTime = 0;

    private ViewPagerFragmentAdapter fragmentAdapter;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    public String areaName;
    public  String companyName;
    public TextView areaNameTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent=getIntent();
//        areaName=intent.getStringExtra("areaname");
//        companyName=intent.getStringExtra("companyname");

        areaNameTitle=findViewById(R.id.area_name);

        fragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        bindViews();
        rb_home.setChecked(true);
        if(!isEnabled())
        {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        startService(new Intent(MainActivity.this,NotificationCollectorService.class));

//        判断是否是管理员
        if(UserManage.getInstance().getUserInfo(MainActivity.this).getMonitor()==0){
            rb_admin.setClickable(false);
            rb_admin.setEnabled(false);
            group.removeView(rb_admin);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo=UserManage.getInstance().getUserInfo(MainActivity.this);
        areaName=userInfo.getAreaName_get();
        companyName=userInfo.getCompanyName_get();
        System.out.println("地区："+areaName+" companyName:"+companyName);
        areaNameTitle.setText(companyName+" . "+areaName);
    }

    private void bindViews() {
        group = (RadioGroup) findViewById(R.id.radio_group);
        rb_admin = (RadioButton) findViewById(R.id.foot_bar_admin);
        rb_home = (RadioButton) findViewById(R.id.foot_bar_home);
        rb_user = (RadioButton) findViewById(R.id.foot_bar_user);
        group.setOnCheckedChangeListener(this);

        viewPager = (CustomViewPager) findViewById(R.id.fragment_container);
        viewPager.setScanScroll(false);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.foot_bar_admin:
                viewPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.foot_bar_home:
                viewPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.foot_bar_user:
                viewPager.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_admin.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_home.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_user.setChecked(true);
                    break;
            }
        }
    }

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

    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK){
            viewPager.setCurrentItem(PAGE_ONE);
        }
    }
}
