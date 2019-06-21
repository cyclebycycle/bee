package com.ccnu.hiic.Bean;

/**
 * Created by Administrator on 2017/9/19.
 */

public class Node {
    private String dev_eui;
    private String floor_id;
    private String room_id;
    private String temp_value;
    private String humi_value;



    public String getDevEui() {
        return dev_eui;
    }
    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getTemp_value() {
        return temp_value;
    }

    public void setTemp_value(String temp_value) {
        this.temp_value = temp_value;
    }

    public String getHumi_value() {
        return humi_value;
    }

    public void setHumi_value(String humi_value) {
        this.humi_value = humi_value;
    }

    public String getFloor() {
        return floor_id;
    }
    public String getRoom() {
        return room_id;
    }
    public String getTemp() {
        return room_id;
    }
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUserphone() {
//        return userphone;
//    }
//
//    public void setUserphone(String userphone) {
//        this.userphone = userphone;
//    }
}
