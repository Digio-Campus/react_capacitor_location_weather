package com.react_capacitor_location_weather;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFetch {
    private static final String BASE_URL = "https://api.open-meteo.com/";
    private final WeatherApiFetch weatherApiFetch;
    private final Context context;

    public WeatherFetch(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApiFetch = retrofit.create(WeatherApiFetch.class);
    }

    public void fetchWeather(double latitude, double longitude) {
        Call<WeatherResponse> call = weatherApiFetch.getWeather(latitude, longitude, "temperature_2m");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    double temperature = weatherResponse.getCurrent().getTemp();
                        createNotificationChannel();
                        showNotification(latitude, longitude, temperature);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WeatherChannel";
            String description = "Channel for weather alert";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("weatherChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification(double latitude, double longitude, double temperature) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "weatherChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Weather Alert")
                .setContentText("Temperature is: " + temperature + " degrees Celsius in latitude: " + latitude + " and longitude: " + longitude)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(0, builder.build());
    }
}