package com.example.ex20221201

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    lateinit var queue : RequestQueue
    lateinit var request: StringRequest // 받아노는 응답이 문자열입니다


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Volley : 서버 통신을 위한 라이브러리
        // Request :  내가 하고 싶은 요청이 담기는 곳/ 응답이 들어오는 곳
        // RequestQeueue : Request에 담긴 요청을 가지고 서버로 이동하는 객체

        val btnReq = findViewById<Button>(R.id.btnReq)
        val etUrl = findViewById<EditText>(R.id.etUrl)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        // 순서 request에 요청이 등록 -> 요청을 queue에 적용 -> queue가 들고
        // 서버로 이동 -> 응답을 가지고 queue가 돌아옴 -> 응답을 request에게 전달
        // -> 처리

        //btnReq를 클릭했을 때
        // etUrl에 적힌 값을 가져와서 request를 생성
        // request를 Queue에 적용

        //queue 초기화 진행
        queue = Volley.newRequestQueue(this@MainActivity)

        btnReq.setOnClickListener {
            val url = etUrl.text.toString()
            //request를 queue에 적용

            //실제 요청을 할 수 있는 객체 :Request
            // StringRequest의 매개변수 4가지
            // 1) 데이터 전송 방식(GET/POST)
            // 2) 요청할 서버 주소 (URL)
            // 3) 응답성공 했을 경우 실행시킬 코드
            // 4) 응답실패 했을 경우 실행시킬 코드
            // 3),4)은 Listener 입니다

            request = StringRequest(
                // 1)
                Request.Method.GET,
                // 2)
                url,
                // 응답에 성공했을 때 어떻게 할껀지 Listener
                {
                    response ->
                    Log.d("response", response.toString())
                    tvResult.text =response
                },
                { // 응답에 오류발생할때 어떻게 할껀지 Listener
                    error ->
                    Log.d("error",error.toString())
                    Toast.makeText(this,"error 발생", Toast.LENGTH_SHORT).show()
                }
            // RequestQueue가 받아온 응답은 response랑 error 매개변수를 통해 확인가능

            )
            // 여러번 요청을 하는 구조를 가지고 있으면 캐시가 누적된다.
            // : 캐시 저장소를 비워주는 기능 (꼭해야함)
            request.setShouldCache(false)

            queue.add(request)

            // permission 늦게 주면 접속안되니까 application 지웠다가 다시 깔아야함



        }



    }
}