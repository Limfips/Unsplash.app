package tgd.company.unsplashapp.service.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tgd.company.unsplashapp.BuildConfig
import tgd.company.unsplashapp.data.colection.Collection
import tgd.company.unsplashapp.data.photo.Photo
import tgd.company.unsplashapp.data.search.SearchResult

interface UnsplashApi {

    @GET("photos/random/")
    suspend fun getRandomPhoto(
        @Query("client_id") apiKey: String = BuildConfig.ACCESS_KEY
    ): Response<Photo>

    @GET("search/photos")
    suspend fun searchPhoto(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("client_id") apiKey: String = BuildConfig.ACCESS_KEY
    ): Response<SearchResult>

    @GET("collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("client_id") apiKey: String = BuildConfig.ACCESS_KEY
    ): Response<List<Collection>>

    @GET("/collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("client_id") apiKey: String = BuildConfig.ACCESS_KEY
    ): Response<List<Photo>>
}