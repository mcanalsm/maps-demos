package com.example.maps_demos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse

class PlaceTextSearchActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var placesClient: com.google.android.libraries.places.api.net.PlacesClient

    // Define the search area boundaries
    private val southWest = LatLng(37.38816277477739, -122.08813770258874)
    private val northEast = LatLng(37.39580487866437, -122.07702325966572)

    private val textQuery = "Tesla charger station"

    // Define the fields we want to retrieve for each place
    private val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_text_search)

        supportActionBar?.title = "Place Text Search"


        placesClient = (application as PlaceClient).placesClient


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun searchTextRequest() {
        // Create the text search request
        val searchByTextRequest = SearchByTextRequest.builder(textQuery, placeFields)
            .setMaxResultCount(10)
            .setLocationRestriction(RectangularBounds.newInstance(southWest, northEast))
            .build()

        // Execute the search request
        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response: SearchByTextResponse ->
                val places = response.places
                if (places.isNotEmpty()) {
                    Log.d("TextSearchPlaces", "Found ${places.size} places:")
                    addMarkersForPlaces(places)
                    drawRectangleOnMap()
                    moveCamera()
                } else {
                    Log.d("TextSearch", "No results for this area")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("TextSearch", "Error searching for places: ${exception.message}")
            }
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

    private fun drawRectangleOnMap() {
        val southEast = LatLng(southWest.latitude, northEast.longitude)
        val northWest = LatLng(northEast.latitude, southWest.longitude)

        val rectOptions = PolygonOptions()
            .add(southWest)
            .add(southEast)
            .add(northEast)
            .add(northWest)
            .fillColor(0x30FF0000)
            .strokeColor(0xFF8B0000.toInt())
            .strokeWidth(5f)

        googleMap.addPolygon(rectOptions)
    }

    private fun moveCamera() {
        val bounds = LatLngBounds(southWest, northEast)
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
        googleMap.animateCamera(cameraUpdate)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        searchTextRequest()
        Log.i("onMapReady", "Map loaded")
    }
}