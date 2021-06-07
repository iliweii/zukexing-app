package com.zukexing.app.pojo;

import java.util.Arrays;

public class Weather {
    private Integer status; // 返回状态
    private Integer count; // 返回结果总数目
    private String info; // 返回的状态信息
    private String infocode; // 返回状态说明,10000代表正确
    private WeatherLive[] lives; // 实况天气数据信息

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public WeatherLive[] getLives() {
        return lives;
    }

    public void setLives(WeatherLive[] lives) {
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "status=" + status +
                ", count=" + count +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", lives=" + Arrays.toString(lives) +
                '}';
    }
}
