package com.example.tokokosmetik.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tokokosmetik.R;
import com.example.tokokosmetik.kosme.db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class TransaksiActivityy extends AppCompatActivity {

    TextView tanggal, id_pembeli, payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        tanggal = (TextView) findViewById(R.id.tanggal);
        id_pembeli = (TextView) findViewById(R.id.id);
        payment = (TextView) findViewById(R.id.payment);
        initData();

    }

    private void initData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest StringRequest = new StringRequest(Request.Method.GET, db_Contract.urlReport+"?id=33", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject order = new JSONObject(response);
                    tanggal.setText(order.getString("date"));
                    id_pembeli.setText(order.getString("username"));
                    payment.setText(order.getString("payment_method"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Memuat data", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(StringRequest);

    }
}
