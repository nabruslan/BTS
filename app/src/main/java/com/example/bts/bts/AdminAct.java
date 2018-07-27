package com.example.bts.bts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.masstransit.Line;

import java.util.ArrayList;

public class AdminAct extends AppCompatActivity {

    DBUser dbHelper;
    SQLiteDatabase db;

    LinearLayout linearLayout;
    EditText nameOfRoute;

    ArrayList<EditText> coords;

    Button deleteRoute;
    Button saveRoute;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DBUser(getApplicationContext());
        dbHelper.create_db();
        db = dbHelper.open();

        coords = new ArrayList<EditText>();

        nameOfRoute = new EditText(this);
        nameOfRoute.setTextSize(20);
        nameOfRoute.setText("Название маршрута");

        deleteRoute = new Button(this);
        deleteRoute.setText("Удалить маршрут");
        deleteRoute.setTextSize(20);

        saveRoute = new Button(this);
        saveRoute.setText("Сохранить маршрут");
        saveRoute.setTextSize(20);

        makeSpinner();

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(spinner, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(nameOfRoute, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.addView(saveRoute, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linear.addView(deleteRoute, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.addView(linear, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setContentView(linearLayout);
    }

    private void makeSpinner() {
        spinner = new Spinner(this);
        spinner.setBackgroundColor(Color.CYAN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getNameOfRoutes(db));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                nameOfRoute.setText(item);
                addPoints(dbHelper.getRoute(db, item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void addPoints(Point[] points) {
        coords.clear();
        if(points != null)
            for(Point point : points) {
                LinearLayout linear = new LinearLayout(this);
                linear.setOrientation(LinearLayout.HORIZONTAL);
                linear.setGravity(Gravity.CENTER);

                EditText newPoint = new EditText(this);
                coords.add(newPoint);
                newPoint.setTextSize(20);
                newPoint.setText(point.getLatitude() + ", " + point.getLongitude());
                linear.addView(newPoint, new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(linear, new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
    }

    public void addCoord() {
        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.setGravity(Gravity.CENTER);

        EditText newPoint = new EditText(this);
        coords.add(newPoint);
        newPoint.setTextSize(20);
        newPoint.setText("long, lat");
        linear.addView(nameOfRoute, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linear.addView(linear, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

}
