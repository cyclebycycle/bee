package com.ccnu.hiic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.Bean.GetNodesInfo;
import com.ccnu.hiic.Bean.GetWeatherInfo;
import com.ccnu.hiic.Bean.Node;
import com.ccnu.hiic.Bean.UserInfo;
import com.ccnu.hiic.adapter.NodeInfoAdapter;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class ApiaryDetialActivity extends AppCompatActivity {
    View view;
    private ListView lv_nodes;
    private TextView wind_speed;
    private TextView wind_direction;
    private TextView light;
    private TextView rainfall;
    private TextView tv_title;
    private ImageView iv_add_node;
    private LinearLayout ll_weather;

    private NodeInfoAdapter nodeInfoAdapter;
    private List<Node> nodes = new ArrayList<>();
    private String username;
    private HttpLoader httpLoader;
    private HttpLoader httpLoader1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int flag = 0;

    private String Wind_speed;
    private String Wind_direction;
    private String  Light;
    private String Rainfall;
    private int FLAG;
    private String apiary_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiary_detial);
        if(UserManage.getInstance().getUserInfo(ApiaryDetialActivity.this).getMonitor()==1){
            findViewById(R.id.iv_add_node).setVisibility(View.GONE);
        }
        UserInfo userInfo=UserManage.getInstance().getUserInfo(ApiaryDetialActivity.this);
        apiary_name = getIntent().getStringExtra("floor_name");
        username= UserManage.getInstance().getUserInfo(ApiaryDetialActivity.this).getUserName();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.main_srl);
        nodeInfoAdapter=new NodeInfoAdapter(ApiaryDetialActivity.this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("进入下拉刷新");
                new LoadDataThread().start();
            }
        });
        initView();

        lv_nodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Node node=nodeInfoAdapter.getNodes(position);
                if (node == null) return;
                System.out.println("要发起的请求是"+node.getFloor_id()+";"+node.getRoom_id());

                Intent intent=new Intent(ApiaryDetialActivity.this, NodeDetialActivity.class);
                intent.putExtra("floor_id",node.getFloor_id());
                intent.putExtra("room_id",node.getRoom_id());
                intent.putExtra("username",username);

                startActivity(intent);

            }
        });
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        ll_weather=(LinearLayout)findViewById(R.id.ll_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("");
        tv_title.setText(apiary_name+"蜂场 详情");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        lv_nodes=(ListView)findViewById(R.id.lv_nodes);
        wind_speed=(TextView)findViewById(R.id.wind_speed_111);
        wind_direction=(TextView)findViewById(R.id.wind_direction);
        light=(TextView)findViewById(R.id.light);
        rainfall=(TextView)findViewById(R.id.rainfall);
        iv_add_node = (ImageView)findViewById(R.id.iv_add_node);
        iv_add_node.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(ApiaryDetialActivity.this,NodeRegistActivity.class);
               intent.putExtra("floor_name",apiary_name);
               startActivity(intent);
           }
       });
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = 0;
        NodesInfo(username,apiary_name);
        WeatherInfo(apiary_name);
    }


    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends  Thread{
        @Override
        public void run() {
            try {
                System.out.println("下拉进入新的数据加载线程");
                flag = 1;
                NodesInfo(username,apiary_name);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }

    //获取当前区域所有数据
    public void NodesInfo (String name,String floor_name){
        httpLoader = new HttpLoader();
        httpLoader.nodesInfo(name,floor_name).subscribe(new Action1<GetNodesInfo>() {
            @Override
            public void call(GetNodesInfo getNodesInfo) {
                System.out.println("返回的主要页面数据" + new Gson().toJson(getNodesInfo));
                ArrayList<Node> nodesInfo=getNodesInfo.getData();
                if(nodesInfo.size()==0){
                    Toast.makeText(ApiaryDetialActivity.this, "暂无数据",Toast.LENGTH_SHORT).show();
                }else{
                    for (int i=0;i< nodesInfo.size();i++){
                        System.out.println("详细数据" + new Gson().toJson(nodesInfo.get(i)));
                    }
                    if(nodesInfo.size()!=0){
                        nodeInfoAdapter.clear();
                        nodeInfoAdapter.addNodes(nodesInfo);
                        lv_nodes.setAdapter(nodeInfoAdapter);
                        nodeInfoAdapter.notifyDataSetChanged();
                        if (flag == 1){
                            Toast.makeText(ApiaryDetialActivity.this, "更新成功",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(flag ==1){
                            Toast.makeText(ApiaryDetialActivity.this, "暂无数据",Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                swipeRefreshLayout.setRefreshing(false);
//
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //获取失败
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ApiaryDetialActivity.this, "请求失败",Toast.LENGTH_SHORT).show();
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

    //获取当前区域气象数据
    public void WeatherInfo (String floor_name){
        httpLoader1 = new HttpLoader();
        httpLoader1.WeatherInfo(floor_name).subscribe(new Action1<GetWeatherInfo>() {
            @Override
            public void call(GetWeatherInfo getWeatherInfo) {
                System.out.println("返回的气象数据" + new Gson().toJson(getWeatherInfo));
                FLAG=getWeatherInfo.getFlag();
                if (FLAG==1){
                    Wind_speed=getWeatherInfo.getData().getWind_speed().trim();
                    Wind_direction=getWeatherInfo.getData().getWind_direction().trim();
                    Light=getWeatherInfo.getData().getLight().trim();
                    Rainfall=getWeatherInfo.getData().getRainfall().trim();
                    System.out.println("光强"+Light);
                    if(Wind_speed.equals("离线")&&Wind_direction.equals("离线")&&Light.equals("离线")&&Rainfall.equals("离线")){
                        wind_speed.setText(Wind_speed);
                        wind_direction.setText(Wind_direction);
                        light.setText(Light);
                        rainfall.setText(Rainfall);
                    }else {
                        wind_speed.setText(Wind_speed+"m/s");
                        light.setText(Light+"pp");
                        rainfall.setText(Rainfall+"mm");
                    }
                }else if (FLAG==3){
//                    AlertDialog.Builder isSure=new AlertDialog.Builder(ApiaryDetialActivity.this);
//                    //设置对话框标题
//                    isSure.setTitle("消息提醒");
//                    //设置对话框消息
//                    isSure.setMessage("该蜂场未注册气象节点！");
//                    // 添加选择按钮并注册监听
//                    isSure.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
                            ll_weather.setVisibility(View.GONE);
//                        }
//                    });
//
//                    //对话框显示
//                    isSure.show();

                }else if (FLAG==2){
                    wind_speed.setText("--m/s");
                    light.setText("--pp");
                    rainfall.setText("--mm");
                    wind_direction.setText("--");
                }


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //获取失败
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ApiaryDetialActivity.this, "请求失败",Toast.LENGTH_SHORT).show();
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
