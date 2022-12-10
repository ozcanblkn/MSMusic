package com.example.msmusic


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.msmusic.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username:String = intent.getStringExtra("username").toString()
        replaceFragment(Home(),username)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(Home(),username)
                R.id.profile -> replaceFragment(Profile(),username)
                R.id.settings -> replaceFragment(Settings(),username)
                else ->{
                }
            }
            true
        }

        }


private  fun replaceFragment(fragment: Fragment,name: String) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.frame_layout, fragment)
    fragmentTransaction.commit()
    val args = Bundle()
    args.putString("username", name)
    fragment.arguments = args
}
}


