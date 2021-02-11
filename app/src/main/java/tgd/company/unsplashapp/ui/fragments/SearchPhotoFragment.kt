package tgd.company.unsplashapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.databinding.FragmentSearchPhotoBinding
import tgd.company.unsplashapp.other.Constants.GRID_SPAN_COUNT
import tgd.company.unsplashapp.other.Constants.SEARCH_TIME_DELAY
import tgd.company.unsplashapp.other.Status
import tgd.company.unsplashapp.service.adapter.ImageAdapter
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject


// •Поиск фотографий по ключевым словам;
class SearchPhotoFragment @Inject constructor(
    private val glide: RequestManager,
    private val imageAdapter: ImageAdapter
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
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        setupRecyclerView()
        subscribeToObservers()

        binding.btnPrev.isClickable = currentPage != 1
        binding.btnNext.isClickable = currentPage != lastPage

        binding.btnPrev.setOnClickListener {
            currentPage--

            viewModel.searchPhoto(searchQuery, currentPage)
        }

        binding.btnNext.setOnClickListener {
            currentPage++

            viewModel.searchPhoto(searchQuery, currentPage)
        }

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

        imageAdapter.setOnItemClickListener {
            viewModel.setSelectedPhoto(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    Status.SUCCESS -> {
                        val images = result.data?.results
                        lastPage = result.data?.total_pages ?: 1
                        imageAdapter.images = images ?: listOf()
                        binding.progressBar.visibility = View.GONE


                        binding.tvCurrentPage.text = "$currentPage / $lastPage"

                        binding.btnPrev.isClickable = currentPage != 1
                        binding.btnNext.isClickable = currentPage != lastPage
                        imageAdapter.notifyDataSetChanged()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().findViewById(R.id.rootLayout),
                            result.message ?: "An unknown error occured.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}