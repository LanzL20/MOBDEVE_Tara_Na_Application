package com.mobdeve.s13.lim.pacheco.tan.tarana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ActivityLakwatsaUpcommingSetLocationBinding
import kotlinx.coroutines.launch

class LakwatsaUpcommingSetLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLakwatsaUpcommingSetLocationBinding
    private var finalLocation= LatLng(0.0,0.0)
    private lateinit var lakwatsa: Lakwatsa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLakwatsaUpcommingSetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("LakwatsaDebugging","i got here!!!!!!!!!!!")
        Log.d("LakwatsaDebugging",intent.getStringExtra(Lakwatsa.ID_KEY)!!)
        Log.d("LakwatsaDebugging","did i got here!!!!!!!!!!!")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lifecycleScope.launch {
            Log.d("LakwatsaDebugging","i got here")
            Log.d("LakwatsaDebugging",intent.getStringExtra(Lakwatsa.ID_KEY)!!)
            lakwatsa=DBHelper.getLakwatsa(intent.getStringExtra(Lakwatsa.ID_KEY)!!)
            Log.d("LakwatsaDebugging",lakwatsa.toString())
            binding.confirmButton.setOnClickListener {
                val newLakwatsa=Lakwatsa(
                    lakwatsa.lakwatsaId,
                    lakwatsa.lakwatsaUsers,
                    finalLocation.latitude,
                    finalLocation.longitude,
                    lakwatsa.lakwatsaTitle,
                    lakwatsa.date,
                    lakwatsa.time,
                    lakwatsa.pollingList,
                    lakwatsa.album,
                    lakwatsa.status,
                    lakwatsa.lakwatsaAdmin,
                    lakwatsa.locationName
                )
                DBHelper.updateLakwatsa(newLakwatsa)
                finish()
            }
        }

        if(!Places.isInitialized()){
            Places.initialize(applicationContext, getString(R.string.google_places_key))
        }

        val autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autoCompleteFragment.setPlaceFields(listOf(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))
        autoCompleteFragment.setCountry("PH")
        autoCompleteFragment.setOnPlaceSelectedListener(object : com.google.android.libraries.places.widget.listener.PlaceSelectionListener {
            override fun onPlaceSelected(place: com.google.android.libraries.places.api.model.Place) {
                mMap.clear()
                val location = place.latLng
                finalLocation=location!!
                mapFragment.getMapAsync {
                    mMap = it
                    mMap.addMarker(MarkerOptions().position(location!!).title(place.name))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                    //add zoom
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

                }
            }
            override fun onError(p0: Status) {
                TODO("Not yet implemented")
            }

        })


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
        val manila= LatLng(14.5995, 120.9842)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manila))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(manila, 10f))

        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng))
            finalLocation=latLng
        }
    }
}