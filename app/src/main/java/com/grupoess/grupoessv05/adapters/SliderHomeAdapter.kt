package com.grupoess.grupoessv05.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grupoess.grupoessv05.R
import com.grupoess.grupoessv05.model.IntroSlide
import com.squareup.picasso.Picasso

class SliderHomeAdapter (private val introSlides: List<IntroSlide>):
        RecyclerView.Adapter<SliderHomeAdapter.IntroSlideViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
            return  IntroSlideViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.slider_item_home, parent, false)
            )
        }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }

        override fun getItemCount(): Int {
            return introSlides.size
        }

        inner class IntroSlideViewHolder(view: View): RecyclerView.ViewHolder(view){

            private  val textTittle = view.findViewById<TextView>(R.id.textTittle)
            private  val textDescription = view.findViewById<TextView>(R.id.textDescription)
            private  val imageIcon = view.findViewById<ImageView>(R.id.imageSlideIcon)

            fun bind (introSlide: IntroSlide){
               // textTittle.text = introSlide.tittle
               // textDescription.text = introSlide.description
               // imageIcon.setImageResource(introSlide.icon)
               // Picasso.get().load(introSlide.icon).into(imageIcon)

                val base64Image: String = introSlide.icon.split(",").get(1)
                val imageAsBytes = Base64.decode(base64Image.toByteArray(), Base64.DEFAULT)
                imageIcon.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0,imageAsBytes.size))
            }
        }
}

