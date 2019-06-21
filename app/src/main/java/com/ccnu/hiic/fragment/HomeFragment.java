package com.ccnu.hiic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.hiic.Bean.GetNodesInfo;
import com.ccnu.hiic.Bean.GetWeatherInfo;
import com.ccnu.hiic.Bean.Node;
import com.ccnu.hiic.adapter.NodeInfoAdapter;
import com.ccnu.hiic.http.Fault;
import com.ccnu.hiic.http.HttpLoader;
import com.ccnu.hiic.util.UserManage;
import com.ccnu.hjjc.R;
import com.ccnu.hiic.activity.NodeDetialActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
public class HomeFragment extends Fragment{
    View view;
    private ListView lv_nodes;
    private TextView wind_speed;
    private TextView wind_direction;
    private TextView light;
    private TextView rainfall;

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

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        username= UserManage.getInstance().getUserInfo(getContext()).getUserName();
        if (args != null) {
//            username=args.getString("username");
        }

    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_home,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.main_srl);

        lv_nodes=(ListView)view.findViewById(R.id.lv_nodes);
        wind_speed=(TextView)view.findViewById(R.id.wind_speed_111);
        wind_direction=(TextView)view.findViewById(R.id.wind_direction);
        light=(TextView)view.findViewById(R.id.light);
        rainfall=(TextView)view.findViewById(R.id.rainfall);
        nodeInfoAdapter=new NodeInfoAdapter(this.getContext());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("进入下拉刷新");
                new LoadDataThread().start();
            }
        });

        lv_nodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Node node=nodeInfoAdapter.getNodes(position);
                if (node == null) return;
                System.out.println("要发起的请求是"+node.getFloor_id()+";"+node.getRoom_id());

                Intent intent=new Intent(getActivity(), NodeDetialActivity.class);
                intent.putExtra("floor_id",node.getFloor_id());
                intent.putExtra("room_id",node.getRoom_id());
                intent.putExtra("username",username);

                startActivity(intent);

            }
        });

//        NodesInfo(username);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = 0;
//        NodesInfo(username);
        WeatherInfo(username);
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
//                NodesInfo(username);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }
    //获取当前区域所有数据
//    public void NodesInfo (String name){
//        httpLoader = new HttpLoader();
//        httpLoader.nodesInfo(name).subscribe(new Action1<GetNodesInfo>() {
//            @Override
//            public void call(GetNodesInfo getNodesInfo) {
//                System.out.println("返回的主要页面数据" + new Gson().toJson(getNodesInfo));
//                ArrayList<Node> nodesInfo=getNodesInfo.getData();
//                for (int i=0;i< nodesInfo.size();i++){
//                    System.out.println("详细数据" + new Gson().toJson(nodesInfo.get(i)));
//                }
//                if(nodesInfo.size()!=0){
//                    nodeInfoAdapter.clear();
//                    nodeInfoAdapter.addNodes(nodesInfo);
//                    lv_nodes.setAdapter(nodeInfoAdapter);
//                    nodeInfoAdapter.notifyDataSetChanged();
//                    if (flag == 1){
//                        Toast.makeText(getActivity(), "更新成功",Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    if(flag ==1){
//                        Toast.makeText(getActivity(), "暂无数据",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                swipeRefreshLayout.setRefreshing(false);
////
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                //获取失败
//                swipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), "请求失败",Toast.LENGTH_SHORT).show();
//                Log.e("TAG", "error message:" + throwable.getMessage());
//                if (throwable instanceof Fault) {
//                    Fault fault = (Fault) throwable;
//                    if (fault.getErrorCode() == 404) {
//                        //错误处理
//                    } else if (fault.getErrorCode() == 500) {
//                        //错误处理
//                    } else if (fault.getErrorCode() == 501) {
//                        //错误处理
//                    }
//                }
//
//            }
//        });
//    }

    //获取当前区域气象数据
    public void WeatherInfo (String name){
        httpLoader1 = new HttpLoader();
        httpLoader1.WeatherInfo(name).subscribe(new Action1<GetWeatherInfo>() {
            @Override
            public void call(GetWeatherInfo getWeatherInfo) {
                Wind_speed=getWeatherInfo.getData().getWind_speed().trim();
                Wind_direction=getWeatherInfo.getData().getWind_direction().trim();
                Light=getWeatherInfo.getData().getLight().trim();
                Rainfall=getWeatherInfo.getData().getRainfall().trim();
                System.out.println("光强"+Light);
                wind_speed.setText(Wind_speed);
                wind_direction.setText(Wind_direction);
                light.setText(Light);
                rainfall.setText(Rainfall);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //获取失败
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "请求失败",Toast.LENGTH_SHORT).show();
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
