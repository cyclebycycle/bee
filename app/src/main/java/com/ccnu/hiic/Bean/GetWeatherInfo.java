package com.ccnu.hiic.Bean;

public class GetWeatherInfo {
    /**
     * flag : 1
     * data : {"id":"5161","dev_eui":"0101000000000001","temp_value":"19.5","humi_value":"83","wind_speed":"离线","wind_direction":"离线","rainfall":"离线","ultraviolet":"14349","pressure":"44122.4","light":"离线","datetime":"2019-05-16 20:58:27"}
     */

    private int flag;
    private DataBean data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5161
         * dev_eui : 0101000000000001
         * temp_value : 19.5
         * humi_value : 83
         * wind_speed : 离线
         * wind_direction : 离线
         * rainfall : 离线
         * ultraviolet : 14349
         * pressure : 44122.4
         * light : 离线
         * datetime : 2019-05-16 20:58:27
         */

        private String wind_speed;
        private String wind_direction;
        private String rainfall;
        private String light;



        public String getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(String wind_speed) {
            this.wind_speed = wind_speed;
        }

        public String getWind_direction() {
            return wind_direction;
        }

        public void setWind_direction(String wind_direction) {
            this.wind_direction = wind_direction;
        }

        public String getRainfall() {
            return rainfall;
        }

        public void setRainfall(String rainfall) {
            this.rainfall = rainfall;
        }


        public String getLight() {
            return light;
        }

        public void setLight(String light) {
            this.light = light;
        }

    }
//    int flag;
//    String wind_speed;
//    String wind_direction;
//    String rainfall;
//    String light;
//
//    public int getFlag() {
//        return flag;
//    }
//
//    public void setFlag(int flag) {
//        this.flag = flag;
//    }
//
//    public String getWind_speed() {
//        return wind_speed;
//    }
//
//    public void setWind_speed(String wind_speed) {
//        this.wind_speed = wind_speed;
//    }
//
//    public String getWind_direction() {
//        return wind_direction;
//    }
//
//    public void setWind_direction(String wind_direction) {
//        this.wind_direction = wind_direction;
//    }
//
//    public String getRainfall() {
//        return rainfall;
//    }
//
//    public void setRainfall(String rainfall) {
//        this.rainfall = rainfall;
//    }
//
//    public String getLight() {
//        return light;
//    }
//
//    public void setLight(String light) {
//        this.light = light;
//    }
}
