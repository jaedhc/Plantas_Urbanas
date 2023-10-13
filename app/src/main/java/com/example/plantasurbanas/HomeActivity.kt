package com.example.plantasurbanas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init (){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val btnNewProductActivity = findViewById<Button>(R.id.btn_newprod)
        val btnNewMovimiento = findViewById<Button>(R.id.btn_mov)
        val btnStock = findViewById<Button>(R.id.btn_stock)

        btnNewProductActivity.setOnClickListener{
            callNewProduct()
        }

        btnNewMovimiento.setOnClickListener {
            callMovimiento()
        }

        btnStock.setOnClickListener {
            callStock()
        }

    }

    private fun callNewProduct(){
        val activityNewProd = Intent(this, NewProductActivity::class.java)
        startActivity(activityNewProd)
    }

    private fun callMovimiento(){
        val activityMovimiento = Intent(this, MovimientoActivity::class.java)
        startActivity(activityMovimiento)
    }

    private fun callStock(){
        val activityStock = Intent(this, StockActivity::class.java)
        startActivity(activityStock)
    }
}