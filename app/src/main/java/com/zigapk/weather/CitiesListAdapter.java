package com.zigapk.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zigapk.weather.exceptions.CityNotFoundException;
import com.zigapk.weather.exceptions.CouldNotReachServerException;
import com.zigapk.weather.utils.FileUtils;

import java.util.ArrayList;

/**
 * Created by zigapk on 23.6.2016.
 */
public class CitiesListAdapter extends RecyclerView.Adapter<CitiesListAdapter.ViewHolder> {
    private ArrayList<String> cities = new ArrayList<>();

    public CitiesListAdapter(ArrayList<String> cities) {
        this.cities = cities;
    }

    public CitiesListAdapter() {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ProgressBar progressBar;
        public TextView cityName;
        public TextView temperatureInfoTv;

        public ViewHolder(View v) {
            super(v);
            view = v;
            progressBar = (ProgressBar) v.findViewById(R.id.temperatureProgressBar);
            cityName = (TextView) v.findViewById(R.id.cityNameTv);
            temperatureInfoTv = (TextView) v.findViewById(R.id.mainTemperatureInfoTv);
        }

    }

    public void addItem(int position, String city) {
        cities.add(position, city);
        notifyItemInserted(position);
    }

    public void addItem(String city) {
        addItem(cities.size(), city);
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void removeItem(int position) {
        cities.remove(position);
        notifyItemRemoved(position);
    }

    public String getCityAtPosition(int position) {
        return cities.get(position);
    }

    public void load(Context context){
        try {
            String json = FileUtils.readFile("cities.json", context);
            cities = new Gson().fromJson(json, CitiesHolder.class).cities;
        }catch (Exception e){
            System.out.println();
        }
    }

    public void save(Context context){
        try {
            String json = new Gson().toJson(new CitiesHolder(cities));
            FileUtils.writeToFile("cities.json", json, context);
        }catch (Exception e){
            System.out.println();
        }
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    @Override
    public CitiesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final City city = new City(cities.get(position));
        holder.cityName.setText(city.user_input_name);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailAct = new Intent(MainActivity.context, DetailsActivity.class);
                detailAct.putExtra("city", city);
                detailAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.context.startActivity(detailAct);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final City tempCity = city.get();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // set formated name
                            cities.set(position, tempCity.name);

                            holder.cityName.setText(tempCity.name);
                            holder.cityName.setText(tempCity.name);
                            holder.temperatureInfoTv.setText(tempCity.main.formatedTemp());
                            holder.temperatureInfoTv.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.GONE);
                            holder.view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent detailAct = new Intent(MainActivity.context,
                                            DetailsActivity.class);
                                    detailAct.putExtra("city", tempCity);
                                    detailAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MainActivity.context.startActivity(detailAct);
                                }
                            });
                        }
                    });
                } catch (CityNotFoundException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            holder.cityName.setText(MainActivity.context.getResources().getString(
                                    R.string.no_data_for, city.user_input_name));
                            holder.cityName.setTextColor(MainActivity.context.getResources().
                                    getColor(R.color.red));
                            holder.temperatureInfoTv.setText("/");
                            holder.temperatureInfoTv.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.GONE);
                            holder.view.setOnClickListener(null);
                        }
                    });
                } catch (CouldNotReachServerException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.showNoConnectionSnackbar();
                            holder.temperatureInfoTv.setText("/");
                            holder.temperatureInfoTv.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.GONE);
                            holder.view.setOnClickListener(null);
                        }
                    });
                }

            }
        }).start();
    }
}

// temporary space to save cities to json
class CitiesHolder {
    public ArrayList<String> cities = new ArrayList<>();

    public CitiesHolder(){}
    public CitiesHolder(ArrayList<String> cities){
        this.cities = cities;
    }
}