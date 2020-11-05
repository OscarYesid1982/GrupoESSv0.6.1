package com.grupoess.grupoessv05

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.grupoess.grupoessv05.seleccion_producto
import kotlinx.android.synthetic.main.activity_main.*


class productos : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<Productos_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productos)
//        setSupportActionBar(findViewById(R.id.toolbar))

      //Acciones Grupo Fab
        idFabYoutube.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.youtube.com/channel/UCm7n7YmVPjRRgM51IUJIuRg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        idFabFacebook.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.facebook.com/GrupoESSCol/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        idFabInstagram.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.instagram.com/grupoess/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        idFabTwitter.setOnClickListener {
            val uri: Uri = Uri.parse("https://twitter.com/ess_grupo")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        idFabGrupoEss.setOnClickListener {
            val uri: Uri = Uri.parse("https://grupoess.com/tienda/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


        var context = this;
        var arrayList_2:ArrayList<Productos_object> = ArrayList()
        //se declara la variable de firbases y se llama a categorias
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("productos")
        //se trae la clase que guardo la seleccion de la categoria
        var cat = Seleccion();


        gridView = findViewById(R.id.grid_view_contet_main)
        arrayList = ArrayList()

        //se llama el resultado de la consulta
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (array in dataSnapshot.children) {
                    var icons = "";
                    var name = "";
                    var descripcion = "";
                    var id= array.key.toString().toInt();
                    var id_categoria = 0;

                    //se recorre el nombre y la categoria
                    for (categoria in array.children) {
                        if(categoria.key == "Descripcion"){descripcion =  categoria.value.toString()}
                        if(categoria.key == "Id_categoria"){id_categoria =  categoria.value.toString().toInt()}
                        if(categoria.key == "Nombre"){name =  categoria.value.toString()}
                        if(categoria.key == "Imagen"){icons =  categoria.value.toString()}
                    }
                    //se hace el filtro de la categoria seleccionada
                    if(cat.get_id_categoria() == id_categoria){
                        arrayList_2.add(Productos_object(id, id_categoria, icons, name, descripcion))
                    }
                }

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
            R.id.Favorite -> {
                Toast.makeText(this, "Mensaje para favoritos", Toast.LENGTH_SHORT).show()
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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Productos_object = arrayList!!.get(position)
        var cat = Seleccion();
        cat.set_id_producto(items.id!!)

        //se valida si el producto ya esta en el carrito de compras
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            var context = this;
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("seleccion_producto/"+user.uid+"/producto_"+items.id)

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if( dataSnapshot.value.toString() == items.id.toString() ){
                        Toast.makeText(context, "Este producto ya esta en el carrito de compras", Toast.LENGTH_SHORT).show()
                        return
                    }
                    else{
                        val intent = Intent(context, seleccion_producto::class.java)
                        startActivityForResult(intent, 0)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("Alerta", "Failed to read value.", error.toException())
                }
            });
        }
    }
}