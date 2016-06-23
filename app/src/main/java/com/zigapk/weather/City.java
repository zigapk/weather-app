package com.zigapk.weather;

import com.google.gson.Gson;
import com.zigapk.weather.exceptions.CityNotFoundException;
import com.zigapk.weather.exceptions.CouldNotReachServerException;
import com.zigapk.weather.utils.InternetUtils;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * Created by zigapk on 22.6.2016.
 */
public class City implements Serializable{
    Weather[] weather = new Weather[0];
    MainWeatherData main = new MainWeatherData();
    int cod = 0;
    String name = "";
    String user_input_name = "";

    public boolean get_performed = false;

    public City(){}

    public City(String user_input){
        user_input_name = user_input;
    }

    public City get() throws CityNotFoundException, CouldNotReachServerException{
        return get(user_input_name);
    }

    public City get(String city_name) throws CityNotFoundException, CouldNotReachServerException{
        try {
            String json = InternetUtils.getTextFromUrl(
                    "http://api.openweathermap.org/data/2.5/weather?appid=bf2bb82e4156bfeda8245c4e29f61b4f&q="
                            + URLEncoder.encode(city_name));
            City result = new Gson().fromJson(json, City.class);
            if (result.cod != 200) throw new CityNotFoundException();

            result.user_input_name = user_input_name;
            result.get_performed = true;
            return result;
        }catch (Exception e){
            throw new CouldNotReachServerException();
        }
    }

}

class Weather implements Serializable {
    String main = "";
    String description = "";
}

class MainWeatherData implements Serializable {
    double temp = 0;
    int humidity = 0;

    public String formatedTemp(){
        double mid = temp - 273.15;
        DecimalFormat f = new DecimalFormat("##.0");
        return f.format(mid) + " °C";
    }
}

