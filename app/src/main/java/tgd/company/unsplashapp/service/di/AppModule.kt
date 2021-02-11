package tgd.company.unsplashapp.service.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.other.Constants.BASE_URL
import tgd.company.unsplashapp.service.remote.UnsplashApi
import tgd.company.unsplashapp.service.repository.DefaultPhotoRepository
import tgd.company.unsplashapp.service.repository.IPhotoRepository
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUnsplashApi(): UnsplashApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(UnsplashApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).applyDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_not_supported_24)
    )

    @Singleton
    @Provides
    fun providePhotoRepository(
        api: UnsplashApi
    ) = DefaultPhotoRepository(api) as IPhotoRepository
}