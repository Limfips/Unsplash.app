package tgd.company.unsplashapp.service.factories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import tgd.company.unsplashapp.service.adapter.PhotosAdapter
import tgd.company.unsplashapp.ui.fragments.*
import javax.inject.Inject

class AppFragmentFactory @Inject constructor(
    private val photosAdapter: PhotosAdapter,
    private val glide: RequestManager
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            PhotoDetailsFragment::class.java.name -> PhotoDetailsFragment(glide)
            MainFragment::class.java.name -> MainFragment(glide)
            CollectionDetailsFragment::class.java.name -> CollectionDetailsFragment(glide)
            CollectionsFragment::class.java.name -> CollectionsFragment(glide)
            SearchPhotoFragment::class.java.name -> SearchPhotoFragment(photosAdapter)
            RandomPhotoFragment::class.java.name -> RandomPhotoFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }
}