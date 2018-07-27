package com.example.bts.bts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.yandex.mapkit.geometry.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLData;
import java.util.ArrayList;

/**
 * Created by Руслан on 28.05.2018.
 */

public class DBUser extends SQLiteOpenHelper {

    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "routesDB.db";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "routes"; // название таблицы в бд
    // названия столбцов
    static final String COLUMN_NUM = "numInRoute";
    static final String COLUMN_LONG = "long";
    static final String COLUMN_LAT = "lat";
    static final String COLUMN_NAME = "nameOfRoute";
    static final String COLUMN_TYPE = "type";

    private Context myContext;

    public DBUser(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    void create_db(){

        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if(!file.exists()) {
                this.getReadableDatabase();
                myInput = myContext.getAssets().open(DB_NAME);
                String outFileName = DB_PATH;

                myOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }

        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public String[] getNameOfRoutes(SQLiteDatabase db) {
        //получаем данные из бд в виде курсора
        Cursor userCursor =  db.rawQuery("select distinct " + COLUMN_NAME + " from "+ TABLE, null);

        ArrayList<String> names = new ArrayList<String>();

        if(userCursor.moveToFirst()) {
            do {
                names.add(userCursor.getString(0));
            } while(userCursor.moveToNext());
        }
        else return null;

        return names.toArray(new String[names.size()]);
    }

    public Point[] getRoute(SQLiteDatabase db, String nameOfRoute) {
        Cursor userCursor = db.rawQuery("select long, lat " + "from " + TABLE +
                " where nameOfRoute='" + nameOfRoute + "'"
                + " and type=0", null);

        ArrayList<Point> points = new ArrayList<Point>();

        if(userCursor.moveToFirst()) {
            do {
                points.add(new Point(userCursor.getDouble(0), userCursor.getDouble(1)));
            } while(userCursor.moveToNext());
        }
        else return null;

        return points.toArray(new Point[points.size()]);
    }

    public void insertLocation(SQLiteDatabase db, String nameOfRoute, Double lon, Double lat, int type, int num) {
        Cursor userCursor = db.rawQuery("select long, lat " + "from " + TABLE +
                " where nameOfRoute='" + nameOfRoute + "'"
                + " and type=" + type, null);

        ContentValues cv = new ContentValues();
        cv.put("long", lon);
        cv.put("lat", lat);
        cv.put("nameOfRoute", nameOfRoute);
        cv.put("type", type);
        cv.put("numInRoute", num);

        if(userCursor.moveToFirst())
            db.update(TABLE, cv, "nameOfRoute='" + nameOfRoute + "' AND " + "type=" + type, null);
        else
            db.insert(TABLE, null, cv);
    }

    public Point getLocation(SQLiteDatabase db, String nameOfRoute) {
        Cursor userCursor = db.rawQuery("select long, lat " + "from " + TABLE +
                " where nameOfRoute='" + nameOfRoute + "'"
                + " and type=1", null);
        if(userCursor.moveToFirst())
            return new Point(userCursor.getDouble(0), userCursor.getDouble(1));
        return null;
    }

}
