package com.example.msmusic

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject
import java.io.IOException

class PlayerActivity : AppCompatActivity() {
    private lateinit var runnable : Runnable
    private lateinit var mediaPlayer : MediaPlayer
    private var handler = Handler()
    private lateinit var imgview : ImageView
    private lateinit var songimage : ImageView
    private lateinit var btnnext : ImageView
    private lateinit var refreshbtn : ImageView
    private lateinit var shufflebtn : ImageView
    private lateinit var btnprevious : ImageView
    private lateinit var likebtn : ImageView
    private lateinit var dislikebtn : ImageView
    private lateinit var imgtitle : TextView
    private lateinit var txtstart : TextView
    private lateinit var txtstop : TextView
    private lateinit var listheight : ImageView
    private lateinit var seekbar : SeekBar
    private var position : Int = 0
    private lateinit var adapter: RecMyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsArrayList : ArrayList<News>
    private lateinit var imgurlList : ArrayList<String>
    private lateinit var songurlList : ArrayList<String>
    private lateinit var titleList : ArrayList<String>
    private lateinit var recimgurlList : ArrayList<String>
    private lateinit var recsongurlList : ArrayList<String>
    private lateinit var rectitleList : ArrayList<String>
    private lateinit var dialog : BottomSheetDialog
    private var username: String? = null
    private var score: String? = null
    private var refresh: Int? = 0
    private var mix: Int? = 0
    private var like: Int? = 0
    private var dislike: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        username = intent.getStringExtra("username").toString()
        imgurlList = intent.getStringArrayListExtra("imgurlList") as ArrayList<String>
        songurlList = intent.getStringArrayListExtra("songurlList") as ArrayList<String>
        titleList = intent.getStringArrayListExtra("titleList") as ArrayList<String>
        recimgurlList = intent.getStringArrayListExtra("recimgurlList") as ArrayList<String>
        recsongurlList = intent.getStringArrayListExtra("recsongurlList") as ArrayList<String>
        rectitleList = intent.getStringArrayListExtra("rectitleList") as ArrayList<String>
        position = intent.getIntExtra("position",0)
        newsArrayList = arrayListOf()
        imgview  = findViewById(R.id.playbtn)
        imgtitle  = findViewById(R.id.txtsn)
        shufflebtn = findViewById(R.id.mix)
        refreshbtn = findViewById(R.id.repeat)
        txtstart = findViewById(R.id.txtstart)
        txtstop = findViewById(R.id.txtstop)
        seekbar = findViewById(R.id.seekbar)
        btnnext = findViewById(R.id.btnnext)
        btnprevious = findViewById(R.id.btnprev)
        songimage = findViewById(R.id.imageview)
        likebtn = findViewById(R.id.likedbtn)
        dislikebtn = findViewById(R.id.dislikebtn)
        listheight = findViewById(R.id.reccyclerviewswipe)
        musicplay(titleList[position],songurlList[position],imgurlList[position])
        for (i in recimgurlList.indices) {
            val news = News(rectitleList[i],recimgurlList[i])
            newsArrayList.add(news)
        }
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet,null)
        dialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView = dialogView.findViewById(R.id.reccyclerview)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = RecMyAdapter(newsArrayList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : RecMyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                mediaPlayer.stop()
                var control = 0
                if(imgurlList.size != recimgurlList.size)
                {
                    imgurlList.clear()
                    imgurlList = recimgurlList
                    songurlList.clear()
                    songurlList = recsongurlList
                    titleList.clear()
                    titleList = rectitleList
                }
                else
                {
                    for (i in titleList.indices) {
                        if(titleList[i] != rectitleList[i])
                        {
                            control = 1
                        }
                    }
                    if(control != 0)
                    {
                        imgurlList.clear()
                        imgurlList = recimgurlList
                        songurlList.clear()
                        songurlList = recsongurlList
                        titleList.clear()
                        titleList = rectitleList
                    }
                }
                positionchange(position)
                musicplay(rectitleList[position],songurlList[position],imgurlList[position])
                count_song(username.toString(),titleList[position])
                dialog.dismiss()
            }
        })
        listheight.setOnClickListener(){
            dialog.show()
        }
        shufflebtn.setOnClickListener(){

            if (mix == 0)
            {
                shufflebtn.setColorFilter(resources.getColor(android.R.color.holo_red_light))
                mix = 1
            }
            else
            {
                shufflebtn.setColorFilter(resources.getColor(android.R.color.white))
                mix = 0
            }
        }
        refreshbtn.setOnClickListener(){

            if (refresh == 0)
            {
                refreshbtn.setColorFilter(resources.getColor(android.R.color.holo_red_light))
                refresh = 1
            }
            else
            {
                refreshbtn.setColorFilter(resources.getColor(android.R.color.white))
                refresh = 0
            }
        }
        likebtn.setOnClickListener(){

            if (like == 0)
            {
                likebtn.setImageResource(R.drawable.ic_baseline_thumb_up_24)
                like = 1
                like_song(username.toString(),titleList[position],"like")
            }
            else
            {
                likebtn.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                like = 0
                like_song(username.toString(),titleList[position],"dislike")
            }
        }
        dislikebtn.setOnClickListener(){

            if (dislike == 0)
            {
                dislikebtn.setImageResource(R.drawable.ic_baseline_thumb_down_24)
                dislike = 1
                like_song(username.toString(),titleList[position],"dislike")
            }
            else
            {
                dislikebtn.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                dislike = 0
                like_song(username.toString(),titleList[position],"like")
            }
        }


            imgview.setOnClickListener {
            if (mediaPlayer.isPlaying)
            {
                imgview.setImageResource(R.drawable.ic_baseline_play_24)
                mediaPlayer.pause()
            }
            else
            {
                imgview.setImageResource(R.drawable.ic_baseline_pause_24)
                mediaPlayer.start()
            }
        }

            btnnext.setOnClickListener(){
                if (position<titleList.size-1)
                {
                    if(mix==1)
                    {
                        val rnds = (1..titleList.size-1).random()
                        positionchange(rnds)
                    }
                    mediaPlayer.stop()
                    position++
                    musicplay(titleList[position],songurlList[position],imgurlList[position])
                    count_song(username.toString(),titleList[position])
                }


            }
            btnprevious.setOnClickListener(){

                if (position>0)
                {
                    if(mix==1)
                    {
                        val rnds = (1..titleList.size-1).random()
                        positionchange(rnds)
                    }
                    mediaPlayer.stop()
                    position--
                    musicplay(titleList[position],songurlList[position],imgurlList[position])
                    count_song(username.toString(),titleList[position])
                }


            }

    }
    private fun positionchange(pos : Int) {
        position = pos
    }
    @SuppressLint("SetTextI18n")
    private fun musicplay(title : String, songurl : String, imgurl : String) {
        imgtitle.text = title
        Glide.with(this).load(imgurl).error(R.drawable.ic_baseline_music_note_24).into(songimage)
        mediaPlayer = MediaPlayer()
        var durationmin : Int
        var durationsec : Int
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.setDataSource(songurl)
            mediaPlayer.prepare()
            mediaPlayer.start()
            imgview.setImageResource(R.drawable.ic_baseline_pause_24)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        durationmin = mediaPlayer.duration/1000/60
        durationsec = mediaPlayer.duration/1000%60
        if(durationsec<10)
        {
            txtstop.text = "$durationmin:0$durationsec"
        }
        else
        {
            txtstop.text = "$durationmin:$durationsec"
        }
        seekbar.progress = 0

        seekbar.max = mediaPlayer.duration
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2){
                    mediaPlayer.seekTo(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        runnable = Runnable {
            seekbar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable,1000)
            durationmin = mediaPlayer.currentPosition/1000/60
            durationsec = mediaPlayer.currentPosition/1000%60
            if(durationsec<10)
            {
                txtstart.text = "$durationmin:0$durationsec"
            }
            else
            {
                txtstart.text = "$durationmin:$durationsec"
            }

        }
        handler.postDelayed(runnable,1000)
        mediaPlayer.setOnCompletionListener {
            if(refresh==1)
            {
                mediaPlayer.stop()
                musicplay(titleList[position],songurlList[position],imgurlList[position])
                count_song(username.toString(),titleList[position])
            }
            else
            {
                if(mix == 1)
                {
                    val rnds = (1..titleList.size).random()
                    positionchange(rnds)
                    musicplay(titleList[position],songurlList[position],imgurlList[position])
                    count_song(username.toString(),titleList[position])
                }
                else
                {
                    mediaPlayer.pause()
                    imgview.setImageResource(R.drawable.ic_baseline_play_24)
                }

            }


        }



    }
    private fun count_song(uname: String, sname: String) {


        val json = JSONObject()
        json.put("musicname", sname)
        json.put("username", uname)
        val url = "http://10.0.2.2:5000/listen_count"
        val queue = Volley.newRequestQueue(this)
        val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, json,
            Response.Listener { response->

            },
            Response.ErrorListener { Toast.makeText(this,it.message.toString() , Toast.LENGTH_LONG).show()}) {}
        queue.add(jsonapi)

    }
    private fun like_song(uname: String, sname: String,decision: String) {


        val json = JSONObject()
        json.put("musicname", sname)
        json.put("username", uname)
        json.put("decision", decision)
        val url = "http://10.0.2.2:5000/liked_song"
        val queue = Volley.newRequestQueue(this)
        val jsonapi: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, json,
            Response.Listener { response->
                Toast.makeText(this, response.getString("message").toString(), Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { Toast.makeText(this,it.message.toString() , Toast.LENGTH_LONG).show()}) {}
        queue.add(jsonapi)

    }

}