package com.ccnu.hiic.Bean;


import java.util.ArrayList;
import java.util.List;

public class GetApiaryDetail {

    private int flag;
    private ArrayList<ApiaryDetail> floor;

    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }

    public GetApiaryDetail(ArrayList<ApiaryDetail> floor) {
        this.floor = floor;
    }
    public ArrayList<ApiaryDetail> getFloor() {
        return floor;
    }

    public void setFloor(ArrayList<ApiaryDetail> floor) {
        this.floor = floor;
    }


}
