package tgd.company.unsplashapp.data.search

import tgd.company.unsplashapp.data.photo.Photo

data class SearchResult(
    val results: List<Photo>,
    val total: Int,
    val total_pages: Int
)