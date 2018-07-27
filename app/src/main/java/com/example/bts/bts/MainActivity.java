package com.example.bts.bts;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    EditText login;
    EditText password;
    TextView log;
    TextView pass;
    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = new EditText(this);
        login.setTextSize(20);
        password = new EditText(this);
        password.setTextSize(20);
        log = new TextView(this);
        log.setText("Введите логин");
        log.setTextSize(20);
        pass = new TextView(this);
        pass.setText("Введите пароль");
        pass.setTextSize(20);
        enter = new Button(this);
        enter.setText("Войти");
        enter.setTextSize(20);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(log, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(login, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(pass, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(password, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(enter, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setContentView(linearLayout);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String LOGIN = login.getText().toString();
                String PASSWORD = password.getText().toString();
                if (LOGIN.equals("u") && PASSWORD.equals("u"))
                    chooseRoute();
                if (LOGIN.equals("d") && PASSWORD.equals("d"))
                    sendLocation();
                if(LOGIN.equals("a") && PASSWORD.equals("a"))
                    openRoutes();
            }
        });

    }

    public void chooseRoute() {
        Intent intent = new Intent(this, MapAndRoutes.class);
        startActivity(intent);
    }

    public void sendLocation() {
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }

    public void openRoutes() {
        Intent intent = new Intent(this, AdminAct.class);
        startActivity(intent);
    }

}
