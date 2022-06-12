package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPassword extends AppCompatActivity {

    EditText et_pass, et_cpass;
    Button btn_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        et_pass= findViewById(R.id.et_pass);
        et_cpass= findViewById(R.id.et_cpass);
        btn_set= findViewById(R.id.btn_set);


        SharedPreferences sharedPreferences = getSharedPreferences("Passcode",MODE_PRIVATE);

        SharedPreferences.Editor ed = sharedPreferences.edit();

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = et_pass.getText().toString();
                String cpwd = et_cpass.getText().toString();
                if (et_cpass.getText().length() == 4){
                    if (et_cpass.getText().toString().matches(et_pass.getText().toString())){
                        ed.putString("code", pwd);
                        ed.putBoolean("isSet", true);
                        ed.commit();
                        Toast.makeText(SetPassword.this, "Passcode Set", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SetPassword.this, VaultFiles.class);
                        startActivity(i);
                    }
                    else {
                        et_cpass.setError("Passcode Does not matched");
                        et_cpass.setText(null);
                    }
                }else {
                    et_pass.setError("Password must be of 4 digits");
                }
            }
        });
    }
}