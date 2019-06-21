package com.ccnu.hiic.Bean;

public class GetDetialNodes {

    /**
     * dev_eui : 0101000000000013
     * datetime : 2019-05-15 02:52:29
     * weight : -1
     * temp_value : 26.25
     * humi_value : 59.49
     */

    private String dev_eui;
    private String datetime;
    private String weight;
    private String temp_value;
    private String humi_value;
    private String fan;
    private String heat;

    public String getDev_eui() {
        return dev_eui;
    }

    public void setDev_eui(String dev_eui) {
        this.dev_eui = dev_eui;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }
}
