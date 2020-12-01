package com.grupoess.grupoessv05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.registrarse.*
import org.json.JSONObject

class registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrarse)

        //se llama la clase que contiene las alertas
        var v_alertas = alertas();

        //se escucha el evento click del boton registrar
        id_registrarse_boton.setOnClickListener {
            Toast.makeText(this,"prueba boton registrarse",Toast.LENGTH_SHORT).show()

            if(id_registrate_clave.length() < 7){
                //validacion
                v_alertas.mensaje("Alerta","La clave debe tener mínimo 7 caracteres","Aceptar",this)
            }
            else if(id_registrate_clave.text.toString() != id_registrate_clave2.text.toString()){
                //validacion
                v_alertas.mensaje("Alerta","Las claves no coinciden","Aceptar",this)
            }
            else if(id_registrate_clave.text.toString() == "" || id_registrate_correo.text.toString() == "" ){
                //valicacion
                v_alertas.mensaje("Alerta","Debe llenar todos los campos","Aceptar",this)
            }
            else{
                //se consulta el servicio
                var queue = Volley.newRequestQueue(this)
                var url = "https://kindrez.com:82/GrupoEss/registrar_usuario.php"
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
                        params["nombre"] = id_registrate_nombre.text.toString()
                        params["apellido"] = id_registrate_apellidos.text.toString()
                        params["direccion"] = id_registrate_direccion.text.toString()
                        params["telefono"] = id_registrate_telefono.text.toString()
                        params["correo"] = id_registrate_correo.text.toString()
                        params["clave_user"] = id_registrate_clave.text.toString()
                        return params
                    }
                }
                queue.add(postRequest)
            }
        }
/*
        //se escucha el evento click del boton cancelar
        id_registrate_ingresar.setOnClickListener {
            val i = Intent(this, login::class.java)
            startActivity(i)
        }

 */
    }

    private fun comprobar_respuesta(response: String) {
        val respuesta = JSONObject(response);
        val i = Intent(this, login::class.java)
        Toast.makeText(this,"Mensaje de entrada",Toast.LENGTH_SHORT).show()
        Toast.makeText(this, respuesta["mensaje"].toString(), Toast.LENGTH_SHORT).show()
        startActivity(i)
    }
}