package com.grupoess.grupoessv05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.grupoess.grupoessv05.variables.Seleccion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.seleccion_producto.*


class seleccion_producto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_producto)
        var cat = Seleccion();
        var context = this;

        val database2 = FirebaseDatabase.getInstance()
        val myRef2 = database2.getReference("productos/"+cat.get_id_producto())

        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (array in dataSnapshot.children) {
                    //se recorre el nombre y la categoria
                    if(array.key == "Nombre"){seleccion_producto_id_titulo.text =array.value.toString()}
                    if(array.key == "Descripcion"){seleccion_producto_id_descripcion.text = array.value.toString()}
                    if(array.key == "Imagen"){Picasso.get().load(array.value.toString()).into(seleccion_producto_id_imagen);}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Alerta", "Failed to read value.", error.toException())
            }
        });

        seleccion_producto_id_compra.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()

                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("seleccion_producto/"+user.uid+"/producto_"+cat.get_id_producto())
                myRef.setValue(cat.get_id_producto())

                val intent = Intent(context, MainActivity::class.java)
                startActivityForResult(intent, 0)
            }else{
                Toast.makeText(this, "Debe iniciar cesion primero", Toast.LENGTH_SHORT).show()
                val i = Intent(this, login::class.java)
                startActivity(i)

            }
        }
    }
    //Opciones Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Search -> {
                Toast.makeText(this, "Mensaje para compartir", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.Car -> {
                Toast.makeText(this, "Mensaje para compartir", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.MyProfile -> {
                Toast.makeText(this, "Mensaje para compartir", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.About -> {
                Toast.makeText(this, "Mensaje para compartir", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}