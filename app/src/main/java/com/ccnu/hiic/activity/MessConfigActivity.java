package com.ccnu.hiic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.ccnu.hiic.Bean.RoomGetReturnObject;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import rx.functions.Action1;

public class MessConfigActivity extends AppCompatActivity {

    private EditText room_id;
    private EditText floor_id;
    private EditText temp_min;
    private EditText temp_max;
    private EditText humi_min;
    private EditText humi_max;
    private EditText temp2_min;
    private EditText temp2_max;
    private EditText humi2_min;
    private EditText humi2_max;
    private Button bt_config_it;
    private Button bt_config_all;
    private String roomid;
    private String floorid;
    private HttpLoader httpLoader;
    private String userName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_config);
        httpLoader=new HttpLoader();
        userName= UserManage.getInstance().getUserInfo(MessConfigActivity.this).getUserName();
        initView();
    }
    public void initView(){
        floor_id=(EditText)findViewById(R.id.floor_id_1);
        room_id=(EditText)findViewById(R.id.room_id_1);
        temp_min=findViewById(R.id.temp_min_1);
        temp_max=findViewById(R.id.temp_max_1);
        humi_min=findViewById(R.id.humi_min_1);
        humi_max=findViewById(R.id.humi_max_1);
        temp2_min=findViewById(R.id.temp_min_2);
        temp2_max=findViewById(R.id.temp_max_2);
        humi2_min=findViewById(R.id.humi_min_2);
        humi2_max=findViewById(R.id.humi_max_2);
        bt_config_all=findViewById(R.id.bt_config_all);
        bt_config_it=findViewById(R.id.bt_config_it);


        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("信息配置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        room_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("获取数据信息1");
                if(!floor_id.getText().toString().trim().isEmpty() && !room_id.getText().toString().trim().isEmpty()){
                    System.out.println("获取数据信息11");
                    roomGet (userName,floor_id.getText().toString().trim(), room_id.getText().toString().trim());
                }
            }
        });
        floor_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("获取数据信息2");
                if(!floor_id.getText().toString().trim().isEmpty() && !room_id.getText().toString().trim().isEmpty()){
                    System.out.println("获取数据信息22");
                    roomGet (userName,floor_id.getText().toString().trim(), room_id.getText().toString().trim());
                }
            }
        });

        bt_config_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOne();
            }
        });

        bt_config_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAll();
            }
        });
    }

    public void roomGet (String username,String floorId, String roomId) {
        System.out.println("获取数据信息3");
        httpLoader.roomGet(username,floorId, roomId).subscribe(new Action1<RoomGetReturnObject>() {
            @Override
            public void call(RoomGetReturnObject roomgetReturnObject) {
                System.out.println("数据是什么" + new Gson().toJson(roomgetReturnObject));
                int flag=roomgetReturnObject.getFlag();
                if(flag==0){
                    Toast.makeText(getApplicationContext(), "该蜂箱不存在",Toast.LENGTH_LONG).show();
                    System.out.println("获取数据信息5 flag==0");
                }else if(flag==1){
//                    Toast.makeText(getApplicationContext(), "该房间号存在",Toast.LENGTH_LONG).show();
                    System.out.println("获取数据信息6 flag==1"+roomgetReturnObject.getData().getTem_low_threshold());
                    RoomGetReturnObject.DataBean dataBean=roomgetReturnObject.getData();
                    temp_min.setText(roomgetReturnObject.getData().getTem_low_threshold()+"");
                    temp_max.setText(roomgetReturnObject.getData().getTem_high_threshold()+"");
                    humi_min.setText(roomgetReturnObject.getData().getHum_low_threshold()+"");
                    humi_max.setText(roomgetReturnObject.getData().getHum_high_threshold()+"");
                    temp2_min.setText(roomgetReturnObject.getData().getNh4_min_threshold()+"");
                    temp2_max.setText(roomgetReturnObject.getData().getNh4_high_threshold()+"");
                    humi2_min.setText(roomgetReturnObject.getData().getH2s_min_threshold()+"");
                    humi2_max.setText(roomgetReturnObject.getData().getH2s_high_threshold()+"");
                } else if(flag==2){
                    Toast.makeText(getApplicationContext(), "配置失败",Toast.LENGTH_LONG).show();
                    System.out.println("获取数据信息7 flag==2");
//                    temp_min.setText(roomgetReturnObject.getData().getTem_low_threshold());
//                    temp_max.setText(roomgetReturnObject.getData().getTem_high_threshold());
//                    humi_min.setText(roomgetReturnObject.getData().getHum_low_threshold());
//                    humi_max.setText(roomgetReturnObject.getData().getHum_high_threshold());
                    System.out.println("获取数据信息4");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("获取数据信息8异常处理787");
                //获取失败
                Log.e("TAG", "异常处理787error message:" + throwable.getMessage());
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

    //配置一个房间节点
    private void addOne(){
        String floorId=floor_id.getText().toString().trim();
        String roomId=room_id.getText().toString().trim();
        String tempMin=temp_min.getText().toString().trim();
        String tempMax=temp_max.getText().toString().trim();
        String humiMin=humi_min.getText().toString().trim();
        String humiMax=humi_max.getText().toString().trim();
        String tempMin2=temp2_min.getText().toString().trim();
        String tempMax2=temp2_max.getText().toString().trim();
        String humiMin2=humi2_min.getText().toString().trim();
        String humiMax2=humi2_max.getText().toString().trim();
        if(floorId.equals("")|| roomId.equals("") ||
                tempMin.equals("")||tempMax.equals("")||humiMin.equals("")||humiMax.equals("")||
                tempMin2.equals("")||tempMax2.equals("")||humiMin2.equals("")||humiMax2.equals("")){
            Toast.makeText(getApplicationContext(), "输入不能有空，请检查！",Toast.LENGTH_LONG).show();
        }else if( Integer.parseInt(tempMax)< Integer.parseInt(tempMin) || Integer.parseInt(humiMax)< Integer.parseInt(humiMin) ||
                Integer.parseInt(tempMax2)< Integer.parseInt(tempMin2) || Integer.parseInt(humiMax2)< Integer.parseInt(humiMin2)){
            Toast.makeText(getApplicationContext(), "最大值不能小于最小值！",Toast.LENGTH_LONG).show();
        }else{
            thesholdOne (userName, floorId, roomId, Integer.parseInt(humiMax), Integer.parseInt(humiMin), Integer.parseInt(tempMin), Integer.parseInt(tempMax),
                    Integer.parseInt(humiMax2), Integer.parseInt(tempMax2), Integer.parseInt(humiMin2), Integer.parseInt(tempMin2));
        }
    }

    //网络请求，配置一个房间
    public void thesholdOne (String username, String floor_id, String room_id, int humiMax, int humiMin, int tempMin,
                             int tempMax, int h2sMax, int nh4Max,int h2sMin, int nh4Min) {
        httpLoader.theshold(username, floor_id, room_id,humiMax, humiMin, tempMin, tempMax, h2sMax, nh4Max, h2sMin, nh4Min).subscribe(new Action1<String>() {
            @Override
            public void call(String flags) {
                if(flags.equals("0")){
                    Toast.makeText(MessConfigActivity.this,"未注册信息",Toast.LENGTH_LONG).show();
                } else if(flags.equals("1")){
                    Toast.makeText(MessConfigActivity.this,"配置成功",Toast.LENGTH_LONG).show();
                }else if(flags.equals("2")){
                    Toast.makeText(MessConfigActivity.this,"配置失败",Toast.LENGTH_LONG).show();
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


    //配置所有房间节点
    private void addAll(){
        String floorId=floor_id.getText().toString().trim();
        String tempMin=temp_min.getText().toString().trim();
        String tempMax=temp_max.getText().toString().trim();
        String humiMin=humi_min.getText().toString().trim();
        String humiMax=humi_max.getText().toString().trim();
        String tempMin2=temp2_min.getText().toString().trim();
        String tempMax2=temp2_max.getText().toString().trim();
        String humiMin2=humi2_min.getText().toString().trim();
        String humiMax2=humi2_max.getText().toString().trim();
        if(floorId.equals("")|| tempMin.equals("")||tempMax.equals("")||humiMin.equals("")||humiMax.equals("")||
                tempMin2.equals("")||tempMax2.equals("")||humiMin2.equals("")||humiMax2.equals("")){
            Toast.makeText(getApplicationContext(), "输入不能有空，请检查！",Toast.LENGTH_LONG).show();
        }else if( Integer.parseInt(tempMax)< Integer.parseInt(tempMin) || Integer.parseInt(humiMax)< Integer.parseInt(humiMin)||
                Integer.parseInt(tempMax2)< Integer.parseInt(tempMin2) || Integer.parseInt(humiMax2)< Integer.parseInt(humiMin2)){
            Toast.makeText(getApplicationContext(), "最大值不能小于最小值！",Toast.LENGTH_LONG).show();
        }else{
//            System.out.println("floor_id"+floorId);
            thesholdAll (userName,floorId, Integer.parseInt(humiMax), Integer.parseInt(humiMin), Integer.parseInt(tempMin), Integer.parseInt(tempMax),
                    Integer.parseInt(humiMax2), Integer.parseInt(tempMax2), Integer.parseInt(humiMin2), Integer.parseInt(tempMin2));
        }
    }

    //网络请求，配置所有房间
    public void thesholdAll (String username,String floor_id, int humiMax, int humiMin, int tempMin, int tempMax,
                             int h2sMax, int nh4Max,int h2sMin, int nh4Min) {

        httpLoader.thesholdall(username,floor_id,humiMax,humiMin,tempMin,tempMax,h2sMax,nh4Max,h2sMin,nh4Min).subscribe(new Action1<String>() {
            @Override
            public void call(String flags) {
                System.out.println("返回"+flags);
                if(flags.equals("0")){
                    Toast.makeText(MessConfigActivity.this,"未注册信息",Toast.LENGTH_LONG).show();
                } else if(flags.equals("1")){
                    Toast.makeText(MessConfigActivity.this,"配置成功",Toast.LENGTH_LONG).show();
                }else if(flags.equals("2")){
                    Toast.makeText(MessConfigActivity.this,"配置失败",Toast.LENGTH_LONG).show();
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
