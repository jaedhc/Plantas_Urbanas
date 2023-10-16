package com.example.plantasurbanas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.plantasurbanas.databinding.ActivityMovimientoBinding
import com.example.plantasurbanas.databinding.ActivityNewProductBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class MovimientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovimientoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.chkIngreso.setOnClickListener {
            if(binding.chkSalida.isChecked){
                binding.chkSalida.isChecked = false
            }
        }

        binding.chkSalida.setOnClickListener {
            if(binding.chkIngreso.isChecked){
                binding.chkIngreso.isChecked = false
            }
        }

        binding.btnConsultar.setOnClickListener {
            val codigo = binding.InputCodigo.text.toString()
            if(verificarCodigo(codigo)){
                obtenerValores(codigo)
            }
        }

        binding.btnGuardar.setOnClickListener {
            var cantidad:Int
            try{
                cantidad = binding.InputCantidad.text.toString().toInt()
            } catch (e: Exception){
                Toast.makeText(this, "El campo Cantidad no puede estar vacío", Toast.LENGTH_LONG).show()
                cantidad = 0
            }

            val codigo = binding.InputCodigo.text.toString()

            if(binding.chkSalida.isChecked){
                cantidad *= -1
            }
            if(verificarCodigo(codigo) && cantidad != 0){
                modificarStock(codigo, cantidad)
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
                        val stock = docs.getString("Stock").toString()
                        val med = docs.getString("Medida").toString()
                        mostrarValores(prod, cat, stock, med)
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

    private fun modificarStock(codigo:String, cantidad:Int){
        val db = FirebaseFirestore.getInstance()
        db.collection("Productos").whereEqualTo("Codigo", codigo)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    Toast.makeText(this, "No se encuentra ningún prodcuto con este codigo", Toast.LENGTH_LONG).show()
                } else {
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