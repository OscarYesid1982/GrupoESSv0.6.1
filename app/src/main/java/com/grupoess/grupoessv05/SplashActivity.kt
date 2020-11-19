package com.grupoess.grupoessv05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.grupoess.grupoessv05.adapters.IntroSliderAdapter
import com.grupoess.grupoessv05.model.IntroSlide
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    /*
    private val introSliderAdapter = IntroSliderAdapter(
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
        setContentView(R.layout.activity_splash)

        introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }

        }
        )
        //escucha el boton de registrarse
        textSkipIntro.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
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
}