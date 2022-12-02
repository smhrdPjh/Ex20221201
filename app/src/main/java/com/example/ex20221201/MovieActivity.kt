package com.example.ex20221201

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MovieActivity : AppCompatActivity() {

    //Volley에 필요한 객체 2개
    var queue : RequestQueue?=null//요청을 가지고 서버로 이동하는 객체
    lateinit var  request: StringRequest // 요청/ 응답이 들어가는 객체
    var movies = ArrayList<MovieVO>() // 영화 정보들이 담길 배열




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        // Volley 를 통한 네트워크 통신 4단계
        // 1. Volley설정
        //   - Volley 라이브러리 추가
        //   - Manifest 에 permission추가 !! -> Internet -> application 삭제및 재시작
        // 2. RequestQueue 생성
        // 3. Request 생성
        // 4. RequestQueue 에 Request 추가!!


        val rc = findViewById<RecyclerView>(R.id.rc)
        val btnMovie = findViewById<Button>(R.id.btnMovie)
        val etInput = findViewById<EditText>(R.id.etInput)

        // btnMovie를 클릭했을 때 영화정보를 (response) Log로 확인해보자

        //버튼을 클릭하건, 에뮬레이터를 작동, OnCreate가 실행될때마다
        // request가들어갈 장소를 만들고 있음



        if(queue == null) {
            queue = Volley.newRequestQueue(this@MovieActivity)
        }
        val adapter = MovieAdapter(this,movies)
        rc.adapter= adapter
        rc.layoutManager = LinearLayoutManager(this)
        btnMovie.setOnClickListener {
            movies.clear()
            val date : String = etInput.text.toString()
            val url : String ="https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=$date\n"


           request = StringRequest(
               Request.Method.GET,
                url,

                {
                    response ->
                    //응답 받아온 response : String json타입으로 받기
                    val json1 = JSONObject(response)
                    Log.d("json1",json1.toString())
                    val json2 = json1.getJSONObject(("boxOfficeResult"))
                    Log.d("json2", json2.toString())
                    val json3 = json2.getJSONArray("dailyBoxOfficeList")
                    Log.d("json3",json3.toString())

//                    val movie = json3.getJSONObject(0)
//                    val rank = movie.getString("rank")
//                    Log.d("Rank", rank)

                    //JsonArray에 순차적으로 접근해서 영화 정보 꺼내오기
                    for (i in 0 until 10){
                        val movie = json3.getJSONObject(i)
                        //Rank
                        var rank =movie.getString("rank")
                        //RankOldAndNew
                        var rankOldAndNew = movie.getString("rankOldAndNew")
                        //movieNm
                        var movieNm = movie.getString("movieNm")
                        Log.d("영화이름",movieNm)
                        //openDt
                        var openDt = movie.getString("openDt")
                        //audiAcc
                        var audiAcc = movie.getString("audiAcc")

                        //하나의 자료형 MovieVO
                        // MovieVo를 ArrayList에 저장~
                        movies.add(MovieVO(rank,rankOldAndNew,movieNm,audiAcc,openDt))
                    }
                    adapter.notifyDataSetChanged()

                },
                {
                    error->
                }


            )
            request.setShouldCache(false)
            queue?.add(request)





        }



    }
}