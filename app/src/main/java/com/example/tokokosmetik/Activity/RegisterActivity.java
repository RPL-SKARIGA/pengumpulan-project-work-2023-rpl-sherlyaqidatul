package com.example.tokokosmetik.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tokokosmetik.R;
import com.example.tokokosmetik.kosme.db_Contract;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, username, password, alamat, notelp;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        alamat = findViewById(R.id.editTextalamat);
        notelp = findViewById(R.id.editTexttlp);
        password = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnlog);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamatText = alamat.getText().toString();
                String emailText = email.getText().toString();
                String notelpText = notelp.getText().toString();
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                if (!(usernameText.isEmpty() || alamatText.isEmpty() || emailText.isEmpty() || notelpText.isEmpty() || passwordText.isEmpty())) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, db_Contract.urlRegister, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("RESPONSE_API",response);
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected HashMap<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();

                            params.put("username", usernameText);
                            params.put("email", emailText);
                            params.put("alamat", alamatText);
                            params.put("notelp", notelpText);
                            params.put("password", passwordText);

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                } else {
                    Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}