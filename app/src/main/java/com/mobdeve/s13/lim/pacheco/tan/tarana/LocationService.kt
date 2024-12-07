package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Priority

class LocationService: Service(){

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    private val MAX_WAIT_TIME = UPDATE_INTERVAL_IN_MILLISECONDS * 3

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(){
        super.onCreate()
        isRunning=true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest=LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS)
            .setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
            .build()

        locationCallback=object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult){
                super.onLocationResult(locationResult)
                for(location in locationResult.locations){
                    sendLocationUpdate(location)
                }
            }
        }
        startLocationUpdates()
    }

    private fun startLocationUpdates(){
        try{
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
        catch(e: SecurityException){
            e.printStackTrace()
        }
    }

    private fun sendLocationUpdate(location: android.location.Location){
        DBHelper.saveLocation(location.latitude, location.longitude)
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, "LocationService")
            .setContentTitle("Tarana")
            .setContentText("Location Service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(1, notification)
        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Location Service"
            val descriptionText = "Location Service is running"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("LocationService", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        var isRunning: Boolean= false
    }
}