package com.example.plantasurbanas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class NewProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        init()
    }

    private fun init (){
        val btnGuardar = findViewById<Button>(R.id.btn_guardar)

        btnGuardar.setOnClickListener {
            val hashMap = obtenerValores()
            subirValores(hashMap)
        }
    }

    private fun obtenerValores():Map<String, Any>{
        val codigo = findViewById<TextInputEditText>(R.id.BoxCodigo).text.toString()
        val prod = findViewById<TextInputEditText>(R.id.InputProd).text.toString()
        val cat = findViewById<TextInputEditText>(R.id.InputCat).text.toString()
        val alm = findViewById<TextInputEditText>(R.id.InputAlm).text.toString()
        val med = findViewById<TextInputEditText>(R.id.InputMed).text.toString()
        val stock = 0

        val hashMap = hashMapOf(
            "Codigo" to codigo,
            "Producto" to prod,
            "Categoria" to cat,
            "Almacen" to alm,
            "Medida" to med,
            "Stock" to stock
        )

        return hashMap
    }

    private fun subirValores(HashMap:Map<String, Any>){
        val db = FirebaseFirestore.getInstance()
        val txtError = findViewById<TextView>(R.id.txtError)
        db.collection("Productos").document().set(HashMap).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this, "Documento generado con éxito", Toast.LENGTH_LONG).show()
                txtError.text = " "
            }else{
                txtError.text = it.exception.toString()
            }
        }
    }
}