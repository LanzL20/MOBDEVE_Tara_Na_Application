package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaLocationBinding

class LakwatsaLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val FINE_PERMISSION_CODE=1
    private var currentLocation:Location?=null
    private var fusedLocationProviderClient: FusedLocationProviderClient?=null

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLakwatsaLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLakwatsaLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //val mapFragment = supportFragmentManager
        //    .findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                        supportMapFragment.getMapAsync { googleMap ->
                            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                            val markerOptions = MarkerOptions().position(latLng).title("I am here!")
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
                            googleMap.addMarker(markerOptions)
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
                    val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    supportMapFragment.getMapAsync { googleMap ->
                        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                        val markerOptions = MarkerOptions().position(latLng).title(UserSession.getUser().name)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
                        googleMap.addMarker(markerOptions)
                    }
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}