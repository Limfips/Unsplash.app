package tgd.company.unsplashapp.service.repository

import retrofit2.Response
import tgd.company.unsplashapp.data.colection.Collection
import tgd.company.unsplashapp.data.photo.Photo
import tgd.company.unsplashapp.data.search.SearchResult
import tgd.company.unsplashapp.other.Resource

interface IPhotoRepository {
    suspend fun getRandom(): Resource<Photo>
    suspend fun searchPhoto(query: String, page: Int): Resource<SearchResult>
    suspend fun getCollections(page: Int): Resource<List<Collection>>
    suspend fun getCollectionPhotos(id: Int, page: Int): Resource<List<Photo>>
}
