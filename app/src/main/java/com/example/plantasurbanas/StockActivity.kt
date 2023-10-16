package com.example.plantasurbanas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.plantasurbanas.databinding.ActivityMovimientoBinding
import com.example.plantasurbanas.databinding.ActivityStockBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class StockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.btnConsultar.setOnClickListener {
            val codigo = binding.InputCodigo.text.toString()
            if(verificarCodigo(codigo)){
                obtenerValores(codigo)
            }
        }

        binding.btnRegresar.setOnClickListener {
            finish()
        }

        binding.btnScan.setOnClickListener{
            iniciarScan()
        }

    }

    private fun iniciarScan(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.initiateScan()
    }

    private fun verificarCodigo(codigo:String):Boolean{
        if(codigo == ""){
            Toast.makeText(this, "El campo código no puede estar vacío", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun obtenerValores(value:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("Productos").whereEqualTo("Codigo", value).get().addOnSuccessListener {
            if(it.isEmpty){
                Toast.makeText(this, "No se encuentra ningún prodcuto con este codigo", Toast.LENGTH_LONG).show()
            } else{
                for (docs in it){
                    val prod = docs.getString("Producto").toString()
                    val cat = docs.getString("Categoria").toString()
                    val alm = docs.getString("Almacen").toString()
                    val med = docs.getString("Medida").toString()
                    val stock = docs.get("Stock").toString().toInt()
                    mostrarValores(prod, cat, alm, med, stock)
                }
            }
        }
    }

    private fun mostrarValores(Prod: String, Cat:String, Alm: String, Med: String, Stock:Int){
        val txtProd = findViewById<TextView>(R.id.txtProd)
        val txtCat = findViewById<TextView>(R.id.txtCat)
        val txtAlm = findViewById<TextView>(R.id.txtAlmacen)
        val txtStock = findViewById<TextView>(R.id.txtStock)
        var texto:String

        texto = "${getString(R.string.str_prod)}: $Prod"
        txtProd.text = texto

        texto = "${getString(R.string.str_cat)}: $Cat"
        txtCat.text = texto

        texto = "${getString(R.string.str_alm)}: $Alm"
        txtAlm.text = texto

        texto = "Stock: $Stock"
        txtStock.text = texto
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){
            if(result.contents == null){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                binding.InputCodigo.setText(result.contents.toString())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
