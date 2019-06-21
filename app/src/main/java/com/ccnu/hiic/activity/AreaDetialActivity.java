package com.ccnu.hiic.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.Bean.ApiaryDetail;
import com.ccnu.hiic.Bean.NodeDetial;
import com.ccnu.hiic.Bean.UserInfo;
import com.ccnu.hiic.Bean.GetApiaryDetail;
import com.ccnu.hiic.adapter.ApiaryDetailAdapter;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.service.NotificationCollectorService;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class AreaDetialActivity extends AppCompatActivity {

    View view;
    private ListView lv_apiary;
    private ApiaryDetailAdapter apiaryDetailAdapter;
    private String username;
    private TextView tv_top;
    private TextView tv_title;
    private HttpLoader httpLoader;
    public String areaName;
    public  String companyName;
    private List<ApiaryDetail> apiaryDetails = new ArrayList<>();
    private ImageView iv_add_apiary;
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_detial);

        if(UserManage.getInstance().getUserInfo(AreaDetialActivity.this).getMonitor()==1){
            findViewById(R.id.iv_add_apiary).setVisibility(View.GONE);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置禁止横屏
        UserInfo userInfo=UserManage.getInstance().getUserInfo(AreaDetialActivity.this);
        areaName=userInfo.getAreaName_get();
        companyName=userInfo.getCompanyName_get();
        username=userInfo.getUserName();
        initView();
        httpLoader=new HttpLoader();
//        showAllApiary (UserManage.getInstance().getUserInfo(AreaDetialActivity.this).getUserName());
        showAllApiary (username);
        lv_apiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApiaryDetail apiaryDetail =apiaryDetailAdapter.getApiary(position);
                if (apiaryDetail == null) return;
                System.out.println("要发起的请求是"+ apiaryDetail +";");

                Intent intent=new Intent(AreaDetialActivity.this, ApiaryDetialActivity.class);
                intent.putExtra("floor_name", apiaryDetail.getFloor_name());
                startActivity(intent);

            }
        });

    }

    private void initView() {
        lv_apiary=(ListView) findViewById(R.id.lv_apiary);
        tv_top = (TextView)findViewById(R.id.tv_topinfo);
        tv_title = (TextView)findViewById(R.id.tv_title);
        iv_add_apiary=(ImageView) findViewById(R.id.iv_add_apiary);
        apiaryDetailAdapter = new ApiaryDetailAdapter(AreaDetialActivity.this);
        tv_top.setText(username+",欢迎您进入蜂箱管理系统,该区域蜂场信息如下：");
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("");
        tv_title.setText(areaName+" 区域");
        setSupportActionBar(toolbar);
        iv_add_apiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AreaDetialActivity.this,AddApiaryActivity.class);
                startActivity(intent);
            }
        });
        if(!isEnabled())
        {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        startService(new Intent(AreaDetialActivity.this,NotificationCollectorService.class));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        //        判断是否是管理员
        if(UserManage.getInstance().getUserInfo(AreaDetialActivity.this).getMonitor()==0){
            menu.findItem(R.id.action_get_client).setVisible(false);
        }else if(UserManage.getInstance().getUserInfo(AreaDetialActivity.this).getMonitor()==1){
            menu.findItem(R.id.action_mess_config).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder isLogout=new AlertDialog.Builder(AreaDetialActivity.this);
                //设置对话框标题
                isLogout.setTitle("注销账号");
                //设置对话框消息
                isLogout.setMessage("注销后需要重新登录，你确定要注销吗？");
                // 添加选择按钮并注册监听
                isLogout.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserManage.getInstance().clear(AreaDetialActivity.this);
                        System.out.println("是否清除1"+UserManage.getInstance().hasUserInfo(AreaDetialActivity.this));
                        Intent intent = new Intent(AreaDetialActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                });
                isLogout.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                //对话框显示
                isLogout.show();
                break;

            case R.id.action_mess_config:
                Intent intent2 = new Intent(AreaDetialActivity.this,MessConfigActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_get_client:
                Intent intent3 = new Intent(AreaDetialActivity.this,AdminGetClientActivity.class);
                startActivity(intent3);
                break;
            case R.id.action_settings:
                Intent intent4 = new Intent(AreaDetialActivity.this,SettingActivity.class);
//                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);
                break;

        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String tag = intent.getStringExtra("EXIT_TAG");
        if (tag != null&& !TextUtils.isEmpty(tag)) {
            if ("SINGLETASK".equals(tag)) {//退出程序
                finish();
            }
        }
    }

    public void showAllApiary (String username){
        httpLoader.showAllApiary(username).subscribe(new Action1<GetApiaryDetail>() {
            @Override
            public void call(GetApiaryDetail getApiaryDetail) {
                System.out.println("数据Admin" + new Gson().toJson(getApiaryDetail));
                ArrayList<ApiaryDetail> apiaryDetail =getApiaryDetail.getFloor();
                for (int i = 0; i< apiaryDetail.size(); i++){
                    System.out.println("详细数据" + new Gson().toJson(apiaryDetail.get(i)));
                }
//                nodeInfoAdapter.clear();
                apiaryDetailAdapter.add(apiaryDetail);
                lv_apiary.setAdapter(apiaryDetailAdapter);
                apiaryDetailAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(AreaDetialActivity.this, "更新成功",Toast.LENGTH_LONG).show();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("TAG", "error message:" + throwable.getMessage());
                System.out.println("异常Admin"+throwable.getMessage());
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

}
