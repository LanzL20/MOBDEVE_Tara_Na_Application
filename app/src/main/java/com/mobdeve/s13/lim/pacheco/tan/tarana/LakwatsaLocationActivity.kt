package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.kotlin.place
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaLocationBinding
import kotlinx.coroutines.launch
import java.util.Locale

class LakwatsaLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val FINE_PERMISSION_CODE=1
    private var currentLocation:Location?=null
    private var fusedLocationProviderClient: FusedLocationProviderClient?=null

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLakwatsaLocationBinding

    private var handler=Handler()
    private var runnable:Runnable?=null
    private val LOCATION_UPDATE_INTERVAL:Long=9000

    private lateinit var lakwatsa:Lakwatsa

    private var user: User= UserSession.getUser()

    private var userMarkers= mutableMapOf<String, Marker>()

    private var placedLocation:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLakwatsaLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLastLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        } else {
            fusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    currentLocation = task.result
                    if (currentLocation != null) {
                        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                        supportMapFragment.getMapAsync { googleMap ->
                            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            placeOrUpdateUserOnMap(latLng, user.getDrawableProfilePicture(), user.name, user.uid)
                        }
                    }
                }
            }
        }
        var locationTask: Task<Location> = fusedLocationProviderClient?.getLastLocation() ?: return
        locationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                currentLocation = task.result
                if (currentLocation != null) {
                    val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                    supportMapFragment.getMapAsync { googleMap ->
                        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        placeOrUpdateUserOnMap(latLng, user.getDrawableProfilePicture(), user.name, user.uid)
                    }
                    DBHelper.saveLocation(currentLocation!!.latitude, currentLocation!!.longitude)
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val manila= LatLng(14.5995, 120.9842)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manila))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(manila, 10f))
    }


    private fun placeOrUpdateUserOnMap(latLng: LatLng, image: Int, name: String, uid: String){
        if(userMarkers.containsKey(uid)){
            userMarkers[uid]?.position=latLng
            return
        }
        else{
            val bitmap=BitmapFactory.decodeResource(resources, image)
            val scaledBitmap=Bitmap.createScaledBitmap(bitmap, 100, 100, false)
            val customMarker=BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            val marker=mMap.addMarker(MarkerOptions().position(latLng).title(name).icon(customMarker))
            userMarkers[uid]=marker!!
        }
    }

    private fun startLocationUpdates(){
        retrieveUserLocations()
        runnable= Runnable {
            retrieveUserLocations()
            handler.postDelayed(runnable!!, LOCATION_UPDATE_INTERVAL)
        }
        handler.postDelayed(runnable!!, LOCATION_UPDATE_INTERVAL)
    }

    private fun stopLocationUpdates(){
        handler.removeCallbacks(runnable!!)
    }

    private fun retrieveUserLocations(){
        lifecycleScope.launch {
            lakwatsa= DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)
            if(placedLocation==false){
                val lakwatsaLocation= LatLng(lakwatsa.locationLatitude, lakwatsa.locationLongitude)
                mMap.addMarker(MarkerOptions().position(lakwatsaLocation).title(lakwatsa.locationName))
                placedLocation=true 
            }
            for (userID in lakwatsa.lakwatsaUsers){
                val user= DBHelper.getUserbyUid(userID)
                if (user.latitude!=0.toDouble() && user.longitude!=0.toDouble()){
                    val latLng= LatLng(user.latitude, user.longitude)
                    Log.d("LakwatsaLocationActivity", "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")
                    Log.d("LakwatsaLocationActivity", "User: ${user.name}")
                    placeOrUpdateUserOnMap(latLng, user.getDrawableProfilePicture(), user.name, user.uid)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }
}