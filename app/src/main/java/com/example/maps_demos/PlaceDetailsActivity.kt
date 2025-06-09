package com.example.maps_demos

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        supportActionBar?.title = "Place Details"

        placesClient = (application as PlaceClient).placesClient

        val placeId = "ChIJk_s92NyipBIRUMnDG8Kq2Js"

        // Fields you want to retrieve
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.NATIONAL_PHONE_NUMBER,
            Place.Field.WEBSITE_URI,
        )

        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                showPlaceDetails(place)
            }
            .addOnFailureListener { exception ->
                Log.e("PlaceDetails", "Place not found: ${exception.message}")
            }
    }

    private fun showPlaceDetails(place: Place) {
        findViewById<TextView>(R.id.placeNameTextView).text = place.displayName
        findViewById<TextView>(R.id.placeAddressTextView).text = place.formattedAddress
        findViewById<TextView>(R.id.placePhoneTextView).text =
            place.nationalPhoneNumber ?: "Phone not available"
        findViewById<TextView>(R.id.placeWebsiteTextView).text =
            place.websiteUri?.toString() ?: "Website not available"
    }
}