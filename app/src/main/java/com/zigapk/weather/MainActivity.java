package com.zigapk.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static SwipeRefreshLayout swipeRefreshLayout;
    private static FloatingActionButton fab;
    public static Context context;
    private static CoordinatorLayout coordinatorLayout;

    final public static int CITY_REQUEST_CODE = 0;
    private static boolean noConnectionSnackbarOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddCityActivity.class),
                        CITY_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_REQUEST_CODE){
            String city = data.getStringExtra("city");
            if (!city.equals("")) {
                // add city to list
            }
        }
    }

    public static void showNoConnectionSnackbar(){
        if (!noConnectionSnackbarOpen) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                    context.getString(R.string.no_connection), Snackbar.LENGTH_LONG);

            // reset noConnectionSnackbarOpen boolean after snackbar gets invisible
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    noConnectionSnackbarOpen = false;
                }
            }, snackbar.getDuration());

            noConnectionSnackbarOpen = true;
            snackbar.show();
        }
    }
}
