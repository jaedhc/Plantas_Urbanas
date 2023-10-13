package com.example.plantasurbanas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class StockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        init()
    }

    private fun init(){
        val txtCodigo = findViewById<TextInputEditText>(R.id.InputCodigo)

        val btnConsultar = findViewById<Button>(R.id.btn_consultar)

        btnConsultar.setOnClickListener {
            val codigo = txtCodigo.text.toString()
            if(verificarCodigo(codigo)){
                obtenerValores(codigo)
            }
        }

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

}
