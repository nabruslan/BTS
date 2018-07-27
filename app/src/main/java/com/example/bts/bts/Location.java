package com.example.bts.bts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Location extends AppCompatActivity {

    LinearLayout linearLayout;
    EditText nameOfRoute;
    Button send;
    TextView lon;
    TextView lat;
    LocationManager manager;
    SQLiteDatabase db;
    DBUser dbHelper;
    Boolean sending;

    public LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            if (location!=null) {
                lat.setText("Широта: " + String.valueOf(location.getLatitude()));
                lon.setText("Долгота: " + String.valueOf(location.getLongitude()));
                if (sending)
                    dbHelper.insertLocation(db, nameOfRoute.getText().toString(), location.getLongitude(), location.getLatitude(), 1, 0);
            }
            else{
                lon.setText("Долгота: ОШИБКА!");
                lat.setText("Широта: ОШИБКА!");
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        sending = false;
        nameOfRoute = new EditText(this);
        nameOfRoute.setTextSize(20);
        send = new Button(this);
        send.setText("Отправить");
        send.setTextSize(20);
        lon = new TextView(this);
        lon.setText("Долгота: ");
        lon.setTextSize(20);
        lat = new TextView(this);
        lat.setText("Широта: ");
        lat.setTextSize(20);

        dbHelper = new DBUser(getApplicationContext());
        dbHelper.create_db();
        db = dbHelper.open();

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(nameOfRoute, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(send, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(lon, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(lat, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setContentView(linearLayout);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sending = true;
            }
        });

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            lon.setText("Ошибка регистрации местоположения");
            lat.setText("");
            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, listener);
    }

}
