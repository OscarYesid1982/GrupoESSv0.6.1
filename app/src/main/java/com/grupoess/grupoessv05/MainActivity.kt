package com.grupoess.grupoessv05

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.grupoess.grupoessv05.adapters.LanguageAdaptersCategorias
import com.grupoess.grupoessv05.adapters.SliderHomeAdapter
import com.grupoess.grupoessv05.model.Categorias_object
import com.grupoess.grupoessv05.model.IntroSlide
import com.grupoess.grupoessv05.variables.Seleccion
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<Categorias_object> ? = null
    private var gridView:GridView ? = null
    private var languageAdapters: LanguageAdaptersCategorias? = null


    private val sliderHomePager = SliderHomeAdapter(
        listOf(
            IntroSlide(
                "Imagen Slider 1",
                "Descripción de la primera imagen como Slider",
                R.drawable.slider1
            ),
            IntroSlide(
                "Imagen Slider 2",
                "Descripción de la segunda imagen como Slider",
                R.drawable.slider2
            ),
            IntroSlide(
                "Imagen Slider 3",
                "Descripción de la tercera imagen como Slider",
                R.drawable.slider3
            ),
            IntroSlide(
                "Imagen Slider 4",
                "Descripción de la cuarta imagen como Slider",
                R.drawable.slider4
            )
        )
    )

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

/*  Config Slider Home
       sliderHomePager2.adapter = sliderHomePager
       setupIndicators()
       setCurrentIndicator2(0)
       sliderHomePager2.registerOnPageChangeCallback(object:
           ViewPager2.OnPageChangeCallback() {

           override fun onPageSelected(position: Int) {
               super.onPageSelected(position)
               setCurrentIndicator2(position)
           }

       }
       )

 */

       //Acciones Grupo Fab
        idFabYoutube.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.youtube.com/user/CristianDavidHenao?sub_confirmation=1")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)


        }



       //Carga de articulos

        var context = this;
        var arrayList_2:ArrayList<Categorias_object> = ArrayList()
        //se declara la variable de firbases y se llama a categorias
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("categorias")

        //se toma el grid_view_contet_main
        gridView = findViewById(R.id.grid_view_contet_main)

        //se llama el resultado de la consulta
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (array in dataSnapshot.children) {
                    var id = array.key.toString().toInt();
                    var Nombre = "";
                    var icon = "";
                    //se recorre el nombre y la categoria
                    for (categoria in array.children) {
                        if (categoria.key == "Nombre") {
                            Nombre = categoria.value.toString()
                        }
                        if (categoria.key == "Imagen") {
                            icon = categoria.value.toString()
                        }
                    }
                    //se guarda el resultado en el array a publicar
                    arrayList_2.add(Categorias_object(icon, Nombre, id))
                }

                //se llena el array list
                arrayList = arrayList_2;
                languageAdapters = LanguageAdaptersCategorias(applicationContext, arrayList_2!!)
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


    //Actividades categorias
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: Categorias_object = arrayList!!.get(position)
        var cat = Seleccion();
        cat.set_id_categoria(items.id!!)

        val intent = Intent(this, productos::class.java)
        startActivityForResult(intent, 0)
    }

    //Slider

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(sliderHomePager.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(0, 0, 0, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }
    private fun setCurrentIndicator2(index: Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until  childCount){
            val imageView = indicatorsContainer.get(i) as ImageView
            if (i == index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.indicator_active
                    )
                )
            }else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.indicator_inactive
                    )
                )
            }
        }
    }


}