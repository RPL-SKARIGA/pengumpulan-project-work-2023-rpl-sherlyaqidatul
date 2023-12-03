package com.example.tokokosmetik.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tokokosmetik.Adapter.CartListAdapter;
import com.example.tokokosmetik.Domain.PopularDomain;
import com.example.tokokosmetik.Helper.ChangeNumberItemsListener;
import com.example.tokokosmetik.Helper.ManagementCart;
import com.example.tokokosmetik.R;
import com.example.tokokosmetik.kosme.db_Contract;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText addressET;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;
    private TextView totalFeeTxt,deliveryTxt,totalTxt;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart=new ManagementCart(this);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> startActivity(new Intent(CartActivity.this,BerandaActivity.class)));
        Button orderBtn = findViewById(R.id.btnorder);
        orderBtn.setOnClickListener(view->order());

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("LOCATION", location.toString());
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getApplicationContext());
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            String address = addresses.get(0).getAddressLine(0);
                            Log.d("LOCATION", address);
                        }
                    }
                });
        initView();
        setVariabel();
        initlist();
        calcualteCart();
    }

    private void initlist() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        if(!managementCart.getListCart().isEmpty()){
            adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
                @Override
                public void change() {

                    calcualteCart();
                }
            });
            recyclerView.setAdapter(adapter);
            //scrollView.setVisibility(View.GONE);
        } else {
            //scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calcualteCart() {
        double percentTax=0;
        double delivery=0;
        if (managementCart.getListCart().isEmpty()) {
            totalFeeTxt.setText("Rp-");
            deliveryTxt.setText("Rp-");
            totalTxt.setText("Rp-");
        } else {
            tax = Math.round((managementCart.getTotalFee() * percentTax * 100.0)) / 100.0;
            double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
            double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;
            totalFeeTxt.setText("Rp"+(int)itemTotal);
            deliveryTxt.setText("Rp" + delivery);
            totalTxt.setText("Rp"+((int)total));
        }

    }

    private void setVariabel() {

        backBtn.setOnClickListener(view -> finish());
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerView = findViewById(R.id.view3);
        scrollView = findViewById(R.id.scrollView4);
        backBtn = findViewById(R.id.backBtn);
        spinner = findViewById(R.id.spinner);
        addressET = findViewById(R.id.addressET);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.payment_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void order(){
        String userId = getSharedPreferences("AUTH", Context.MODE_PRIVATE).getString("userid", "");
        String paymentMethod = spinner.getSelectedItem().toString();

        double percentTax=0;
        double delivery=0;
        String address = String.valueOf(((EditText) findViewById(R.id.addressET)).getText());
        if(address.isEmpty()){
            Toast.makeText(getApplicationContext(), "Alamat Harus Diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        tax = Math.round((managementCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
        JSONArray json = new JSONArray();
        for (PopularDomain item : managementCart.getListCart()) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", item.getId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                object.put("jumlahPembelian", item.getNumberinCart());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            json.put(object);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, db_Contract.urlOrder, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("gagal")){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    editor.remove("CartList");
                    editor.apply();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE", response);
                    Intent intent = new Intent(getApplicationContext(), TransaksiActivityy.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Gagal memproses pesanan", Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("RESPONSE", error.toString());
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("id_user", userId);
                params.put("payment_method", paymentMethod);
                params.put("total_payment", "" + total);
                params.put("produk", json.toString());
                params.put("alamat", address);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}