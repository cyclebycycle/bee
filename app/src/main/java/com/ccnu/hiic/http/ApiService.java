package com.ccnu.hiic.http;

import com.ccnu.hiic.Bean.GetApiaryDetail;
import com.ccnu.hiic.Bean.GetDetialNodes;

import com.ccnu.hiic.Bean.GetClientsName;
import com.ccnu.hiic.Bean.LoginReturnObject;
import com.ccnu.hiic.Bean.GetNodesInfo;
import com.ccnu.hiic.Bean.InformReturnObject;

import com.ccnu.hiic.Bean.RegistReturnObject;
import com.ccnu.hiic.Bean.RoomGetReturnObject;
import com.ccnu.hiic.Bean.GetWeatherInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface ApiService {
    //登陆
    @FormUrlEncoded
    @POST("AndroidLoginJudge/")
    Observable<LoginReturnObject> Login(@Field("name") String name,
                                        @Field("password") String password);
    //请求主页所有数据
    @FormUrlEncoded
    @POST("Android_nodes_info/")
    Observable<GetNodesInfo> NodesInfo(@Field("username") String username,
                                       @Field("floor_name") String floor_name);

    //请求气象信息
    @FormUrlEncoded
    @POST("enviorment_param_search/")
    Observable<GetWeatherInfo> WeatherInfo(@Field("floor_name") String floor_name);

    //详细页面信息
    @FormUrlEncoded
    @POST("check_nodes_info/")
    Observable<GetDetialNodes>DetialNodesInfo(@Field("username") String username,
                                              @Field("floor_id") String floorId,
                                              @Field("room_id") String roomId);

    //查看所有用户
    @FormUrlEncoded
    @POST("show_all_usr/")
    Observable<GetClientsName> showAllUsr(@Field("username") String username);

    //查看蜂场
    @FormUrlEncoded
    @POST("show_all_bee/")
    Observable<GetApiaryDetail> showAllApiary(@Field("username") String username);

    //新增用户
    @FormUrlEncoded
    @POST("new_user/")
    Observable<String> addClient(@Field("username") String username,
                                 @Field("new_usr") String newClient,
                                         @Field("new_monitor") int monitor);
    //注册温湿度节点
    @FormUrlEncoded
    @POST("app_set_nodeInfo/")
    Observable<String> addNodeInfo(@Field("username") String username,
                                 @Field("dev_eui") String dev_eui,
                                 @Field("floor_id") String floor_id,
                                   @Field("room_id") String room_id,
                                   @Field("type") String type,
//                                   @Field("posx") int posx,
//                                   @Field("posy") int posy,
                                   @Field("humiMax") int humiMax,
                                   @Field("humiMin") int humiMin,
                                   @Field("tempMin") int tempMin,
                                   @Field("tempMax") int tempMax,
                                   @Field("h2sMax") int h2sMax,
                                   @Field("nh4Max") int nh4Max,
                                   @Field("h2sMin") int h2sMin,
                                   @Field("nh4Min") int nh4Min);



    //注册其他节点
    @FormUrlEncoded
    @POST("app_set_nodeInfo/")
    Observable<String> addOtherNodeInfo(@Field("username") String username,
                                   @Field("dev_eui") String dev_eui,
                                   @Field("floor_id") String floor_id,
                                   @Field("room_id") String room_id,
                                   @Field("type") String type);
    //APP提交房间号
    @FormUrlEncoded
    @POST("app_set_threshold")
    Observable<RoomGetReturnObject> roomGet(@Field("username") String username,
                                            @Field("floor_id") String floor_id,
                                            @Field("room_id") String room_id);


    //配置到该房间
    @FormUrlEncoded
    @POST("app_set_threshold")
    Observable<String> theshold(@Field("username")String username,
                                @Field("floor_id") String floor_id,
                                @Field("room_id") String room_id,
                                @Field("humiMax") int humiMax,
                                @Field("humiMin") int humiMin,
                                @Field("tempMin") int tempMin,
                                @Field("tempMax") int tempMax,
                                @Field("h2sMax") int h2sMax,
                                @Field("nh4Max") int nh4Max,
                                @Field("h2sMin") int h2sMin,
                                @Field("nh4Min") int nh4Min);

    //配置到所有房间
    @FormUrlEncoded
    @POST("app_set_threshold")
    Observable<String> thesholdall(@Field("username")String username,
                                   @Field("ifALL") int ifAll,
                                   @Field("floor_id") String floor_id,
                                   @Field("humiMax") int humiMax,
                                   @Field("humiMin") int humiMin,
                                   @Field("tempMin") int tempMin,
                                   @Field("tempMax") int tempMax,
                                   @Field("h2sMax") int h2sMax,
                                   @Field("nh4Max") int nh4Max,
                                   @Field("h2sMin") int h2sMin,
                                   @Field("nh4Min") int nh4Min);

    //管理用户注册
    @FormUrlEncoded
    @POST("MonitorRegist/")
    Observable<RegistReturnObject> AdminRegister(
            @Field("area") String area,
            @Field("area_name") String area_name,
            @Field("company_name")String company_name,
            @Field("r_code")String code,
            @Field("r_name") String names,
            @Field("r_pwd") String password,
            @Field("r_tel") String phone,
            @Field("r_email") String email);

    //用户注册
    @FormUrlEncoded
    @POST("MonitorRegist/")
    Observable<RegistReturnObject> Register(
            @Field("r_name") String names,
            @Field("r_pwd") String password,
            @Field("r_tel") String phone,
            @Field("r_email") String email);




    //读取电话、邮箱
    @FormUrlEncoded
    @POST("AndroidModifyUserInfo/")
    Observable<InformReturnObject> Infrom(@Field("username") String name);

    //修改电话
    @FormUrlEncoded
    @POST("AndroidModifyUserInfo/")
    Observable<String> ChangePhone(@Field("username") String name,
                                         @Field("new_tel") String new_tel);
    //修改邮箱
    @FormUrlEncoded
    @POST("AndroidModifyUserInfo/")
    Observable<String> ChangeMail(@Field("username") String name,
                                   @Field("new_email") String new_email);
    //修改密码
    @FormUrlEncoded
    @POST("AndroidModifyUserInfo/")
    Observable<String> ChangePassword(@Field("username") String name,
                                  @Field("old_pwd") String old_pwd,
                                      @Field("new_pwd") String new_pwd);


    //新增蜂场
    @FormUrlEncoded
    @POST("app_add_floor/")
    Observable<String> addApiary(@Field("username") String username,
                                 @Field("floor_name") String floor_name);

    //开启提交状态
    @FormUrlEncoded
    @POST("fan_heat_set")
    Observable<String> control_open(@Field("username")String username,
                                @Field("floor_name") String floor_name,
                                @Field("room_id") String room_id,
                                @Field("fan") boolean fan,
                                @Field("heat") boolean heat,
                                @Field("h2sMax") String h2sMax,
                                @Field("nh4Max") String nh4Max,
                                @Field("h2sMin") String h2sMin,
                                @Field("nh4Min") String nh4Min);
    //开启提交状态
    @FormUrlEncoded
    @POST("fan_heat_set")
    Observable<String> control_close(@Field("username")String username,
                               @Field("floor_name") String floor_name,
                               @Field("room_id") String room_id,
                               @Field("fan") boolean fan,
                               @Field("heat") boolean heat);
}
