package com.example.maps_demos

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.maps_demos.databinding.ActivityPlacesUikitBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.PlaceDetailsCompactFragment
import com.google.android.libraries.places.widget.PlaceDetailsCompactFragment.Content
import com.google.android.libraries.places.widget.PlaceLoadListener
import com.google.android.libraries.places.widget.PlaceSearchFragment
import com.google.android.libraries.places.widget.PlaceSearchFragmentListener
import com.google.android.libraries.places.widget.model.AttributionPosition
import com.google.android.libraries.places.widget.model.MediaSize
import com.google.android.libraries.places.widget.model.Orientation
import com.google.android.libraries.places.api.net.SearchByTextRequest


class PlacesUIKitActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPlacesUikitBinding
    private var googleMap: GoogleMap? = null
    private var lastSelectedMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesUikitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val apiKey = BuildConfig.PLACES_API_KEY
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "API Key not found in local.properties", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.dismissButton.setOnClickListener {
            dismissPlaceDetails()
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val defaultLocation = LatLng(37.422, -122.085)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
        showPlaceSearchFragment()

        map.setOnMarkerClickListener { marker ->
            val placeId = marker.snippet
            if (!placeId.isNullOrEmpty()) {

                lastSelectedMarker = marker
                showPlaceDetailsFragment(placeId)
            } else {
                Toast.makeText(this, "No place ID found for this marker.", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun showPlaceDetailsFragment(placeId: String) {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.placeDetailsWrapper.visibility = View.GONE
        binding.searchFragment.visibility = View.GONE

        val orientation = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Orientation.HORIZONTAL
        } else {
            Orientation.VERTICAL
        }

        val fragment = PlaceDetailsCompactFragment.newInstance(
            listOf(Content.ADDRESS, Content.OPEN_NOW_STATUS, Content.MEDIA),
            orientation,
            R.style.CustomizedPlaceDetailsTheme
        )

        fragment.setPlaceLoadListener(object : PlaceLoadListener {
            override fun onSuccess(place: Place) {
                binding.loadingIndicator.visibility = View.GONE
                binding.placeDetailsWrapper.visibility = View.VISIBLE

                val placeLocation = LatLng(place.location.latitude, place.location.longitude)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 17f))
            }

            override fun onFailure(e: Exception) {
                dismissPlaceDetails()
                Toast.makeText(this@PlacesUIKitActivity, "Failed to load place details.", Toast.LENGTH_SHORT).show()
            }
        })

        supportFragmentManager.beginTransaction()
            .replace(binding.placeDetailsContainer.id, fragment)
            .commitNow()

        fragment.loadWithPlaceId(placeId)
    }

    private fun showPlaceSearchFragment() {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val request = SearchByTextRequest.newInstance("mexican restaurant in san jose, california", placeFields)

        val fragment = PlaceSearchFragment.newInstance(PlaceSearchFragment.STANDARD_CONTENT)
        fragment.preferTruncation = false
        fragment.attributionPosition = AttributionPosition.BOTTOM
        fragment.mediaSize = MediaSize.LARGE
        fragment.selectable = true

        fragment.registerListener(object : PlaceSearchFragmentListener {
            override fun onLoad(places: List<Place>) {
                Log.d("onLoad", "Loaded ${places.size} places")
                googleMap?.clear()
                lastSelectedMarker = null

                if (places.isEmpty()) return

                for (place in places) {
                    place.location?.let { latLng ->
                        val markerOptions = MarkerOptions()
                            .position(latLng)
                            .title(place.displayName)
                            .snippet(place.id)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        googleMap?.addMarker(markerOptions)
                    }
                }

                places.first().location?.let { firstLatLng ->
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 12f))
                }
            }

            override fun onRequestError(e: Exception) {
                Log.e("onRequestError", "Search error: ${e.message}", e)
            }

            override fun onPlaceSelected(place: Place) {
                Log.d("onPlaceSelected", "Place selected:  ID: ${place.id}")

                place.location?.let { latLng ->
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }

                place.id?.let { placeId ->
                    showPlaceDetailsFragment(placeId)
                }
            }
        })

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_fragment, fragment)
            .commitNow()

        fragment.configureFromSearchByTextRequest(request)
    }

    private fun dismissPlaceDetails() {
        binding.placeDetailsWrapper.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
        binding.searchFragment.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        googleMap = null
    }
}
