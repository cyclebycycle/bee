package com.ccnu.hiic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class NodeRegistActivity extends AppCompatActivity {

    private Spinner type_choice;
//    private String[] typeChoice = {"tem_hum", "smoke", "air", "door", "water"};
private String[] typeChoice = {"tem_hum", "weather"};
    private String nodeType = typeChoice[0];//节点类型，默认
    private EditText node_num;
    private TextView floor_id;
    private EditText room_id;
    private EditText temp_min;
    private EditText temp_max;
    private EditText humi_min;
    private EditText humi_max;
    private EditText temp2_min;
    private EditText temp2_max;
    private EditText humi2_min;
    private EditText humi2_max;
    private Button bt_add_node;
    private LinearLayout ll_temp_humi;
    private HttpLoader httpLoader;


    private String apiary_name;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_regist);
        httpLoader = new HttpLoader();

        initView();
    }

    public void initView() {
        type_choice = findViewById(R.id.type_choice);
        ll_temp_humi = findViewById(R.id.ll_temp_humi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("节点注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NodeRegistActivity.this, R.layout.spniner_style, getData());//将可选内容与ArrayAdapter连接起来
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉列表的风格
        type_choice.setAdapter(adapter);//将adapter2 添加到spinner中
        type_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nodeType = typeChoice[i];//节点选择
                if (!nodeType.equals(typeChoice[0])) {
                    ll_temp_humi.setVisibility(View.GONE);
                } else {
                    ll_temp_humi.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        node_num = findViewById(R.id.node_num);
        floor_id = findViewById(R.id.floor_id);
        room_id = findViewById(R.id.room_id);
        bt_add_node = findViewById(R.id.bt_add_node);
        temp_min = findViewById(R.id.temp_min);
        temp_max = findViewById(R.id.temp_max);
        humi_min = findViewById(R.id.humi_min);
        humi_max = findViewById(R.id.humi_max);
        temp2_min = findViewById(R.id.temp2_min);
        temp2_max = findViewById(R.id.temp2_max);
        humi2_min = findViewById(R.id.humi2_min);
        humi2_max = findViewById(R.id.humi2_max);
        apiary_name = getIntent().getStringExtra("floor_name");
        floor_id.setText(apiary_name);
        bt_add_node.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();

                if(now - lastClickTime >3000){
                    System.out.println("过了3s");//3秒内重复操作无效
                    lastClickTime = now;
                    dataCheck();
                }else {
                    System.out.println("3秒内重复操作无效");
                }
            }
        });
    }

    private void dataCheck() {
        System.out.println("节点注册");
        String nodeId = node_num.getText().toString().trim();
        String roomId = room_id.getText().toString().trim();
        String tempMin="";
        String tempMax="";
        String humiMin="";
        String humiMax="";
        String tempMin2="";
        String tempMax2="";
        String humiMin2="";
        String humiMax2="";
        if (nodeType.equals(typeChoice[0])) {
            tempMin = temp_min.getText().toString().trim();
            tempMax = temp_max.getText().toString().trim();
            humiMin = humi_min.getText().toString().trim();
            humiMax = humi_max.getText().toString().trim();
            tempMin2 = temp2_min.getText().toString().trim();
            tempMax2 = temp2_max.getText().toString().trim();
            humiMin2 = humi2_min.getText().toString().trim();
            humiMax2 = humi2_max.getText().toString().trim();
        }
        if (nodeType.equals(typeChoice[0])){
            if (tempMin.equals("") || tempMax.equals("") || humiMin.equals("") || humiMax.equals("")||tempMin2.equals("") || tempMax2.equals("")
                    || humiMin2.equals("") || humiMax2.equals("") || nodeType.trim().equals("") || nodeId.equals("") || roomId.equals("")){
                Toast.makeText(getApplicationContext(), "输入不能有空，请检查！", Toast.LENGTH_LONG).show();
            }else if (Integer.parseInt(tempMax) < Integer.parseInt(tempMin) || Integer.parseInt(humiMax) < Integer.parseInt(humiMin) ||
            Integer.parseInt(tempMax2) < Integer.parseInt(tempMin2) || Integer.parseInt(humiMax2) < Integer.parseInt(humiMin2)){
                Toast.makeText(getApplicationContext(), "最大值不能小于最小值！", Toast.LENGTH_LONG).show();
            }else {
                addNodeInfo(UserManage.getInstance().getUserInfo(NodeRegistActivity.this).getUserName(),
                        nodeId, apiary_name, roomId, nodeType,
                        Integer.parseInt(humiMax), Integer.parseInt(humiMin),Integer.parseInt(tempMin), Integer.parseInt(tempMax),
                        Integer.parseInt(humiMax2),Integer.parseInt(tempMax2),Integer.parseInt(humiMin2), Integer.parseInt(tempMin2));
            }
        }else {
            if (nodeType.trim().equals("") || nodeId.equals("")){
                Toast.makeText(getApplicationContext(), "输入不能有空，请检查！", Toast.LENGTH_LONG).show();
            }else {
                addOtherNodeInfo(UserManage.getInstance().getUserInfo(NodeRegistActivity.this).getUserName(),
                        nodeId, apiary_name, roomId, nodeType);
            }
        }


//        if ((nodeType.equals(typeChoice[0]) && tempMin.equals("") || tempMax.equals("") || humiMin.equals("") || humiMax.equals(""))
//                ||(nodeType.trim().equals("") || nodeId.equals("") || floorId.equals("") || roomId.equals(""))) {
//            Toast.makeText(getApplicationContext(), "输入不能有空，请检查！", Toast.LENGTH_LONG).show();
//        } else if (nodeType.equals(typeChoice[0]) &&((Integer.parseInt(tempMax) < Integer.parseInt(tempMin) || Integer.parseInt(humiMax) < Integer.parseInt(humiMin))) {
//            Toast.makeText(getApplicationContext(), "最大值不能小于最小值！", Toast.LENGTH_LONG).show();
//        } else {
//            if (nodeType.equals(typeChoice[0])) {
//                addNodeInfo(UserManage.getInstance().getUserInfo(NodeRegistActivity.this).getUserName(),
//                        nodeId, floorId, roomId,
//                        nodeType, Integer.parseInt(humiMax), Integer.parseInt(humiMin),
//                        Integer.parseInt(tempMin), Integer.parseInt(tempMax));
//            } else {
//                addOtherNodeInfo(UserManage.getInstance().getUserInfo(NodeRegistActivity.this).getUserName(),
//                        nodeId, floorId, roomId, nodeType);
//            }
//
//        }
    }

    //温湿度
    public void addNodeInfo(String username, String dev_eui, String floorId, String roomId,
                            String type, int humiMax, int humiMin, int tempMin, int tempMax, int h2sMax, int nh4Max, int h2sMin, int nh4Min) {
//        System.out.println("用户名"+username);
//        System.out.println("用dev_eui"+dev_eui);
//        System.out.println("用floor_id"+floor_id);
//        System.out.println("用room_id"+room_id);
//        System.out.println("用type"+type);
//        System.out.println("用humiMax"+humiMax);
//        System.out.println("用humiMin"+humiMin);
//        System.out.println("用tempMin"+tempMin);
//        System.out.println("用tempMax"+tempMax);
        httpLoader.addNodeInfo(username, dev_eui, floorId, roomId,
                type, humiMax, humiMin, tempMin, tempMax, h2sMax, nh4Max, h2sMin, nh4Min).subscribe(new Action1<String>() {
            @Override
            public void call(String flags) {
                System.out.println("数据AddNote" + flags);
                if (flags.equals("1")) {
                    Toast.makeText(NodeRegistActivity.this, "该设备已存在", Toast.LENGTH_LONG).show();
                } else if (flags.equals("2")) {
                    Toast.makeText(NodeRegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
//                    node_num.setText("");
//                    floor_id.setText("");
//                    room_id.setText("");
//                    temp_min.setText("");
//                    temp_max.setText("");
//                    humi_min.setText("");
//                    humi_max.setText("");
                } else if (flags.equals("3")) {
                    Toast.makeText(NodeRegistActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //获取失败
                Log.e("TAG", "error message:" + throwable.getMessage());
                System.out.println("异常AddNode" + throwable.getMessage());
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


    public void addOtherNodeInfo(String username, String dev_eui, String floorId, String roomId,
                                 String type) {

        httpLoader.addOtherNodeInfo(username, dev_eui, floorId, roomId,
                type).subscribe(new Action1<String>() {
            @Override
            public void call(String flags) {
                System.out.println("数据AddNote" + flags);
                if (flags.equals("1")) {
                    Toast.makeText(NodeRegistActivity.this, "该设备已存在", Toast.LENGTH_LONG).show();
                } else if (flags.equals("2")) {
                    Toast.makeText(NodeRegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
//                    node_num.setText("");
//                    floor_id.setText("");
//                    room_id.setText("");
                } else if (flags.equals("3")) {
                    Toast.makeText(NodeRegistActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //获取失败
                Log.e("TAG", "error message:" + throwable.getMessage());
                System.out.println("异常AddNode" + throwable.getMessage());
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

    private List<String> getData() {
        List<String> dataList = new ArrayList<>();
        dataList.add("温湿度");
        dataList.add("气象");
//        dataList.add("烟雾");
//        dataList.add("气体");
//        dataList.add("门禁");
//        dataList.add("水浸");
        return dataList;
    }
}
