package com.grupoess.grupoessv05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.grupoess.grupoessv05.R
import com.grupoess.grupoessv05.model.Productos_object
import com.grupoess.grupoessv05.variables.Seleccion
import com.grupoess.grupoessv05.adapters.LanguageAdaptersProductos
import com.grupoess.grupoessv05.model.Categorias_object
import com.grupoess.grupoessv05.seleccion_producto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.seleccion_producto.*


class carrito : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var arrayList:ArrayList<Productos_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productos)

        var context = this;
        var arrayList_2:ArrayList<Productos_object> = ArrayList()

        //se valida si el producto ya esta en el carrito de compras
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            var context = this;
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("seleccion_producto/"+user.uid)
            gridView = findViewById(R.id.grid_view_contet_main)
            arrayList = ArrayList()

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (id_prducto in dataSnapshot.children) {
                        var icons = "";
                        var name = "";
                        var descripcion = "";

                        val database2 = FirebaseDatabase.getInstance()
                        val myRef2 = database2.getReference("productos/"+id_prducto.value.toString())

                        myRef2.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (categoria in dataSnapshot.children) {
                                    if(categoria.key == "Descripcion"){descripcion =  categoria.value.toString()}
                                    if(categoria.key == "Nombre"){name =  categoria.value.toString()}
                                    if(categoria.key == "Imagen"){icons =  categoria.value.toString()}
                                }
                                arrayList_2.add(Productos_object(id_prducto.value.toString().toInt(), 0, icons, name, descripcion))

                                //se llena el array list
                                arrayList = arrayList_2;
                                languageAdapters = LanguageAdaptersProductos(applicationContext, arrayList_2!!)
                                gridView?.adapter = languageAdapters
                                gridView?.onItemClickListener = context
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                                Log.w("Alerta", "Failed to read value.", error.toException())
                            }
                        });
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("Alerta", "Failed to read value.", error.toException())
                }
            });
        }
        else{
            Toast.makeText(this, "Debe iniciar cesion primero", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Productos_object = arrayList!!.get(position)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("seleccion_producto/"+user.uid+"/producto_"+items.id)
            myRef.removeValue();
        }

        Toast.makeText(this, "El producto a borrar es"+items.id, Toast.LENGTH_SHORT).show()
    }
}