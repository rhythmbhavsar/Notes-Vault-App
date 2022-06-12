package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

public class Passcode extends AppCompatActivity {

    EditText et_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        et_p=findViewById(R.id.et_p);
    }
    public void numberEvent(View view) {
        String number = et_p.getText().toString();
        switch (view.getId()){
            case R.id.tv_1:
                number += "1";
                break;
            case R.id.tv_2:
                number += "2";
                break;
            case R.id.tv_3:
                number += "3";
                break;
            case R.id.tv_4:
                number += "4";
                break;
            case R.id.tv_5:
                number += "5";
                break;
            case R.id.tv_6:
                number += "6";
                break;
            case R.id.tv_7:
                number += "7";
                break;
            case R.id.tv_8:
                number += "8";
                break;
            case R.id.tv_9:
                number += "9";
                break;
            case R.id.tv_0:
                number += "0";
                break;
        }
        et_p.setText(number);
        SharedPreferences sh = getSharedPreferences("Passcode",MODE_PRIVATE);
        String code = sh.getString("code", "");
        if (et_p.length()==4){
            final Handler handler = new Handler();
            if (number.matches(code)){
//                Toast.makeText(Passcode.this, "rgf", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Passcode.this,VaultFiles.class);
                startActivity(i);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_p.getText().clear();
                    }
                }, 300);
            }else {
                Animation shake = AnimationUtils.loadAnimation(Passcode.this, R.anim.shake_animation);
                et_p.startAnimation(shake);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_p.getText().clear();
                    }
                }, 300);
            }
        }
    }
}