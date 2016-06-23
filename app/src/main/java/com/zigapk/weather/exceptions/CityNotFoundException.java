package com.zigapk.weather.exceptions;

/**
 * Created by zigapk on 22.6.2016.
 */
public class CityNotFoundException extends Exception{
    public CityNotFoundException(){}
    public CityNotFoundException(String message){
        super(message);
    }
}
