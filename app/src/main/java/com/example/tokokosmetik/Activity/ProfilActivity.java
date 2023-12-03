package com.example.tokokosmetik.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tokokosmetik.R;

public class ProfilActivity extends AppCompatActivity {

    private EditText email, username, password, alamat, notelp;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        alamat = findViewById(R.id.editTextalamat);
        notelp = findViewById(R.id.editTexttlp);
        password = findViewById(R.id.editTextPassword);
        btnUpdate = findViewById(R.id.btnlog);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamatText = alamat.getText().toString();
                String emailText = email.getText().toString();
                String notelpText = notelp.getText().toString();
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
            }
        });
    }
}
