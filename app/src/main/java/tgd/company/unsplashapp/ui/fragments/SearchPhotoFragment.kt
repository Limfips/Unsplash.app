package tgd.company.unsplashapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.databinding.FragmentSearchPhotoBinding
import tgd.company.unsplashapp.other.Constants.GRID_SPAN_COUNT
import tgd.company.unsplashapp.other.Constants.SEARCH_TIME_DELAY
import tgd.company.unsplashapp.other.Status
import tgd.company.unsplashapp.service.adapter.PhotosAdapter
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject


// •Поиск фотографий по ключевым словам;
class SearchPhotoFragment @Inject constructor(
    private val photosAdapter: PhotosAdapter
) : Fragment(R.layout.fragment_search_photo) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentSearchPhotoBinding? = null
    private val binding get() = _binding!!

    private var lastPage = 1
    private var currentPage = 1
    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPhotoBinding.inflate(inflater, container, false)
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
                    photosAdapter.clear()
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        setupRecyclerView()
        subscribeToObservers()
        settingBtnNavigation()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        currentPage = 1
                        searchQuery = editable.toString()

                        viewModel.searchPhoto(searchQuery, currentPage)
                    }
                }
            }
        }

        photosAdapter.setOnItemClickListener {
            viewModel.setSelectedPhoto(it)
        }
    }

    private fun settingBtnNavigation() {
        binding.btnPrev.isClickable = currentPage != 0 && lastPage != 0
        binding.btnNext.isClickable = currentPage != lastPage && lastPage != 0

        binding.btnPrev.setOnClickListener {
            currentPage--

            viewModel.searchPhoto(searchQuery, currentPage)
        }

        binding.btnNext.setOnClickListener {
            currentPage++

            viewModel.searchPhoto(searchQuery, currentPage)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeToObservers() {
        viewModel.searchPhotos.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { result ->
                Log.w("SPF_TAG", result.toString())
                when(result.status) {
                    Status.SUCCESS -> {
                        val photos = result.data?.results
                        lastPage = result.data?.total_pages ?: 0
                        photosAdapter.photos = photos ?: listOf()
                        binding.pbSearchPhotos.visibility = View.GONE

                        if (lastPage == 0) {
                            binding.tvNotFound.visibility = View.VISIBLE
                        } else {
                            binding.tvNotFound.visibility = View.INVISIBLE
                        }

                        binding.tvCurrentPage.text = "$currentPage / $lastPage"

                        binding.btnPrev.isClickable = currentPage != 1
                        binding.btnNext.isClickable = currentPage != lastPage && lastPage != 0
                        photosAdapter.notifyDataSetChanged()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().findViewById(R.id.rootLayout),
                            result.message ?: "An unknown error occured.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.pbSearchPhotos.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.pbSearchPhotos.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = photosAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}