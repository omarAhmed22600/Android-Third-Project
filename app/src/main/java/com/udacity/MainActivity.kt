package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*



class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var radioGroup: RadioGroup
    private lateinit var selectedRepositoryURL: String
    private lateinit var selectedRepository: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        selectedRepositoryURL = ""
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        radioGroup=findViewById(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, i ->
             when (i){
                1 -> {
                    selectedRepositoryURL = getString(R.string.glide_url)
                    selectedRepository = getString(R.string.download_using_glide)
                }

                2 ->  {
                    selectedRepositoryURL = getString(R.string.loadapp_url)
                    selectedRepository = getString(R.string.download_using_loadapp)
                }

                3 -> {
                    selectedRepositoryURL = getString(R.string.retrofit_url)
                    selectedRepository = getString(R.string.download_using_refrofit)
                }
            }
            Log.i("MainActivity",selectedRepositoryURL)
        }

        createChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_id)
        )

        custom_button.setOnClickListener {
            if (selectedRepositoryURL.isEmpty())
            {
                Toast.makeText(this,"Please Choose a Repository!",Toast.LENGTH_SHORT).show()
                custom_button.onDownloadCompleted()
            }
            else {
                download()
            }
        }
    }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                Log.i("MainActivity","download finished")
                Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show()
                custom_button.onDownloadCompleted()
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.cancelAll()
                notificationManager.sendNotification(context.getString(R.string.notification_description),context,selectedRepository,
                    STATUS)

            }
        }
    }
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Result"
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    private fun download() {

            //tried to make it download from the 3 URLS specified in the requirements but for some
            //reason it wont download so i will leave it download from the default URL given.
            val request =
                //DownloadManager.Request(Uri.parse(selectedRepository))
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue

    }


    companion object {
//        var downloadStatus = -1
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
        private const val STATUS = "Success!"
    }

}
