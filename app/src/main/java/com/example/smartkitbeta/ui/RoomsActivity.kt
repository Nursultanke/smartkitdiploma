package com.example.smartkitbeta.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.smartkitbeta.R
import com.example.smartkitbeta.services.SensorService
import com.example.smartkitbeta.services.SensorsInstance
import com.example.smartkitbeta.services.SensorsItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_rooms.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomsActivity : AppCompatActivity() {

    lateinit var mainHandler: Handler
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    private val updateLayoutTask = object : Runnable {
        override fun run() {
            getRequestWithQueryParameters()
            mainHandler.postDelayed(this, 5000)
        }
    }

    private  lateinit var retService : SensorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        mainHandler = Handler(Looper.getMainLooper())
        retService = SensorsInstance
            .getRetrofitInstance()
            .create(SensorService::class.java)
        lampBtnInit()
        socketBtnInit()
    }

    private fun  getRequestWithQueryParameters(){

        val responseLiveData: LiveData<Response<SensorsItem>> = liveData {
            val response: Response<SensorsItem> = retService.getSensors()
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val result = "lamp = ${it.body()?.lamp.toString()}\n" +
                    "socket = ${it.body()?.socket.toString()}\n"+
                    "temperatura = ${it.body()?.temperatura.toString()}\n"+
                    "humidity = ${it.body()?.humidity.toString()}\n"+
                    "gas = ${it.body()?.gas.toString()}\n"+
                    "smoke = ${it.body()?.smoke.toString()}\n\n"


            Log.e("LampTag", it.body().toString())
            if (it.body()?.lamp == 0 ) {
                lampImageId.setImageResource(R.drawable.lamppassive)
                lampTextId.text = "Чырак өчук"
                lampTextId.setTextColor(Color.GRAY)
            }
            else {
                lampImageId.setImageResource(R.drawable.lampactive)
                lampTextId.text = "Чырак ачык"
                lampTextId.setTextColor(Color.BLACK)
            }
            //socket's
            if (it.body()?.socket == 0 ) {
                sockedImageId.setImageResource(R.drawable.socketpassive)
                sockedTextId.text = "ачык"
                sockedTextId.setTextColor(Color.GRAY)
            }
            else {
                sockedImageId.setImageResource(R.drawable.socketactive)
                sockedTextId.text = "өчук"
                sockedTextId.setTextColor(Color.BLACK)
            }
            //temperatura

//                tempTextId.text = it.body()?.temperatura.toString()

            //gas
            if (it.body()?.gas == 0 ) {
                gasImageId.setImageResource(R.drawable.gaspassive)
                gasTextId.text = "Жок"
                gasTextId.setTextColor(Color.GRAY)
            }
            else {
                gasImageId.setImageResource(R.drawable.gasactive)
                gasTextId.text = "Бар"
                gasTextId.setTextColor(Color.BLACK)
            }
            //smoke
            if (it.body()?.smoke == 0 ) {
                fireImageId.setImageResource(R.drawable.firepassive)
                fireTextId.text = "Өрт жок"
                fireTextId.setTextColor(Color.GRAY)
            }
            else {
                fireImageId.setImageResource(R.drawable.fireactive)
                fireTextId.text = "Өрт бар"
                fireTextId.setTextColor(Color.BLACK)
            }
            if (it.body()?.motion == 0 ) {
                moveImageId.setImageResource(R.drawable.movingpassive)
                moveTextId.text = "Жок"
                moveTextId.setTextColor(Color.GRAY)
            }
            else {
                moveImageId.setImageResource(R.drawable.movingactive)
                moveTextId.text = "Бар"
                moveTextId.setTextColor(Color.BLACK)
            }


        })
    }

    private fun updateSocketInit(){

        val responseLiveData:LiveData<Response<SensorsItem>> = liveData {
            val response: Response<SensorsItem> = retService.getSensors()
            emit(response)
        }

        responseLiveData.observe(this, Observer {

            if (it.body()?.socket == 0 ) {
                sockedImageId.setImageResource(R.drawable.socketactive)
                sockedTextId.text = "Socket ачык"
                sockedTextId.setTextColor(Color.GRAY)
                val requestCall = retService.updateSocket(1)
                requestCall.enqueue(object : Callback<SensorsItem> {
                    override fun onFailure(call: Call<SensorsItem>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<SensorsItem>, response: Response<SensorsItem>) {
                    }
                })
            }
            else{
                sockedImageId.setImageResource(R.drawable.socketpassive)
                sockedTextId.text = "Socket очук"
                sockedTextId.setTextColor(Color.BLACK)
                val requestCall = retService.updateSocket(0)
                requestCall.enqueue(object : Callback<SensorsItem> {
                    override fun onFailure(call: Call<SensorsItem>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<SensorsItem>, response: Response<SensorsItem>) {
                    }

                })
            }
        })

    }

    private fun updateInit() {
        val responseLiveData:LiveData<Response<SensorsItem>> = liveData {
            val response: Response<SensorsItem> = retService.getSensors()
            emit(response)
        }

        responseLiveData.observe(this, Observer {

            if (it.body()?.lamp == 0 ) {
                lampImageId.setImageResource(R.drawable.lampactive)
                lampTextId.text = "Чырак ачык"
                lampTextId.setTextColor(Color.GRAY)
                val requestCall = retService.updateSensors(1)
                requestCall.enqueue(object : Callback<SensorsItem> {
                    override fun onFailure(call: Call<SensorsItem>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<SensorsItem>, response: Response<SensorsItem>) {
                    }
                })
            }
            else{
                lampImageId.setImageResource(R.drawable.lamppassive)
                lampTextId.text = "Чырак очук"
                lampTextId.setTextColor(Color.BLACK)
                val requestCall = retService.updateSensors(0)
                requestCall.enqueue(object : Callback<SensorsItem> {
                    override fun onFailure(call: Call<SensorsItem>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<SensorsItem>, response: Response<SensorsItem>) {
                    }

                })
            }
        })
    }


    private fun socketBtnInit(){
        sockedId.setOnClickListener {
            updateSocketInit()
        }
    }

    private fun lampBtnInit(){
        lampId.setOnClickListener {
            updateInit()
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateLayoutTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateLayoutTask)
    }

}
