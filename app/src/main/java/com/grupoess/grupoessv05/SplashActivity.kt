package com.grupoess.grupoessv05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoessv05.adapters.IntroSliderAdapter
import com.grupoess.grupoessv05.adapters.SliderHomeAdapter
import com.grupoess.grupoessv05.model.IntroSlide
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.sliderhome.*
import org.json.JSONArray
import org.json.JSONObject


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //escucha el boton de registrarse
        textSkipIntro.setOnClickListener {
            val i = Intent(this, login::class.java)
            startActivity(i)
        }

/*
        introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        /*
        introSliderViewPager.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }

        }
        )

         */

    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
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
    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until  childCount){
            val imageView = indicatorsContainer.get(i) as ImageView
            if (i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext, R.drawable.indicator_active
                ))
            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext, R.drawable.indicator_inactive
                ))
            }
        }
    }

 */

        traer_slider()
    }



    private fun traer_slider() {
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:83/traer_slider.php"
        val postRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
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

        val introSliderAdapter = IntroSliderAdapter(list)

        // Config Slider Home
        introSliderViewPager.adapter = introSliderAdapter
        // setupIndicators()
        // setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //   setCurrentIndicator(position)
            }
        })


    }

}
