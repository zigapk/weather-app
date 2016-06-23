package com.zigapk.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static TextView cityNameTv;
    private static TextView temperatureTv;
    private static TextView humidityTv;
    private static TextView descriptionTv;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cityNameTv = (TextView) findViewById(R.id.city_name);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        humidityTv = (TextView) findViewById(R.id.humidity);
        descriptionTv = (TextView) findViewById(R.id.description);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        final City city = (City) intent.getSerializableExtra("city");
        if (city.get_performed) {
            cityNameTv.setText(city.user_input_name);
            temperatureTv.setText(city.main.formatedTemp());
            humidityTv.setText(Integer.toString(city.main.humidity));
            descriptionTv.setText(city.weather[0].description);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            cityNameTv.setText(city.user_input_name);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final City tempCity = city.get();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                cityNameTv.setText(tempCity.name);
                                temperatureTv.setText(tempCity.main.formatedTemp());
                                humidityTv.setText(Integer.toString(tempCity.main.humidity));
                                descriptionTv.setText(tempCity.weather[0].description);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                cityNameTv.setText(MainActivity.context.getResources().getString(
                                        R.string.no_data_for, city.user_input_name));
                                cityNameTv.setTextColor(MainActivity.context.getResources().
                                        getColor(R.color.red));
                                temperatureTv.setText("/");
                                humidityTv.setText("/");
                                descriptionTv.setText("/");
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                }
            }).start();
        }
    }
}
