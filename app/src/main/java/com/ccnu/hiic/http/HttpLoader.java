package com.ccnu.hiic.http;

import com.ccnu.hiic.Bean.GetApiaryDetail;
import com.ccnu.hiic.Bean.RegistReturnObject;
import com.ccnu.hiic.Bean.GetDetialNodes;

import com.ccnu.hiic.Bean.GetClientsName;
import com.ccnu.hiic.Bean.LoginReturnObject;
import com.ccnu.hiic.Bean.GetNodesInfo;
import com.ccnu.hiic.Bean.InformReturnObject;

import com.ccnu.hiic.Bean.RoomGetReturnObject;
import com.ccnu.hiic.Bean.GetWeatherInfo;

import rx.Observable;


public class HttpLoader extends ObjectLoader {
    private ApiService apiService;

    public HttpLoader(){
        apiService = RetrofitServiceManager.getInstance().create(ApiService.class);
    }


    //登陆
    public Observable<LoginReturnObject> login(String name, String password){
        return observe(apiService.Login(name,password));
    }

    //主页请求数据

    public Observable<GetNodesInfo> nodesInfo(String username,String floor_name){
        return observe(apiService.NodesInfo(username,floor_name));
    }

    //请求气象信息
    public Observable<GetWeatherInfo> WeatherInfo(String floor_name){
        return observe(apiService.WeatherInfo(floor_name));
    }
    //详细房间信息
    public Observable<GetDetialNodes> detailNodesInfo(String name, String floorId , String  roomId){
        return observe(apiService.DetialNodesInfo(name,floorId,roomId));
    }
    //读取电话、邮箱
    public Observable<InformReturnObject> infrom(String name){
        return observe(apiService.Infrom(name));
    }
    //普通用户注册
    public Observable<RegistReturnObject> regist(String name, String password, String phone, String email){
        return observe(apiService.Register(name,password,phone,email));
    }
    //管理员注册
    public Observable<RegistReturnObject> adminregist(String area,String area_name,String company_name,String code,String name, String password,String phone,String email){
        return observe(apiService.AdminRegister(area,area_name,company_name,code,name,password,phone,email));
    }
    //查看所有用户
    public Observable<GetClientsName> showAllUsr(String username){
        return observe(apiService.showAllUsr(username));
    }

    //查看蜂场
    public Observable<GetApiaryDetail> showAllApiary(String username){
        return observe(apiService.showAllApiary(username));
    }

    //新增用户
    public Observable<String> addClient(String username,String newClientName,int monitor){
        return observe(apiService.addClient(username,newClientName,monitor));
    }

    //温湿度节点配置
    public Observable<String> addNodeInfo(String username, String dev_eui,String floor_id, String room_id, String type, int humiMax, int humiMin,
                                          int tempMin, int tempMax,int h2sMax,int nh4Max,int h2sMin, int nh4Min){
        return observe(apiService.addNodeInfo(username, dev_eui, floor_id,  room_id,
                 type,  humiMax,  humiMin,  tempMin,  tempMax, h2sMax,nh4Max ,h2sMin, nh4Min ));
    }


    //其他节点配置
    public Observable<String> addOtherNodeInfo(String username, String dev_eui,String floor_id, String room_id,
                                          String type){
        return observe(apiService.addOtherNodeInfo(username, dev_eui, floor_id,  room_id, type));
    }

    //提交房间
    public Observable<RoomGetReturnObject> roomGet(String username, String floor_id, String room_id){
        return observe(apiService.roomGet(username, floor_id,room_id));
    }

    //配置该房间
    public Observable<String> theshold(String username, String floor_id, String room_id, int humiMax, int humiMin, int tempMin, int tempMax,
                                       int h2sMax, int nh4Max,int h2sMin, int nh4Min){
        return observe(apiService.theshold(username,  floor_id,  room_id,
                  humiMax,  humiMin,  tempMin,  tempMax, h2sMax, nh4Max, h2sMin, nh4Min));
    }

    //配置所有房间
    public Observable<String> thesholdall(String username,String floor_id, int humiMax, int humiMin, int tempMin, int tempMax,
                                          int h2sMax, int nh4Max,int h2sMin, int nh4Min){
        return observe(apiService.thesholdall(username, 1,floor_id, humiMax,  humiMin,  tempMin,  tempMax,
                h2sMax, nh4Max, h2sMin, nh4Min));
    }

    //修改信息
    public Observable<String> change_phone(String name, String new_tel){
        return observe(apiService.ChangePhone(name,new_tel));
    }

    //修改邮箱
    public Observable<String> change_email(String name, String new_email){
        return observe(apiService.ChangeMail(name,new_email));
    }
    //修改密码
    public Observable<String> change_pwd(String name, String old_pwd,String new_pwd){
        return observe(apiService.ChangePassword(name,old_pwd,new_pwd));
    }

    //新增蜂场
    public Observable<String> addApiary(String username,String floor_name){
        return observe(apiService.addApiary(username,floor_name));
    }

    //开启控制
    public Observable<String> control_open(String username, String floor_name, String room_id, boolean fan, boolean heat,
                                           String h2sMax, String nh4Max,String h2sMin, String nh4Min){
        return observe(apiService.control_open(username,  floor_name,  room_id,
                fan,  heat, h2sMax, nh4Max, h2sMin, nh4Min));
    }
    //关闭控制
    public Observable<String> control_close(String username, String floor_name, String room_id, boolean fan, boolean heat){
        return observe(apiService.control_close(username,  floor_name,  room_id, fan,  heat));
    }

}
