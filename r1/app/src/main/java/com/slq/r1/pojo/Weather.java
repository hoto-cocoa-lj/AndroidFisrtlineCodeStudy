package com.slq.r1.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Weather {
    int id;
    String name;

    @SerializedName("weather_id")  String weatherId;
    String[] parents;

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weatherId='" + weatherId + '\'' +
                ", parents=" + Arrays.toString(parents) +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String[] getParents() {
        return parents;
    }

    public void setParents(String[] parents) {
        this.parents = parents;
    }

    public Weather(int id, String name, String weatherId, String[] parents) {
        this.id = id;
        this.name = name;
        this.weatherId = weatherId;
        this.parents = parents;
    }
}
