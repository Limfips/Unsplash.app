package tgd.company.unsplashapp.data.colection

import tgd.company.unsplashapp.data.photo.Photo

data class Collection(
    val description: String,
    val id: Int,
    val title: String,
    val total_photos: Int,
    val cover_photo: Photo
)