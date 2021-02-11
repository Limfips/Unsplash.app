package tgd.company.unsplashapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.databinding.FragmentCollectionDetailsBinding
import tgd.company.unsplashapp.other.Constants
import tgd.company.unsplashapp.other.Status
import tgd.company.unsplashapp.service.adapter.PhotosAdapter
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject

//возможность открыть коллекцию и
//посмотреть фотографии в ней;
class CollectionDetailsFragment @Inject constructor(
        private val photosAdapter: PhotosAdapter
) : Fragment(R.layout.fragment_collection_details) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentCollectionDetailsBinding? = null
    private val binding get() = _binding!!

    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailsBinding.inflate(inflater, container, false)
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
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        setupRecyclerView()
        subscribeToObservers()

        binding.btnPrevForCollection.isClickable = currentPage != 1

        binding.btnPrevForCollection.setOnClickListener {
            currentPage--

            viewModel.getCollections(currentPage)
        }

        binding.btnNextForCollection.setOnClickListener {
            currentPage++

            viewModel.getCollections(currentPage)
        }

        viewModel.getPhotosForCollections(currentPage)
        photosAdapter.setOnItemClickListener {
            viewModel.setSelectedPhoto(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.photoForCollections.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    Status.SUCCESS -> {
                        val photos = result.data
                        photosAdapter.photos = photos ?: listOf()

                        binding.pbPhotoForCollection.visibility = View.GONE

                        binding.tvCurrentPageForCollection.text = "$currentPage"

                        binding.btnPrevForCollection.isClickable = currentPage != 1
                        photosAdapter.notifyDataSetChanged()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                                requireActivity().findViewById(R.id.rootLayout),
                                result.message ?: "An unknown error occured.",
                                Snackbar.LENGTH_LONG
                        ).show()
                        binding.pbPhotoForCollection.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.pbPhotoForCollection.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvImagesForCollection.apply {
            adapter = photosAdapter
            layoutManager = GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
        }
    }
}