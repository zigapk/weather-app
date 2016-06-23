package com.zigapk.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AddCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        Intent intent = new Intent();
        intent.putExtra("city", "");
        setResult(MainActivity.CITY_REQUEST_CODE, intent);
    }

    public void confirm(View v){
        String city = ((TextInputEditText) findViewById(R.id.addCityInput)).getText().toString();
        if (!city.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("city", city);
            setResult(MainActivity.CITY_REQUEST_CODE, intent);
        }
        finish();
    }

    public void close(View v){
        finish();
    }
}
