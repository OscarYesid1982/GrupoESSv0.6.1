package com.grupoess.grupoessv05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.grupoess.grupoessv05.variables.Seleccion
import com.grupoess.grupoessv05.variables.convertir_utd8
import com.grupoess.grupoessv05.variables.user
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.productos.*
import kotlinx.android.synthetic.main.seleccion_producto.*
import org.json.JSONArray
import org.json.JSONObject


class seleccion_producto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_producto)

        var nameUsuario = user()
        nameUsuario.get_nombre()
        textUsuarioSeleccionProducto.text = nameUsuario.get_nombre()

        var cat = Seleccion();
        traer_producto_seleccionado(cat.get_id_producto())


        seleccion_producto_id_compra.setOnClickListener {
            //se lee el usuario en cache
            var user_data = user();
            if(user_data.get_id() == "null"){
                val intent = Intent(this, login::class.java)
                startActivityForResult(intent, 0)
            }
            else{
                var cat = Seleccion();
                //se consulta el servicio
                var queue = Volley.newRequestQueue(this)
                var url = "https://kindrez.com:82/GrupoEss/registrar_seleccion_usuario.php"
                val postRequest: StringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener { response -> // response
                        //el texto que viene lo convertimos de string a json
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
                        params["id_producto"] = cat.get_id_producto().toString()
                        params["cantidad"] = "10"
                        return params
                    }
                }
                queue.add(postRequest)
            }
        }


    }
    private fun comprobar_respuesta(response: String?) {
        val respuesta = JSONObject(response);

        if(respuesta["mensaje"].toString() == "1"){
            Toast.makeText(this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
        }else if(respuesta["mensaje"].toString() == "0"){
            Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
        }
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

    private fun traer_producto_seleccionado(id: Int){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:83/traer_producto_seleccion.php"
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
                params["id"] = id.toString()
                return params
            }
        }
        queue.add(postRequest)
    }

    private fun covertir_json(response: String) {
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_product = JSONObject(data.getJSONObject(i).toString())

            seleccion_producto_id_titulo.text = data_utf8.get_text( data_product["name"].toString() )
            seleccion_producto_id_descripcion.text = data_utf8.get_text( data_product["descripcion"].toString() )
            Picasso.get().load( data_product["imagen"].toString() ).into(seleccion_producto_id_imagen)
            seleccion_producto_valor_Producto.text = data_utf8.get_text("$ " + data_product["precio"].toString())
        }

    }
}