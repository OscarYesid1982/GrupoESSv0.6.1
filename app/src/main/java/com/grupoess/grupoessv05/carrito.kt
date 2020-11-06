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

        var context = this;
        var arrayList_2:ArrayList<Productos_object> = ArrayList()
        var contador = 0;

        //se valida si el producto ya esta en el carrito de compras
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            var context = this;
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("seleccion_producto/"+user.uid)

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.children.count() == 0){
                        Toast.makeText(context, "No tiene productos en el carrito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivityForResult(intent, 0)
                    }

                    for (id_prducto in dataSnapshot.children) {

                        var queue = Volley.newRequestQueue(context)
                        var url = "https://kindrez.com:83/traer_productos.php"
                        val postRequest: StringRequest = object : StringRequest(
                            Request.Method.POST, url,
                            Response.Listener { response -> // response
                                //el texto que viene lo convertimos de string a json
                                val data_ini = JSONObject(response)
                                val data = JSONArray(data_ini["data"].toString())

                                for (i in 0 until data.length()) {
                                    val data_product = JSONObject(data.getJSONObject(i).toString())
                                    if(id_prducto.value.toString() == data_product["id_wordpress"].toString()){
                                        arrayList_2.add(Productos_object(data_product["id_wordpress"].toString().toInt(), data_product["id_categoria"].toString().toInt(), data_product["imagen"].toString(),data_product["name"].toString(),data_product["descripcion"].toString()))
                                    }

                                    if(data.length() == (i+1)){
                                        contador +=1;
                                        if(contador == dataSnapshot.children.count()){
                                            //se toma el grid_view_contet_main
                                            arrayList = arrayList_2;
                                            gridView = findViewById(R.id.grid_view_contet_main_carrito)
                                            languageAdapters = LanguageAdaptersProductos(applicationContext, arrayList_2!!)
                                            gridView?.adapter = languageAdapters
                                            gridView?.onItemClickListener = context
                                        }
                                    }
                                }
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