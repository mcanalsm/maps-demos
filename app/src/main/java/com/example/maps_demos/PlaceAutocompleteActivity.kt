package com.example.maps_demos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class PlaceAutocompleteActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap:GoogleMap
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_autocomplete)

        supportActionBar?.hide()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        val apiKey = BuildConfig.PLACES_API_KEY

        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        Places.initialize(applicationContext, apiKey)
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment
            .setPlaceFields(placeFields)
            .setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {

                    Log.i("place", "Place: ${place.name}, ID: ${place.id}, Location: ${place.latLng}")

                    val placeLatLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                    currentMarker?.remove()
                    currentMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(placeLatLng)
                            .title(place.displayName)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLatLng))
                }

                override fun onError(status: Status) {
                    Log.i("Error when Place is not returned", "An error occurred: $status")
                }
            })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        Log.i("onMapReady", "Map loaded")
    }
}