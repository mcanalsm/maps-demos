package com.example.maps_demos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng


class StreetViewActivity : AppCompatActivity(), OnStreetViewPanoramaReadyCallback {

    // Get a handle to the GoogleMap object and display marker.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street_view)

        supportActionBar?.title = "Street View Map"


        val streetViewPanoramaFragment =
            supportFragmentManager
                .findFragmentById(R.id.street_view_panorama) as SupportStreetViewPanoramaFragment
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this)
    }

    override fun onStreetViewPanoramaReady(streetViewPanorama: StreetViewPanorama) {
        val brooklynBridge = LatLng(40.706001, -73.997002) // Times Square, NYC

        streetViewPanorama.setPosition(brooklynBridge)
    }
}
