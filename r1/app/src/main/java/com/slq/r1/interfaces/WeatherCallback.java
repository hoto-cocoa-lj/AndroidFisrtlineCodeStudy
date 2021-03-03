package com.slq.r1.interfaces;

import com.slq.r1.pojo.Weather;

public interface WeatherCallback {
    public void goUp(Weather weather );
    public void goDown(Weather weather );
}
