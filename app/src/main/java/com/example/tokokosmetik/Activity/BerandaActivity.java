package com.example.tokokosmetik.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tokokosmetik.Adapter.PopularListAdapter;
import com.example.tokokosmetik.Domain.PopularDomain;
import com.example.tokokosmetik.R;
import com.example.tokokosmetik.kosme.db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BerandaActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        EditText searchET = findViewById(R.id.editTextText);

        searchET.addTextChangedListener(new TextWatcher() {
        @Override

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            initRecyclerview(String.valueOf(searchET.getText()));
        }
        });
        initRecyclerview("");
        bottom_navigation();
    }

    private void bottom_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);

        homeBtn.setOnClickListener(view -> startActivity(new Intent(BerandaActivity.this,BerandaActivity.class)));
        cartBtn.setOnClickListener(view -> startActivity(new Intent(BerandaActivity.this,CartActivity.class)));
    }

    private void initRecyclerview(String search) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest StringRequest = new StringRequest(Request.Method.GET, db_Contract.urlProduk + "?search="+search, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("RESPONSE_API", response);
                    JSONArray json = new JSONArray(response);
                    ArrayList<PopularDomain> items = new ArrayList<>();
                    for (int i = 0; i<json.length(); i++){
                        JSONObject produk = json.getJSONObject(i);
                        int id = produk.getInt("id");
                        String title = produk.getString("title");
                        int review = produk.getInt("review");
                        int stock = produk.getInt("stock");
                        int score = produk.getInt("score");
                        int price = produk.getInt("price");
                        String description  = produk.getString("description");
                        String picUrl = produk.getString("picUrl");
                        PopularDomain domain = new PopularDomain(id, title, description, picUrl, review, score, price, stock);
                        items.add(domain);
                    }
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                    recyclerViewPopular=findViewById(R.id.view1);
                    recyclerViewPopular.setLayoutManager(mLayoutManager);
                    adapterPopular=new PopularListAdapter(items);
                    recyclerViewPopular.setAdapter(adapterPopular);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Memuat Produk", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(StringRequest);
        ArrayList<PopularDomain> items=new ArrayList<>();

    }
}