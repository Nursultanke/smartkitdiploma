package com.example.smartkitbeta

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RemoteViews
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartkitbeta.adapters.Room_adapter
import com.example.smartkitbeta.menu.AboutActivity
import com.example.smartkitbeta.models.RoomItems
import com.example.smartkitbeta.menu.ProfileActivity
import com.example.smartkitbeta.menu.SettingsActivity
import com.example.smartkitbeta.services.SensorService
import com.example.smartkitbeta.services.SensorsInstance
import com.example.smartkitbeta.services.SensorsItem
import com.example.smartkitbeta.ui.RoomsActivity
import com.example.smartkitbeta.ui.notActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_rooms.*
import retrofit2.Response

class MainActivity : AppCompatActivity(), Room_adapter.OnRoomClickListener {

    lateinit var mainHandler: Handler
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val updateTextTask = object : Runnable {
        override fun run() {
            getRequestWithQueryParameters()
            mainHandler.postDelayed(this, 5000)

        }
    }

    private val description = "Test notification"
    private  lateinit var retService : SensorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val roomList = generateList()

        //notInit()

        mainHandler = Handler(Looper.getMainLooper())
        getRequestWithQueryParameters()

        retService = SensorsInstance
            .getRetrofitInstance()
            .create(SensorService::class.java)

        room_recyclerView.adapter = Room_adapter(roomList, this)
        room_recyclerView.layoutManager = LinearLayoutManager(this)
        room_recyclerView.setHasFixedSize(true)
    }

    private fun  generateList(): List<RoomItems>{
        val list = ArrayList<RoomItems>()
        list.add(RoomItems(R.drawable.zal, "Зал"))
        list.add(RoomItems(R.drawable.kitchen, "Кухня"))

        return list
    }

    override fun onRoomClick(position: Int) {
        startActivity(Intent(this, RoomsActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.profile_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_menu -> {
                startActivity(Intent(this,
                    ProfileActivity::class.java))
                true
            }

            R.id.settings_menu -> {
                startActivity(Intent(this,
                    SettingsActivity::class.java))
                true
            }

            R.id.about_us_menu -> {
                startActivity(Intent(this,
                    AboutActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun notInit(){
        val btn = findViewById<Button>(R.id.buttonnotificatoin)

        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(this, notActivity::class.java)

            val pendingIntent = PendingIntent.getActivity(this,
                0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val contentView = RemoteViews(packageName,
                R.layout.activity_not)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(
                    channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this,channelId)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(this.resources,
                            R.drawable.ic_launcher_background))
                    .setContentIntent(pendingIntent)
            }else{

                builder = Notification.Builder(this)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(this.resources,
                        R.drawable.ic_launcher_background))
                    .setContentIntent(pendingIntent)
                    .setSound(Settings.System.DEFAULT_RINGTONE_URI)
            }
            notificationManager.notify(1234,builder.build())
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

            //gaz
            if (it.body()?.gas == 0 ) {
                gasMainImageId.setImageResource(R.drawable.gaspassive)
                gasMainTextId.text = "Газ"
                gasMainTextId.setTextColor(Color.GRAY)
            }
            else {
                gasMainImageId.setImageResource(R.drawable.gasactive)
                gasMainTextId.text = "Газ агуусу"
                gasMainTextId.setTextColor(Color.BLACK)
                notInit()
            }
            //temp
            tempTextId.text = it.body()?.temperatura.toString()
            //
            if (it.body()?.smoke == 0 ) {
                fireMainImageId.setImageResource(R.drawable.firepassive)
                fireMainTextId.text = "Өрт жок"
                fireMainTextId.setTextColor(Color.GRAY)
            }
            else {
                fireMainImageId.setImageResource(R.drawable.fireactive)
                fireMainTextId.text = "Өрт чыгуусу"
                fireMainTextId.setTextColor(Color.BLACK)
                notInit()
            }
            //humidity
            himiTextId.text = it.body()?.humidity.toString()
            //moving
            if (it.body()?.motion == 0 ) {
                moveMainImageId.setImageResource(R.drawable.movingpassive)
                moveMainTextId.text = "Жок"
                moveMainTextId.setTextColor(Color.GRAY)
            }
            else {
                moveMainImageId.setImageResource(R.drawable.movingactive)
                moveMainTextId.text = "Бар"
                moveMainTextId.setTextColor(Color.BLACK)
            }

        })
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.post(updateTextTask)
    }

    override fun onStop() {
        super.onStop()
        mainHandler.post(updateTextTask)
    }

}
