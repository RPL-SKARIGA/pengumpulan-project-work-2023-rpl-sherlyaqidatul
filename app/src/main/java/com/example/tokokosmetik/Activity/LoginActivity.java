package com.example.tokokosmetik.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tokokosmetik.R;
import com.example.tokokosmetik.kosme.db_Contract;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button btnlog;
    TextView textview4;
    int btn = R.id.btnlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        btnlog = (Button) findViewById(R.id.btnlog);
        textview4 = (TextView) findViewById(R.id.textView4);
        textview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextUsername = username.getText().toString();
                String editTextPassword = password.getText().toString();

                if (!(editTextUsername.isEmpty() || editTextPassword.isEmpty())) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest StringRequest = new StringRequest(Request.Method.GET, db_Contract.urlLogin + "?username=" + editTextUsername + "&password=" + editTextPassword, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE_API",response);
                            if (!response.equals("0")) {
                                SharedPreferences.Editor editor = getSharedPreferences("AUTH", Context.MODE_PRIVATE).edit();
                                editor.putString("userid", response);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), BerandaActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(StringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Password atau Username salah", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}