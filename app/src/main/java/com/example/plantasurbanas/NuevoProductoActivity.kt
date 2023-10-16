package com.example.plantasurbanas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.example.plantasurbanas.databinding.ActivityNewProductBinding

class NuevoProductoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNewProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScan.setOnClickListener{
            iniciarScan()
        }

        binding.btnRegresar.setOnClickListener {
            finish()
        }

        binding.btnGuardar.setOnClickListener {
            if(validarCampos()){
                val hashMap = obtenerValores()
                subirValores(hashMap)
            }
        }

    }

    private fun iniciarScan(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.initiateScan()
    }

    private fun validarCampos():Boolean{
        return if(binding.InputAlm.text.toString() == "" ||
            binding.InputCat.text.toString() == "" ||
            binding.InputProd.text.toString() == "" ||
            binding.InputMed.text.toString() == "" ||
            binding.InputCodigo.text.toString() == ""){
            Toast.makeText(this, "Todos los campos deben ser rellenados", Toast.LENGTH_LONG).show()
            false
        } else {
            true
        }
    }

    private fun obtenerValores():Map<String, Any>{
        val codigo = binding.InputCodigo.text.toString()
        val prod = binding.InputProd.text.toString()
        val cat = binding.InputCat.text.toString()
        val alm = binding.InputAlm.text.toString()
        val med = binding.InputMed.text.toString()
        val stock = binding.InputAlm.text.toString()

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

    private fun borrarValroes(){
        binding.InputCodigo.setText("")
        binding.InputAlm.setText("")
        binding.InputCat.setText("")
        binding.InputMed.setText("")
        binding.InputProd.setText("")
    }

    private fun subirValores(HashMap:Map<String, Any>){
        val db = FirebaseFirestore.getInstance()
        db.collection("Productos").document().set(HashMap).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this, "Producto guardado con éxito", Toast.LENGTH_LONG).show()
                binding.txtError.text = " "
                borrarValroes()
            }else{
                binding.txtError.text = it.exception.toString()
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