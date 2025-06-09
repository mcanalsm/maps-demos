package com.example.maps_demos

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.Collections

class PlacePhotosActivity : AppCompatActivity(){
    private lateinit var placesClient: PlacesClient
    private lateinit var imageView: ImageView
    private val placeId = "ChIJk_s92NyipBIRUMnDG8Kq2Js"
    private val placeFields = Collections.singletonList(Field.PHOTO_METADATAS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_photos)
        imageView = findViewById(R.id.photoImageView)

        supportActionBar?.title = "Place Photos"

        placesClient = (application as PlaceClient).placesClient

        placeDetailsRequest()
    }

    private fun placeDetailsRequest() {
        val placeDetailsRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(placeDetailsRequest)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                val metadata = place.photoMetadatas

                if (metadata == null || metadata.isEmpty()) {
                    Log.w("Place Details", "No photo metadata available")
                    return@addOnSuccessListener
                }

                val photoMetadata = metadata[0]

                Log.d("Place Details", "Found photo metadata: $photoMetadata")

                val photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
                    .setMaxWidth(500)
                    .setMaxHeight(300)
                    .build()

                placesClient.fetchResolvedPhotoUri(photoRequest)
                    .addOnSuccessListener { fetchResolvedPhotoUriResponse ->
                        val uri = fetchResolvedPhotoUriResponse.uri
                        val requestOptions = RequestOptions().override(Target.SIZE_ORIGINAL)
                        Glide.with(this).load(uri).apply(requestOptions).into(imageView)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Place Details", "Error fetching place details: ${exception.message}")
                    }

            }
            .addOnFailureListener { exception ->
                Log.e("Place Details", "Error fetching place details: ${exception.message}")
            }
    }
}