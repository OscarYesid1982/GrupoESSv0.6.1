package com.grupoess.grupoessv05

import android.annotation.SuppressLint
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
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoessv05.adapters.LanguageAdaptersCategorias
import com.grupoess.grupoessv05.adapters.LanguageAdaptersCategorias2
import com.grupoess.grupoessv05.adapters.SliderHomeAdapter
import com.grupoess.grupoessv05.model.Categorias_object
import com.grupoess.grupoessv05.model.IntroSlide
import com.grupoess.grupoessv05.variables.Seleccion
import com.grupoess.grupoessv05.variables.convertir_utd8
import com.grupoess.grupoessv05.variables.user
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sliderhome.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.listOf
import kotlin.collections.set


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<Categorias_object> ? = null
    private var gridView:GridView ? = null
    private var gridView2:GridView ? = null
    private var languageAdapters: LanguageAdaptersCategorias? = null
    private var languageAdapters2: LanguageAdaptersCategorias2? = null


      @SuppressLint("WrongConstant")
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.toolbar))

          traer_slider()

       var nameUsuario = user()
       nameUsuario.get_nombre()
       textUsuario.text = nameUsuario.get_nombre()

      //Carga Fab
      grupoFab()

       //se traen las caegorias
       traer_categorias()
   }

    private fun traer_categorias(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:83/traer_categorias.php"
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                covertir_json(response)
            },
            Response.ErrorListener { // error
                Log.i(
                    "Alerta",
                    "Error al intentar cargar las variables contacte con el administrador"
                )
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
        var data_arraylist:ArrayList<Categorias_object> = ArrayList()
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_utf8 = convertir_utd8();

        for (i in 0 until data.length()) {
            val data_categpry = JSONObject(data.getJSONObject(i).toString())
            if(data_categpry["parent"].toString() == "38"){
                data_arraylist.add(Categorias_object( data_categpry["img"].toString(),  data_utf8.get_text(data_categpry["name"].toString()), data_categpry["id_wordpress"].toString().toInt()))
            }
        }

        //se toma el grid_view_contet_main
        arrayList = data_arraylist;
        gridView = findViewById(R.id.grid_view_contet_main)
        gridView2 = findViewById(R.id.grid_view_contet_main2)

        languageAdapters = LanguageAdaptersCategorias(applicationContext, data_arraylist!!)
        languageAdapters2 = LanguageAdaptersCategorias2(applicationContext, data_arraylist!!)
        gridView?.adapter = languageAdapters
        gridView2?.adapter = languageAdapters2
        gridView?.onItemClickListener = this
        gridView2?.onItemClickListener = this

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


    //Actividades categorias
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Categorias_object = arrayList!!.get(position)
        var cat = Seleccion();
        cat.set_id_categoria(items.id!!)

        val intent = Intent(this, productos::class.java)
        startActivityForResult(intent, 0)
    }

    private fun traer_slider(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:83/traer_slider.php"
        val postRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response -> // response
                //el texto que viene lo convertimos de string a json
                covertir_jsonSlider(response)
            },
            Response.ErrorListener { // error
                Log.i(
                    "Alerta",
                    "Error al intentar cargar las variables contacte con el administrador"
                )
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

    private fun covertir_jsonSlider(response: String?) {
        val data_ini = JSONObject(response)
        val data = JSONArray(data_ini["data"].toString())
        var data_categpry = JSONObject(data.getJSONObject(0).toString())
        var list = mutableListOf(
            IntroSlide(
                "Imagen Slider 1",
                "Descripción de la primera imagen como Slider",
                data_categpry["Imagen"].toString()
            )
        );

        for (i in 1 until data.length()) {
            data_categpry = JSONObject(data.getJSONObject(i).toString())

            list.addAll(
                listOf(
                    IntroSlide(
                        "Imagen Slider 1",
                        "Descripción de la primera imagen como Slider",
                        data_categpry["Imagen"].toString()
                    )
                )
            )
        }

        val introSliderAdapter = SliderHomeAdapter(list)

        // Config Slider Home
        introSliderViewPager2.adapter = introSliderAdapter
        // setupIndicators()
        // setCurrentIndicator(0)
        introSliderViewPager2.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //   setCurrentIndicator(position)
            }
        })


    }

    fun grupoFab(){
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

        idFabWP.setOnClickListener {
            Toast.makeText(this, "Conexion WP", Toast.LENGTH_SHORT).show()
            val launchIntent = packageManager.getLaunchIntentForPackage("com.whatsapp")
            startActivity(launchIntent)
        }
    }



}