package com.example.bts.bts;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.driving.DrivingOptions;
import com.yandex.mapkit.driving.DrivingRoute;
import com.yandex.mapkit.driving.DrivingRouter;
import com.yandex.mapkit.driving.DrivingSession;
import com.yandex.mapkit.driving.RequestPoint;
import com.yandex.mapkit.driving.RequestPointType;
import com.yandex.mapkit.geometry.Circle;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CircleMapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import java.util.ArrayList;
import java.util.List;

public class MapAndRoutes extends Activity implements DrivingSession.DrivingRouteListener {

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private DBUser dbHelper;
    private SQLiteDatabase db;

    private CircleMapObject circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("170d6066-72a5-4e13-8e8c-6533761c16e9");
        MapKitFactory.initialize(this);

        dbHelper = new DBUser(getApplicationContext());
        dbHelper.create_db();
        db = dbHelper.open();

        setContentView(R.layout.activity_map_and_routes);
        mapView = (MapView)findViewById(R.id.mapview);
        drivingRouter = MapKitFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        makeSpinner();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onDrivingRoutes(List<DrivingRoute> routes) {
        for (DrivingRoute route : routes) {
            mapObjects.addPolyline(route.getGeometry());
        }
    }

    @Override
    public void onDrivingRoutesError(Error error) {

    }

    private void submitRequest(Point[] points, Point loc) {
        if(mapObjects != null)
            mapObjects.clear();
        DrivingOptions options = new DrivingOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        if(points != null) {
            for (Point point : points) {
                requestPoints.add(new RequestPoint(
                        point, new ArrayList<Point>(), RequestPointType.WAYPOINT));
            }
            drivingSession = drivingRouter.requestRoutes(requestPoints, options, this);
        }
        if(loc == null) {
            loc = points[0];
        }
        else {
            circle = mapObjects.addCircle(new Circle(loc, 10), Color.BLACK, 2, Color.DKGRAY);
            circle.setZIndex(100.0f);
        }

        mapView.getMap().move(new CameraPosition(loc, 16.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
    }

    private void makeSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.routes);
        spinner.setBackgroundColor(Color.CYAN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getNameOfRoutes(db));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                submitRequest(dbHelper.getRoute(db, item), dbHelper.getLocation(db, item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}
