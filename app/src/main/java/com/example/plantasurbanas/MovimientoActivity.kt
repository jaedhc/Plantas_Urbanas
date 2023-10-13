package com.example.plantasurbanas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class MovimientoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)

        init()
    }

    private fun init(){

        val chkIngreso = findViewById<CheckBox>(R.id.chkIngreso)
        val chkSalida = findViewById<CheckBox>(R.id.chkSalida)
        val btnConsultar = findViewById<Button>(R.id.btn_consultar)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar)

        val txtCodigo = findViewById<TextInputEditText>(R.id.InputCodigo)
        val txtCantidad = findViewById<TextInputEditText>(R.id.InputCantidad)

        chkIngreso.setOnClickListener {
            if(chkSalida.isChecked){
                chkSalida.isChecked = false
            }
        }

        chkSalida.setOnClickListener {
            if(chkIngreso.isChecked){
                chkIngreso.isChecked = false
            }
        }

        btnConsultar.setOnClickListener {
            val codigo = txtCodigo.text.toString()
            if(verificarCodigo(codigo)){
                obtenerValores(codigo)
            }
        }

        btnGuardar.setOnClickListener {
            var cantidad = txtCantidad.text.toString().toInt()
            val codigo = txtCodigo.text.toString()

            if(chkSalida.isChecked){
                cantidad *= -1
            }
            if(verificarCodigo(codigo)){
                obtenerStock(codigo, cantidad)
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
                        mostrarValores(prod, cat, alm, med)
                    }
                }
            }
    }

    private fun mostrarValores(Prod: String, Cat:String, Alm: String, Med: String){
        val txtProd = findViewById<TextView>(R.id.txtProd)
        val txtCat = findViewById<TextView>(R.id.txtCat)
        val txtAlm = findViewById<TextView>(R.id.txtAlmacen)
        val txtMed = findViewById<TextView>(R.id.txtMedida)
        var texto:String

        texto = "${getString(R.string.str_prod)}: $Prod"
        txtProd.text = texto

        texto = "${getString(R.string.str_cat)}: $Cat"
        txtCat.text = texto

        texto = "${getString(R.string.str_alm)}: $Alm"
        txtAlm.text = texto

        texto = "${getString(R.string.str_med)}: $Med"
        txtMed.text = texto
    }

    private fun obtenerStock(codigo:String, cantidad:Int){
        val db = FirebaseFirestore.getInstance()
        db.collection("Productos").whereEqualTo("Codigo", codigo)
            .get()
            .addOnSuccessListener {
                for(docs in it){
                    val stock = docs.get("Stock").toString().toInt()
                    val nuevoStock = stock + cantidad
                    if(nuevoStock >= 0){
                        val hashMap = hashMapOf(
                            "Stock" to nuevoStock
                        )
                        db.collection("Productos")
                            .document(docs.id).update(hashMap as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Stock modificado correctamente", Toast.LENGTH_LONG).show()
                            }
                    }else {
                        Toast.makeText(this, "No tiene suficiente en stock para retirar", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

}