package com.grupoess.grupoessv05

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grupoess.grupoessv05.variables.Seleccion
import com.grupoess.grupoessv05.variables.user
import kotlinx.android.synthetic.main.login.*
import org.json.JSONArray
import org.json.JSONObject


class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //se lee el usuario en cache
        var user_data = leer_user(false)
        if(user_data != "vacio"){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        else{
            var validar_pesta = Seleccion();
            if(validar_pesta.get_id_validar_pestana()){
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                validar_pesta.set_id_validar_pestana(false);
                return;
            }
            //accion de cuando le dan el click sobre el boton de logueo
            id_login_boton.setOnClickListener {
                validar_login()
            }

            //escucha el boton de registrarse
            id_login_registrar.setOnClickListener {
                val i = Intent(this, registrarse::class.java)
                startActivity(i)
            }
        }
    }

    private fun validar_login(){
        //se consulta el servicio
        var queue = Volley.newRequestQueue(this)
        var url = "https://kindrez.com:82/GrupoEss/login.php"
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
                params["correo"] = id_login_correo.text.toString()
                params["clave_user"] = id_login_clave.text.toString()
                return params
            }
        }
        queue.add(postRequest)
    }

    private fun comprobar_respuesta(response: String) {
        val respuesta = JSONObject(response);

        if(respuesta["mensaje"].toString() == "1"){
            var info = JSONArray(respuesta["data"].toString())
            guardar_data(info[0].toString())
        }else if(respuesta["mensaje"].toString() == "0"){
            Toast.makeText(this, "El usuario o la contraseña estan mal escritos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardar_data(data: String) {
        val sharpref = getPreferences(Context.MODE_WORLD_READABLE)
        val editor = sharpref.edit()
        editor.putString("user", data)
        editor.commit()
        leer_user(true);
    }

    private fun leer_user(guardar: Boolean): String? {
        val sharpref = getPreferences(Context.MODE_PRIVATE)
        val valor = sharpref.getString("user", "vacio")

        if(guardar || valor != "vacio"){
            var data_temp = JSONObject( valor )
            var user = user()
            val i = Intent(this, MainActivity::class.java)

            user.set_user( data_temp["id"].toString(), data_temp["nombre"].toString(), data_temp["apellido"].toString(), data_temp["direccion"].toString(), data_temp["telefono"].toString(), data_temp["correo"].toString(), data_temp["fecha_ultimo_ingreso"].toString())
            startActivity(i)
        }

        return valor;
    }
}