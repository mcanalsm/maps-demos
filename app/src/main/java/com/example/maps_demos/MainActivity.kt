package com.example.maps_demos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDynamicMap = findViewById<Button>(R.id.btn_dynamic_map)
        val btnStreetViewMap = findViewById<Button>(R.id.btn_street_view)
        val btnPlaceAutocomplete = findViewById<Button>(R.id.btn_place_autocomplete)
        val btnPlaceNearby = findViewById<Button>(R.id.btn_place_nearby)
        val btnPlaceTextSearch = findViewById<Button>(R.id.btn_place_textsearch)
        val btnPlaceDetails = findViewById<Button>(R.id.btn_place_details)
        val btnPlacePhotos = findViewById<Button>(R.id.btn_place_photos)
        val btnPlaceSAR = findViewById<Button>(R.id.btn_place_SAR)
        val btnPlacesUIkit = findViewById<Button>(R.id.btn_places_UIkit)



        btnDynamicMap.setOnClickListener {
            startActivity(Intent(this, DynamicMapActivity::class.java))
        }

        btnStreetViewMap.setOnClickListener {
            startActivity(Intent(this, StreetViewActivity::class.java))
        }

        btnPlaceAutocomplete.setOnClickListener {
            startActivity(Intent(this, PlaceAutocompleteActivity::class.java))
        }

        btnPlaceNearby.setOnClickListener {
            startActivity(Intent(this, PlaceNearbyActivity::class.java))
        }

        btnPlaceTextSearch.setOnClickListener {
            startActivity(Intent(this, PlaceTextSearchActivity::class.java))
        }

        btnPlaceDetails.setOnClickListener {
            startActivity(Intent(this, PlaceDetailsActivity::class.java))
        }


        btnPlacePhotos.setOnClickListener {
            startActivity(Intent(this, PlacePhotosActivity::class.java))
        }

        btnPlaceSAR.setOnClickListener {
            startActivity(Intent(this, PlaceSARActivity::class.java))
        }

        btnPlacesUIkit.setOnClickListener {
            startActivity(Intent(this, PlacesUIKitActivity::class.java))
        }





    }
}
