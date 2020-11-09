package com.grupoess.grupoessv05

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.grupoess.grupoessv05.model.Categorias_object
import com.grupoess.grupoessv05.seleccion_producto
import com.grupoess.grupoessv05.variables.user
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.seleccion_producto.*
import org.json.JSONArray
import org.json.JSONObject


class carrito : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var arrayList:ArrayList<Productos_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carrito_activity)

        var user_data = user();
        if(user_data.get_id() == "null"){
            Toast.makeText(this, "Debe iniciar cesion primero", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, login::class.java)
            startActivityForResult(intent, 0)
        }
        else{
            traer_productos()
        }
    }

    private fun traer_productos(){
        var user_data2 = user();
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:83/traer_carrito.php"
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
                params["id_user"] = user_data2.get_id()
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

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())
            data_arraylist.add(Productos_object(data_product["id_wordpress"].toString().toInt(), data_product["id_categoria"].toString().toInt(), data_product["imagen"].toString(),data_product["name"].toString(),data_product["descripcion"].toString()))
        }

        Log.i("prueba",data.toString())
        //se toma el grid_view_contet_main
        arrayList = data_arraylist;
        gridView = findViewById(R.id.grid_view_contet_main_carrito)
        languageAdapters = LanguageAdaptersProductos(applicationContext, data_arraylist!!)
        gridView?.adapter = languageAdapters
        gridView?.onItemClickListener = this
    }


    private fun comprobar_respuesta(response: String?) {
        val respuesta = JSONObject(response);

        if(respuesta["mensaje"].toString() == "1"){
            Toast.makeText(this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
        }else if(respuesta["mensaje"].toString() == "0"){
            Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Productos_object = arrayList!!.get(position)

        /*val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("seleccion_producto/"+user.uid+"/producto_"+items.id)
            myRef.removeValue();
        }

        Toast.makeText(this, "El producto a borrar es"+items.id, Toast.LENGTH_SHORT).show()
        */

        var user_data = user();

        Log.i("Alerta",user_data.get_id())
        Log.i("Alerta",items.id.toString())

        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:82/GrupoEss/registrar_seleccion_usuario.php"
        val postRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                Log.i("Alerta",response)
                comprobar_respuesta(response);
            },
            Response.ErrorListener { // error
                Log.i("Alerta","Error al intentar cargar las variables contacte con el administrador")
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["clave"] = "R3J1cG9Fc3M"
                params["id_usuario"] = user_data.get_id()
                params["id_producto"] = items.id.toString()
                params["cantidad"] = "10"
                return params
            }
        }
        queue.add(postRequest)
    }

}