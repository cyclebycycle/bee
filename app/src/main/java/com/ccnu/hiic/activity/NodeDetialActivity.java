package com.ccnu.hiic.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.Bean.GetDetialNodes;
import com.ccnu.hiic.Bean.NodeDetial;
import com.ccnu.hiic.Bean.RoomGetReturnObject;
import com.ccnu.hiic.adapter.NodeDtialAdapter;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;

public class NodeDetialActivity extends AppCompatActivity {
    private String username,floor_id,room_id;
    private  TextView tv_title;
    private HttpLoader httpLoader;
    private TextView tv_dev_eui,tv_temp,tv_humi,tv_weight,tv_time;
    private EditText temp_high,temp_min,humi_high,humi_min;
    private Switch switch_fan,switch_heater;
    private String h2sMax,nh4Max,h2sMin,nh4Min;

    private boolean flag_fan,flag_heat;
  //  private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.node_detial);
        httpLoader=new HttpLoader();
        Intent intent=getIntent();
        floor_id=intent.getStringExtra("floor_id");
        room_id=intent.getStringExtra("room_id");
        username=intent.getStringExtra("username");
        System.out.println("nodeDetial传入的具体参数是是:"+floor_id+",room_id"+room_id+",username"+username);
//        Thread t=new Thread(new MyRunnable());
//        t.start();

//        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.main_srl_detial);
        tv_title=findViewById(R.id.tv_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("");
        tv_title.setText(floor_id+"."+room_id+"蜂箱 详情");
        setSupportActionBar(toolbar);
//


//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                System.out.println("详情页面进入下拉刷新");
//                new LoadDataThread().start();
//            }
//        });

        initView();
        myDetialNodesInfo(username,floor_id,room_id);
    }

    private void initView() {
        tv_dev_eui = (TextView)findViewById(R.id.dev_eui);
        tv_temp = (TextView)findViewById(R.id.temp);
        tv_humi = (TextView)findViewById(R.id.humi);
        tv_weight = (TextView)findViewById(R.id.weight);
        tv_time = (TextView)findViewById(R.id.last_time);
        switch_fan = (Switch)findViewById(R.id.control_fan);
        switch_heater = (Switch)findViewById(R.id.control_heater);
        switch_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked=switch_fan.isChecked();
                if(isChecked){
                    flag_fan=true;
                    controlView(username,floor_id,room_id);
                }else {
                    control_close(username,floor_id,room_id,isChecked,flag_heat);
                }
            }
        });
//        switch_fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                System.out.println("风扇状态"+flag_fan);
//
//                if (isChecked){
//                    flag_fan=true;
//                    controlView(username,floor_id,room_id);
//                }else {
//                    control_close(username,floor_id,room_id,isChecked,flag_heat);
//                }
//            }
//        });
//        switch_heater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    flag_heat=true;
//                    controlView(username,floor_id,room_id);
//                }else {
//                    control_close(username,floor_id,room_id,flag_fan,isChecked);
//                }
//            }
//        });
        switch_heater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked=switch_heater.isChecked();
                if(isChecked){
                    flag_heat=true;
                    controlView(username,floor_id,room_id);
                }else {
                    control_close(username,floor_id,room_id,flag_fan,isChecked);
                }
            }
        });
    }

    public void controlView(final String username, String floorId, String roomId){

        LinearLayout layout_control=(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_control,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("当前控制阈值如下：").setView(layout_control);

        temp_high=(EditText) layout_control.findViewById(R.id.temp_max);
        temp_min = (EditText)layout_control.findViewById(R.id.temp_min);
        humi_high=(EditText) layout_control.findViewById(R.id.humi_max);
        humi_min=(EditText) layout_control.findViewById(R.id.humi_min);

        httpLoader.roomGet(username,floorId, roomId).subscribe(new Action1<RoomGetReturnObject>() {
            @Override
            public void call(RoomGetReturnObject roomgetReturnObject) {
                System.out.println("数据是什么" + new Gson().toJson(roomgetReturnObject));
                int flag=roomgetReturnObject.getFlag();
                if(flag==0){
                    Toast.makeText(getApplicationContext(), "该蜂箱不存在",Toast.LENGTH_SHORT).show();
                    System.out.println("获取数据信息5 flag==0");
                }else if(flag==1){
//                    Toast.makeText(getApplicationContext(), "该房间号存在",Toast.LENGTH_SHORT).show();
                    System.out.println("获取数据信息6 flag==1"+roomgetReturnObject.getData().getTem_low_threshold());
                    RoomGetReturnObject.DataBean dataBean=roomgetReturnObject.getData();
                    temp_min.setText(roomgetReturnObject.getData().getNh4_min_threshold()+"");
                    temp_high.setText(roomgetReturnObject.getData().getNh4_high_threshold()+"");
                    humi_min.setText(roomgetReturnObject.getData().getH2s_min_threshold()+"");
                    humi_high.setText(roomgetReturnObject.getData().getH2s_high_threshold()+"");
                } else if(flag==2){
                    Toast.makeText(getApplicationContext(), "配置失败",Toast.LENGTH_SHORT).show();
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

        builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                h2sMax=humi_high.getText().toString();
                nh4Max=temp_high.getText().toString();
                h2sMin=humi_min.getText().toString();
                nh4Min=temp_min.getText().toString();
                if(h2sMax.equals("")||nh4Max.equals("")||h2sMin.equals("")||nh4Min.equals("")){
                    Toast.makeText(getApplicationContext(), "输入不能有空，请检查！",Toast.LENGTH_LONG).show();
                }else if( Integer.parseInt(h2sMax)< Integer.parseInt(h2sMin) || Integer.parseInt(nh4Max)< Integer.parseInt(nh4Min) ){
                    Toast.makeText(getApplicationContext(), "最大值不能小于最小值！",Toast.LENGTH_LONG).show();
                }else {
                    control_open(username, floor_id, room_id, flag_fan, flag_heat, h2sMax, nh4Max, h2sMin, nh4Min);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                control_open(username,floor_id,room_id,flag_fan,flag_heat,h2sMax,nh4Max,h2sMin,nh4Min);
                dialog.cancel();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
        builder.setCancelable(false);


    }


//    public class MyRunnable implements Runnable{
//        public void run(){
//            System.out.println("节点详细信息的run函数");
//        }
//    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends  Thread{
        @Override
        public void run() {
            try {
                System.out.println("下拉进入新的数据加载线程");
                myDetialNodesInfo(username,floor_id,room_id);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }

    //控制开启
    public void control_open (String username, String floor_name, String room_id, boolean fan, boolean heat,
                              String h2sMax, String nh4Max,String h2sMin, String nh4Min) {

        httpLoader.control_open(username,  floor_name,  room_id,
                fan,  heat, h2sMax, nh4Max, h2sMin, nh4Min).subscribe(new Action1<String>() {
            @Override
            public void call(String flags) {
                System.out.println("控制开启返回"+flags);
                if(flags.equals("1")){
                    Toast.makeText(NodeDetialActivity.this,"配置成功",Toast.LENGTH_SHORT).show();
                }else if(flags.equals("2")){
                    Toast.makeText(NodeDetialActivity.this,"配置失败",Toast.LENGTH_SHORT).show();
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

    //控制开启
    public void control_close (String username, String floor_name, String room_id, boolean fan, boolean heat) {

        httpLoader.control_close(username,  floor_name,  room_id, fan,  heat).subscribe(new Action1<String>() {
            @Override
            public void call(String flags) {
                System.out.println("返回"+flags);
                if(flags.equals("1")){
                    Toast.makeText(NodeDetialActivity.this,"配置成功",Toast.LENGTH_SHORT).show();
                }else if(flags.equals("2")){
                    Toast.makeText(NodeDetialActivity.this,"配置失败",Toast.LENGTH_SHORT).show();
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

    //获取当前区域所有数据
    public void myDetialNodesInfo (String name,String floorId ,String roomId){
        httpLoader.detailNodesInfo(name,floorId,roomId).subscribe(new Action1<GetDetialNodes>() {
            @Override
            public void call(GetDetialNodes getDetialNodes) {
                System.out.println("节点详细信息返回的数据" + new Gson().toJson(getDetialNodes));
//                ArrayList<NodeDetial>detialNodes=getDetialNodes.getData();
//
//                for (int i=0;i< detialNodes.size();i++){
//                    System.out.println("详细数据" + new Gson().toJson(detialNodes.get(i)));
//                }
//                nodeInfoAdapter.clear();

                tv_dev_eui.setText(getDetialNodes.getDev_eui());
                tv_humi.setText(getDetialNodes.getHumi_value());
                tv_temp.setText(getDetialNodes.getTemp_value());
                tv_weight.setText(getDetialNodes.getWeight());
                tv_time.setText(getDetialNodes.getDatetime());
                if (getDetialNodes.getFan().equals("t")){
                    switch_fan.setChecked(true);
                    flag_fan=true;
                }else if(getDetialNodes.getFan().equals("f")){
                    switch_fan.setChecked(false);
                    flag_fan=false;


                }
                if (getDetialNodes.getHeat().equals("t")){
                    switch_heater.setChecked(true);
                    flag_heat=true;
                }else if(getDetialNodes.getHeat().equals("f")){
                    switch_heater.setChecked(false);
                    flag_heat=false;

                }
//                swipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(NodeDetialActivity.this, "更新成功",Toast.LENGTH_LONG).show();
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
