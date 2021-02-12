package tgd.company.unsplashapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.data.photo.Photo
import tgd.company.unsplashapp.databinding.FragmentMainBinding
import tgd.company.unsplashapp.databinding.FragmentRandomPhotoBinding
import tgd.company.unsplashapp.other.Resource
import tgd.company.unsplashapp.other.Status
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject


class RandomPhotoFragment @Inject constructor(
        private val glide: RequestManager
) : Fragment(R.layout.fragment_random_photo) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentRandomPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoViewModel::class.java)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!viewModel.closeBehavior!!.invoke()) {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        onInitBottomAppBar()
        subscribeToObservers()

        viewModel.fab = binding.btnRandomPhoto

        binding.btnRandomPhoto.setOnClickListener {
            viewModel.getRandomPhoto()
        }

        if (viewModel.randomPhoto.value == null ) {
            viewModel.getRandomPhoto()
        } else {
            settingPhoto(
                    viewModel.randomPhoto.value!!.peekContent().data?.urls?.regular,
                    viewModel.randomPhoto.value!!.peekContent()
            )
        }
    }

    private fun subscribeToObservers() {

        viewModel.randomPhoto.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    Status.SUCCESS -> {
                        val url = result.data?.urls?.small
                        settingPhoto(url, result)
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                                binding.root,
                                result.message ?: "An unknown error occured.",
                                Snackbar.LENGTH_LONG
                        ).setAnchorView(viewModel.fab).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun settingPhoto(
            url: String?,
            result: Resource<Photo>
    ) {
        glide.load(url).into(binding.ltRandom.ivItemPhoto)
        binding.ltRandom.ivItemPhoto.setOnClickListener {
            viewModel.setSelectedPhoto(result.data)
        }
    }

    private fun onInitBottomAppBar() {

        binding.bottomAppBar.setNavigationOnClickListener {
            findNavController().navigate(
                    RandomPhotoFragmentDirections.actionRandomPhotoFragmentToCollectionsFragment()
            )
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.helper -> {

                    true
                }
                R.id.search -> {
                    findNavController().navigate(
                            RandomPhotoFragmentDirections.actionRandomPhotoFragmentToSearchPhotoFragment()
                    )
                    true
                }
                else -> false
            }
        }


    }
}