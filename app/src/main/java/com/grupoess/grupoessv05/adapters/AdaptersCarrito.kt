package com.grupoess.grupoessv05.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.grupoess.grupoessv05.R
import com.grupoess.grupoessv05.model.Productos_object
import com.squareup.picasso.Picasso

class AdaptersCarrito(var context: Context, var arrayList: ArrayList<Productos_object>):BaseAdapter() {

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var view: View = View.inflate(context, R.layout.card_view_carrito, null)
        //var icons: ImageView = view.findViewById(R.id.Imageview_Producto)
        var names: TextView = view.findViewById(R.id.NombreProducto)

        //var descrip: TextView = view.findViewById(R.id.descripcionCorta)
        var precio: TextView = view.findViewById(R.id.valorTotal)

        var listItem: Productos_object = arrayList.get(position)

        //Picasso.get().load(listItem.icons).into(icons)
        //descrip.text = listItem.descripcion
        names.text = listItem.name
        precio.text = "$" + listItem.precio.toString()

        //cantidades seleccionadas
        //Cargar el precio total= cantidades seleccionadas por el usuario



        return view
    }
}