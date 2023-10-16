package com.example.plantasurbanas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.plantasurbanas.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init (){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.btnNewprod.setOnClickListener{
            callNewProduct()
        }

        binding.btnMov.setOnClickListener {
            callMovimiento()
        }

        binding.btnStock.setOnClickListener {
            callStock()
        }

        binding.btnExit.setOnClickListener{
            finish()
        }

    }

    private fun callNewProduct(){
        val activityNewProd = Intent(this, NuevoProductoActivity::class.java)
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