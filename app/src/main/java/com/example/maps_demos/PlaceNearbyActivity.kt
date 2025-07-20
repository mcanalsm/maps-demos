package com.example.maps_demos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchNearbyRequest



class PlaceNearbyActivity : AppCompatActivity(),  OnMapReadyCallback  {

    private lateinit var placesClient: com.google.android.libraries.places.api.net.PlacesClient
    private lateinit var googleMap:GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_place_nearby)

    supportActionBar?.title = "Place Nearby"

    placesClient = (application as PlaceClient).placesClient


        val mapFragment = supportFragmentManager
        .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    private fun nearbyTextRequest() {

    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

    val center = LatLng(40.7580, -73.9855)
    val circle = CircularBounds.newInstance(center, /* radius = */ 1000.0)

    val includedTypes = listOf("restaurant", "cafe")
    val excludedTypes = listOf("pizza_restaurant", "american_restaurant")
    val maxSearchResults = 10

    val searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
        .setIncludedTypes(includedTypes)
        .setExcludedTypes(excludedTypes)
        .setMaxResultCount(maxSearchResults)
        .build()


    placesClient.searchNearby(searchNearbyRequest)
        .addOnSuccessListener { response ->
            val places = response.places

            if (places.isNotEmpty()) {
                Log.d("NearbyPlaces", "Found ${places.size} places:")

                addMarkersForPlaces(places)
                drawCircleOnMap(center)
                moveCamera(center)

            } else {
                Log.d("NearbyPlaces", "No places found nearby.")
            }
        }

    }

    private fun drawCircleOnMap(center: LatLng ){
        googleMap.addCircle(
            CircleOptions()
                .center(center)
                .radius(1000.0)
                .strokeColor(0xFFCD5C5C.toInt())
                .fillColor(0x30FF0000)
        )

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, 14f)
        googleMap.animateCamera(cameraUpdate)
        googleMap.moveCamera(cameraUpdate)
    }

    private fun addMarkersForPlaces(places: List<Place>) {
        places.forEach { place ->
            Log.d(
                "TextSearchPlaces",
                "Place ID: ${place.id}, Name: ${place.name}, Location: ${place.latLng?.latitude}, ${place.latLng?.longitude}"
            )
            place.latLng?.let { latLng ->
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(place.name)
                    .snippet(place.id)
                googleMap.addMarker(markerOptions)
            }
        }
    }

    private fun moveCamera(center: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, 14f)
        googleMap.animateCamera(cameraUpdate)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        Log.i("onMapReady", "Map loaded")

        nearbyTextRequest()
    }


}
