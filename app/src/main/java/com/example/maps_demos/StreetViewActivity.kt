package com.example.maps_demos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment

class StreetViewActivity : AppCompatActivity(), OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var streetViewPanorama: StreetViewPanorama
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street_view)

        supportActionBar?.title = "Street View"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val streetViewFragment = supportFragmentManager
            .findFragmentById(R.id.street_view_panorama) as SupportStreetViewPanoramaFragment
        streetViewFragment.getStreetViewPanoramaAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val defaultLocation = LatLng(40.706001, -73.997002)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))

        googleMap.setOnMapClickListener { latLng ->
            currentMarker?.remove()
            currentMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
            )

            streetViewPanorama.setPosition(latLng)
        }
    }

    override fun onStreetViewPanoramaReady(panorama: StreetViewPanorama) {
        streetViewPanorama = panorama
        val defaultLocation = LatLng(40.706001, -73.997002)
        streetViewPanorama.setPosition(defaultLocation)
    }
}
