package com.ccnu.hiic.Bean;

public class RoomGetReturnObject {

    /**
     * data : {"tem_high_threshold":"80","tem_low_threshold":"-10","hum_high_threshold":"60","hum_low_threshold":"20"}
     * flag : 1
     */

    private DataBean data;
    private int flag;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public static class DataBean {
        /**
         * tem_high_threshold : 80
         * tem_low_threshold : -10
         * hum_high_threshold : 60
         * hum_low_threshold : 20
         */

        private int tem_high_threshold;
        private int tem_low_threshold;
        private int hum_high_threshold;
        private int hum_low_threshold;
        private int h2s_high_threshold;
        private int h2s_min_threshold;
        private int nh4_high_threshold;
        private int nh4_min_threshold;

        public int getTem_high_threshold() {
            return tem_high_threshold;
        }

        public void setTem_high_threshold(int tem_high_threshold) {
            this.tem_high_threshold = tem_high_threshold;
        }

        public int getTem_low_threshold() {
            return tem_low_threshold;
        }

        public void setTem_low_threshold(int tem_low_threshold) {
            this.tem_low_threshold = tem_low_threshold;
        }

        public int getHum_high_threshold() {
            return hum_high_threshold;
        }

        public void setHum_high_threshold(int hum_high_threshold) {
            this.hum_high_threshold = hum_high_threshold;
        }

        public int getHum_low_threshold() {
            return hum_low_threshold;
        }

        public void setHum_low_threshold(int hum_low_threshold) {
            this.hum_low_threshold = hum_low_threshold;
        }

        public int getH2s_high_threshold() {
            return h2s_high_threshold;
        }

        public void setH2s_high_threshold(int h2s_high_threshold) {
            this.h2s_high_threshold = h2s_high_threshold;
        }

        public int getH2s_min_threshold() {
            return h2s_min_threshold;
        }

        public void setH2s_min_threshold(int h2s_min_threshold) {
            this.h2s_min_threshold = h2s_min_threshold;
        }

        public int getNh4_high_threshold() {
            return nh4_high_threshold;
        }

        public void setNh4_high_threshold(int nh4_high_threshold) {
            this.nh4_high_threshold = nh4_high_threshold;
        }

        public int getNh4_min_threshold() {
            return nh4_min_threshold;
        }

        public void setNh4_min_threshold(int nh4_min_threshold) {
            this.nh4_min_threshold = nh4_min_threshold;
        }
    }
}

