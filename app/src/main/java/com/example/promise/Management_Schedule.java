package com.example.promise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Management_Schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_schedule);

        Button btn1 = (Button) findViewById(R.id.btn_create_schedule);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Writing_Schedule.class);
                startActivity(intent);
            }
        });

        Button btn2 = (Button) findViewById(R.id.btn_check_schedule);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Checking_Schedule.class);
                startActivity(intent);
            }
        });
    }
}