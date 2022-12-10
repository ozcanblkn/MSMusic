package com.example.msmusic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private var per=0
    private val requestPermision=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        per=if(it){1}else{0}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestPermision.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        val log_button = findViewById<Button>(R.id.button_loggin)
        val reg_button = findViewById<Button>(R.id.button_sign)
        val user_name = findViewById<EditText>(R.id.user_name)
        val user_password = findViewById<EditText>(R.id.user_password)

        log_button.setOnClickListener(){
            login(user_name.text.toString(),user_password.text.toString())
        }
        reg_button.setOnClickListener(){
            register(user_name.text.toString(),user_password.text.toString())
        }



    }


    private fun login(uname: String, pass: String) {
        val json = JSONObject()
        json.put("_id", uname)
        json.put("pass", pass)
        val url = "http://10.0.2.2:5000/login"
        val queue = Volley.newRequestQueue(this)
        val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, json,
            Response.Listener { response->
               if(response.getString("_id").toString()==uname)
               {
                   val intent = Intent(this, MainActivity::class.java).putExtra("username",uname)
                   startActivity(intent)
               }
            },
            Response.ErrorListener { Toast.makeText(this, "Giriş Yapılamadı" , Toast.LENGTH_LONG).show()}) {}
        queue.add(jsonapi)

    }
    private fun register(uname: String, pass: String) {

        if(uname != "" && pass != "")
        {
            val json = JSONObject()
            json.put("_id", uname)
            json.put("pass", pass)
            val url = "http://10.0.2.2:5000/signin"
            val queue = Volley.newRequestQueue(this)
            val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, json,
                Response.Listener { response->
                    if(response.getString("message").toString() == "Registration Successfull!")
                    {
                        Toast.makeText(this, response.getString("message").toString(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java).putExtra("username",uname)
                        startActivity(intent)
                    }
                },
                Response.ErrorListener { Toast.makeText(this,it.message.toString() , Toast.LENGTH_LONG).show()}) {}
            queue.add(jsonapi)

        }
        else
        {
            Toast.makeText(this, "Tüm alanları doldurunuz! ", Toast.LENGTH_SHORT).show()
        }
        }

}

