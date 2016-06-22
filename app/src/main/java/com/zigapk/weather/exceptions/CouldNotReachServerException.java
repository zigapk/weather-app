package com.zigapk.weather.exceptions;

/**
 * Created by zigapk on 22.6.2016.
 */
public class CouldNotReachServerException extends Exception{
    public CouldNotReachServerException(){}
    public CouldNotReachServerException(String message){
        super(message);
    }
}
