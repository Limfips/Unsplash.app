package tgd.company.unsplashapp.service.repository

import retrofit2.Response
import tgd.company.unsplashapp.data.colection.Collection
import tgd.company.unsplashapp.data.photo.Photo
import tgd.company.unsplashapp.data.search.SearchResult
import tgd.company.unsplashapp.other.Resource
import tgd.company.unsplashapp.service.remote.UnsplashApi
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val api: UnsplashApi
) : IPhotoRepository {

    override suspend fun getRandom(): Resource<Photo> {
        return try {
            val response = api.getRandomPhoto()
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured 701", null)
            } else {
                Resource.error("An unknown error occured 702", null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun searchPhoto(query: String, page: Int): Resource<SearchResult> {
        return try {
            val response = api.searchPhoto(query, page)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured 701", null)
            } else {
                Resource.error("An unknown error occured 702", null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getCollections(page: Int): Resource<List<Collection>> {
        return try {
            val response = api.getCollections(page)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured 701", null)
            } else {
                Resource.error("An unknown error occured 702", null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getCollectionPhotos(id: Int, page: Int): Resource<List<Photo>> {
        return try {
            val response = api.getCollectionPhotos(id, page)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured 701", null)
            } else {
                Resource.error("An unknown error occured 702", null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}