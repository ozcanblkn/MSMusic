package com.example.msmusic

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.lang.Error

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var popimage: ImageView
    private lateinit var rockimage: ImageView
    private lateinit var hiphopimage: ImageView
    private lateinit var electronic: ImageView
    private var username: String? = null
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = this.arguments?.getString("username").toString()
        //Toast.makeText(activity, username.toString(), Toast.LENGTH_SHORT).show()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popimage = view.findViewById(R.id.popimage)
        rockimage = view.findViewById(R.id.rockimage)
        hiphopimage = view.findViewById(R.id.hiphopimage)
        electronic = view.findViewById(R.id.electronic)
        popimage.setOnClickListener(){
            replaceFragment(Home(),username.toString(),"Pop")
        }
        rockimage.setOnClickListener(){
                replaceFragment(Home(),username.toString(),"Rock")
        }
        hiphopimage.setOnClickListener(){
            replaceFragment(Home(),username.toString(),"HipHop")

        }
        electronic.setOnClickListener(){

        }



    }
    private  fun replaceFragment(fragment: Fragment,name: String,genre: String){
        val  fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
        val args = Bundle()
        args.putString("username", name)
        args.putString("genre", genre)
        fragment.arguments = args

    }

}