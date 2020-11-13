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
import com.grupoess.grupoessv05.variables.convertir_utd8
import com.grupoess.grupoessv05.variables.user
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.idFabFacebook
import kotlinx.android.synthetic.main.activity_main.idFabGrupoEss
import kotlinx.android.synthetic.main.activity_main.idFabInstagram
import kotlinx.android.synthetic.main.activity_main.idFabTwitter
import kotlinx.android.synthetic.main.activity_main.idFabYoutube
import kotlinx.android.synthetic.main.activity_main.textUsuario
import kotlinx.android.synthetic.main.productos.*
import org.json.JSONArray
import org.json.JSONObject


class productos : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<Productos_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productos)

        var nameUsuario = user()
        nameUsuario.get_nombre()
        textUsuarioProductos.text = nameUsuario.get_nombre()

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
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
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
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())

            if(cat.get_id_categoria() == 42){
                data_arraylist.add(Productos_object(data_product["id_wordpress"].toString().toInt(), data_product["id_categoria"].toString().toInt(), data_product["imagen"].toString(), data_utf8.get_text(data_product["name"].toString()), data_utf8.get_text(data_product["descripcion"].toString()),0 ))
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
            /* R.id.Search -> {
                 Toast.makeText(this, "Mensaje para compartir", Toast.LENGTH_SHORT).show()
                 return true
             }


             R.id.Favorite -> {
                 Toast.makeText(this, "Mensaje para favoritos", Toast.LENGTH_SHORT).show()
                 return true
             }

             */
            R.id.Car -> {
                val intent = Intent(this, carrito::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.MyProfile -> {
                val intent = Intent(this, registrarse::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.About -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Productos_object = arrayList!!.get(position)
        var cat = Seleccion();
        cat.set_id_producto(items.id!!)

        val intent = Intent(this, seleccion_producto::class.java)
        startActivityForResult(intent, 0)
    }
}