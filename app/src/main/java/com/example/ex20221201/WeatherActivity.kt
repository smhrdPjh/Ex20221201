package com.example.ex20221201

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val rvWeather = findViewById<RecyclerView>(R.id.rvWeather)
        val btnWeather = findViewById<Button>(R.id.btnWeather)
        val weatherList = ArrayList<WeatherVO>()
        // Volley 활용한 네트워크 통신
        // 1. Volley 라이브러리 설정
        // - 라이브러리, 추가, 인터넷 권한 , http 열기
        // 2. RequestQueue 생성

        // 없을때만 생성 해야함
        val requestQueue = Volley.newRequestQueue(this)


        // 3. Request 생성

        val adapter = WeatherAdapter(this,weatherList)

        rvWeather.adapter=adapter
        rvWeather.layoutManager=LinearLayoutManager(this)

        btnWeather.setOnClickListener {

            val cityList = ArrayList<String>()
            cityList.add("Gwangju")
            cityList.add("Seoul")
            cityList.add("Jeju-do")
            cityList.add("London")
            cityList.add("New York")
            cityList.add("Madrid")
            cityList.add("Beijing")

            val requestList = ArrayList<StringRequest>()
            for (i in 0 until cityList.size){
                val url : String = "https://api.openweathermap.org/data/2.5/weather?q=${cityList.get(i)}\n" +
                        "&appid=260b9603daf30bb221d849552e988f5d"

                val request = StringRequest(
                    Method.GET,
                    url,
                    {
                    response ->
                        Log.d("날씨$i", response)

                        val result = JSONObject(response)
                        val weathers = result.getJSONArray("weather")
                        val weather= weathers.get(0) as JSONObject//down casting
                        val state = weather.getString("main")

                        val main = result.getJSONObject("main")
                        val temp = main.getString("temp")
                        val humidity = main.getString("humidity")

                        var wind = result.getJSONObject("wind")
                        var speed = wind.getString("speed")

                     /*   val name = result.getJSONObject("name")
                        val cityName = name.getString("name")*/

                        Log.d("현재날씨$i","상태 : $state, 온도:$temp,습도$humidity, 속도$speed")
                        weatherList.add(WeatherVO(cityList.get(i), state, temp, humidity, speed))
                        adapter.notifyDataSetChanged()
                    },
                    {
                        error->
                    }
                )
                requestList.add(request)
            }

        for(i in 0 until requestList.size){
            requestQueue.add(requestList.get(i))
        }

            //7개라서 응답이 안온상태에서 새로고침하면 응답을 전부 못받을 수 있음

            //1초동안 코드 지연
          //  Thread.sleep(1000)




        }


    }
}