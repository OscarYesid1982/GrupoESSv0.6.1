package com.grupoess.grupoessv05

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_main.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //Acciones Grupo Fab
        fabAbout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, 0)
        }
        icWebGrupo.setOnClickListener {
            val uri: Uri = Uri.parse("https://grupoess.com/tienda/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        icYoutubeGrupo.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.youtube.com/channel/UCm7n7YmVPjRRgM51IUJIuRg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        icFacebokGrupo.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.facebook.com/GrupoESSCol/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        icInstagramGrupo.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.instagram.com/grupoess/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        icTwitterGrupo.setOnClickListener {
            val uri: Uri = Uri.parse("https://twitter.com/ess_grupo")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

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
            R.id.Favorite -> {
                Toast.makeText(this, "Mensaje para favoritos", Toast.LENGTH_SHORT).show()
                return true
            }
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
}