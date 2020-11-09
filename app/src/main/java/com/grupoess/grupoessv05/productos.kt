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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
import org.json.JSONArray
import org.json.JSONObject


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
        //traer las productos
        traer_productos()
    }

    private fun traer_productos(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:83/traer_productos.php"
        val postRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                covertir_json(response)
            },
            Response.ErrorListener { // error
                Log.i("Alerta","Error al intentar cargar las variables contacte con el administrador")
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["clave"] = "R3J1cG9Fc3M"
                return params
            }
        }
        queue.add(postRequest)
    }

    private fun covertir_json(response: String) {
        var data_arraylist:ArrayList<Productos_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var cat = Seleccion();
        ;

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())
            if(cat.get_id_categoria() == 83 && (data_product["id_wordpress"].toString().toInt() == 16171 || data_product["id_wordpress"].toString().toInt() == 15342 || data_product["id_wordpress"].toString().toInt() == 15352 || data_product["id_wordpress"].toString().toInt() == 15401 || data_product["id_wordpress"].toString().toInt() == 15413 || data_product["id_wordpress"].toString().toInt() == 16169 || data_product["id_wordpress"].toString().toInt() == 16171)){
                data_arraylist.add(Productos_object(data_product["id_wordpress"].toString().toInt(), data_product["id_categoria"].toString().toInt(), data_product["imagen"].toString(),data_product["name"].toString(),data_product["descripcion"].toString()))
            }
            if(cat.get_id_categoria() == 42 && (data_product["id_wordpress"].toString().toInt() == 15365 || data_product["id_wordpress"].toString().toInt() == 15365)){
                data_arraylist.add(Productos_object(data_product["id_wordpress"].toString().toInt(), data_product["id_categoria"].toString().toInt(), data_product["imagen"].toString(),data_product["name"].toString(),data_product["descripcion"].toString()))
            }
        }

        //se toma el grid_view_contet_main
        arrayList = data_arraylist;
        gridView = findViewById(R.id.grid_view_contet_main_Productos)
        languageAdapters = LanguageAdaptersProductos(applicationContext, data_arraylist!!)
        gridView?.adapter = languageAdapters
        gridView?.onItemClickListener = this

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