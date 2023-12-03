package com.example.tokokosmetik.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tokokosmetik.Domain.PopularDomain;
import com.example.tokokosmetik.Helper.ManagementCart;
import com.example.tokokosmetik.R;

public class DetailActivity extends AppCompatActivity {
    private Button addToCartBtn;
    private TextView titletxt, priceTxt, descriptionTxt, reviewTxt, rateTxt;
    private ImageView pictitem, backBtn;
    private PopularDomain objek;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        backBtn = findViewById(R.id.backbutton2);
        backBtn.setOnClickListener(view -> startActivity(new Intent(DetailActivity.this,BerandaActivity.class)));
        managementCart = new ManagementCart(this);
        initView();
    }


    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartbtn);
        priceTxt = findViewById(R.id.pricetxt);
        titletxt = findViewById(R.id.titletxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        pictitem = findViewById(R.id.itempict);
        reviewTxt = findViewById(R.id.riviewTxt);
        rateTxt = findViewById(R.id.scoreTxt);
        backBtn = findViewById(R.id.backBtn);

        Intent intent = getIntent();
        objek = (PopularDomain) getIntent().getSerializableExtra("object");
        priceTxt.setText("" + ((int)objek.getPrice()));
        titletxt.setText(objek.getTitle());
        reviewTxt.setText("" + objek.getReview());
        rateTxt.setText("" + objek.getScore());
        descriptionTxt.setText(objek.getDescription());

        int imageResourceId = getResources().getIdentifier(objek.getPicUrl(), "drawable", getApplicationContext().getPackageName());
        Glide.with(getApplicationContext()).load(imageResourceId).into(pictitem);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objek.getStock() > 0){
                    objek.setNumberinCart(numberOrder);
                    managementCart.insertCosmetic(objek);
                }else{
                    Toast.makeText(getApplicationContext(), "Stock Barang Tidak Tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}