package com.example.msmusic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */


class Home : Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: MyAdapter
    private lateinit var likeadapter: LikeMyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var likerecyclerView: RecyclerView
    private lateinit var newsArrayList : ArrayList<News>
    private lateinit var recimgurlList : ArrayList<String>
    private lateinit var recsongurlList : ArrayList<String>
    private lateinit var rectitleList : ArrayList<String>
    private lateinit var homerecimgurlList : ArrayList<String>
    private lateinit var homerecsongurlList : ArrayList<String>
    private lateinit var homerectitleList : ArrayList<String>
    private lateinit var likeimgurlList : ArrayList<String>
    private lateinit var likesongurlList : ArrayList<String>
    private lateinit var liketitleList : ArrayList<String>
    private var username: String? = null
    private var genre: String? = null





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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = this.arguments?.getString("username")
        genre = this.arguments?.getString("genre")
        recommendation(username.toString(),"",view,0)
        music_array(username.toString(),view)
    }
    private fun music_array(username: String,view: View) {
        val json = JSONObject()
        val url = "http://10.0.2.2:5000/musiclist"
        json.put("username", username)
        val queue = Volley.newRequestQueue(activity)
        val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, json,
            Response.Listener { response->

                newsArrayList = arrayListOf<News>()
                likeimgurlList = ArrayList<String>()
                likesongurlList = ArrayList<String>()
                liketitleList = ArrayList<String>()
                val values =
                    response.getString("likeimgurl").substring(0, response.getString("likeimgurl").length - 1)
                val items: List<String> = values.split(",")
                for (item in items)
                    likeimgurlList.add(item)

                val values2 =
                    response.getString("liketitle").substring(0, response.getString("liketitle").length - 1)
                val items2: List<String> = values2.split(",")
                for (item2 in items2)
                    liketitleList.add(item2)

                val values3 =
                    response.getString("likesongurl").substring(0, response.getString("likesongurl").length - 1)
                val items3: List<String> = values3.split(",")
                for (item3 in items3)
                    likesongurlList.add(item3)

                for (i in likeimgurlList.indices) {
                    val news = News(liketitleList[i],likeimgurlList[i])
                    newsArrayList.add(news)
                }
                val layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                likerecyclerView = view.findViewById(R.id.likerecyclerview)
                likerecyclerView.layoutManager = layoutManager
                likerecyclerView.setHasFixedSize(true)
                likeadapter = LikeMyAdapter(newsArrayList)
                likerecyclerView.adapter = likeadapter
                likeadapter.setOnItemClickListener(object : LikeMyAdapter.onItemClickListener{

                    override fun onItemClick(position: Int) {
                        likerecommendation(username,liketitleList[0],position)

                    }
                })



            },
            Response.ErrorListener { Toast.makeText(activity, "Başarısız" , Toast.LENGTH_LONG).show()}) {}
        queue.add(jsonapi)



    }
    private fun recommendation(username: String,musicname: String,view: View,position: Int) {
        val json = JSONObject()
        json.put("username", username)
        json.put("musicname", musicname)
        val url = "http://10.0.2.2:5000/recommendation"
        val queue = Volley.newRequestQueue(activity)
        val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, json,
            Response.Listener { response->
                if(musicname=="")
                {
                    newsArrayList = arrayListOf<News>()
                    homerecimgurlList = ArrayList<String>()
                    homerecsongurlList = ArrayList<String>()
                    homerectitleList = ArrayList<String>()
                    val values =
                        response.getString("homerecimgurl").substring(0, response.getString("homerecimgurl").length - 1)
                    val items: List<String> = values.split(",")
                    for (item in items)
                        homerecimgurlList.add(item)

                    val values2 =
                        response.getString("homerectitle").substring(0, response.getString("homerectitle").length - 1)
                    val items2: List<String> = values2.split(",")
                    for (item2 in items2)
                        homerectitleList.add(item2)

                    val values3 =
                        response.getString("homerecsongurl").substring(0, response.getString("homerecsongurl").length - 1)
                    val items3: List<String> = values3.split(",")
                    for (item3 in items3)
                        homerecsongurlList.add(item3)

                    for (i in homerecimgurlList.indices) {
                        val news = News(homerectitleList[i],homerecimgurlList[i])
                        newsArrayList.add(news)
                    }
                    val layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                    recyclerView = view.findViewById(R.id.recyclerview)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.setHasFixedSize(true)
                    adapter = MyAdapter(newsArrayList)
                    recyclerView.adapter = adapter
                }
                else
                {
                    newsArrayList = arrayListOf<News>()
                    recimgurlList = ArrayList<String>()
                    recsongurlList = ArrayList<String>()
                    rectitleList = ArrayList<String>()
                    val values =
                        response.getString("recimgurl").substring(0, response.getString("recimgurl").length - 1)
                    val items: List<String> = values.split(",")
                    for (item in items)
                        recimgurlList.add(item)

                    val values2 =
                        response.getString("rectitle").substring(0, response.getString("rectitle").length - 1)
                    val items2: List<String> = values2.split(",")
                    for (item2 in items2)
                        rectitleList.add(item2)

                    val values3 =
                        response.getString("recsongurl").substring(0, response.getString("recsongurl").length - 1)
                    val items3: List<String> = values3.split(",")
                    for (item3 in items3)
                        recsongurlList.add(item3)
                    activity?.let{
                        val intent = Intent (it, PlayerActivity::class.java).putExtra("username",username)
                            .putExtra("imgurlList",homerecimgurlList).putExtra("songurlList",homerecsongurlList)
                            .putExtra("titleList",homerectitleList).putExtra("recimgurlList",recimgurlList)
                            .putExtra("recsongurlList",recsongurlList).putExtra("rectitleList",rectitleList)
                            .putExtra("position",position)
                        it.startActivity(intent)
                        }

                }
                adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{

                    override fun onItemClick(position: Int) {

                        activity?.let{
                            recommendation(username,homerectitleList[position],view,position)
                        }
                    }
                })



            },
            Response.ErrorListener { Toast.makeText(activity, it.message.toString() , Toast.LENGTH_LONG).show()}) {}
        queue.add(jsonapi)

    }
    private fun likerecommendation(username: String, musicname: String,position: Int) {


        val json = JSONObject()
        json.put("username", username)
        json.put("musicname", musicname)
        val url = "http://10.0.2.2:5000/recommendation"
        val queue = Volley.newRequestQueue(activity)
        val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, json,
            Response.Listener { response->
                recimgurlList = ArrayList<String>()
                recsongurlList = ArrayList<String>()
                rectitleList = ArrayList<String>()
                val values =
                    response.getString("recimgurl").substring(0, response.getString("recimgurl").length - 1)
                val items: List<String> = values.split(",")
                for (item in items)
                    recimgurlList.add(item)

                val values2 =
                    response.getString("rectitle").substring(0, response.getString("rectitle").length - 1)
                val items2: List<String> = values2.split(",")
                for (item2 in items2)
                    rectitleList.add(item2)

                val values3 =
                    response.getString("recsongurl").substring(0, response.getString("recsongurl").length - 1)
                val items3: List<String> = values3.split(",")
                for (item3 in items3)
                    recsongurlList.add(item3)
                activity?.let{
                    val intent = Intent (it, PlayerActivity::class.java).putExtra("username",username)
                        .putExtra("imgurlList",likeimgurlList).putExtra("songurlList",likesongurlList)
                        .putExtra("titleList",liketitleList).putExtra("recimgurlList",recimgurlList)
                        .putExtra("recsongurlList",recsongurlList).putExtra("rectitleList",rectitleList)
                        .putExtra("position",position)
                    it.startActivity(intent)
                }
            },
            Response.ErrorListener {}) {}
        queue.add(jsonapi)

    }
}

