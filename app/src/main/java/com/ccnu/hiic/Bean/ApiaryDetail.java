package com.ccnu.hiic.Bean;

import java.util.List;

public class ApiaryDetail {


    private int dismiss;
    private int abnormal;
    private String floor_name;
    private String username;

    public int getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(int abnormal) {
        this.abnormal = abnormal;
    }

    public int getDismiss() {
        return dismiss;

    }

    public void setDismiss(int dismiss) {
        this.dismiss = dismiss;
    }

    public String getFloor_name() {
            return floor_name;
        }

        public void setFloor_name(String floor_name) {
            this.floor_name = floor_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

}
