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
import tgd.company.unsplashapp.databinding.FragmentCollectionsBinding
import tgd.company.unsplashapp.other.Constants
import tgd.company.unsplashapp.other.Status
import tgd.company.unsplashapp.other.Status.*
import tgd.company.unsplashapp.service.adapter.CollectionsAdapter
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject

// •Отображение списка коллекций фотографий
class CollectionsFragment @Inject constructor(
    private val collectionsAdapter: CollectionsAdapter
) : Fragment(R.layout.fragment_collections) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!

    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
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
        settingBtnNavigation()



        viewModel.getCollections(currentPage)
        collectionsAdapter.setOnItemClickListener {
            viewModel.setSelectedCollection(it)
            findNavController().navigate(
                    CollectionsFragmentDirections.actionCollectionsFragmentToCollectionDetailsFragment()
            )
        }
    }

    private fun settingBtnNavigation() {
        binding.btnPrev.isClickable = currentPage != 1

        binding.btnPrev.setOnClickListener {
            currentPage--

            viewModel.getCollections(currentPage)
        }

        binding.btnNext.setOnClickListener {
            currentPage++

            viewModel.getCollections(currentPage)
        }
    }

    private fun subscribeToObservers() {
        viewModel.collections.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    SUCCESS -> {
                        val collections = result.data
                        collectionsAdapter.collections = collections ?: listOf()

                        binding.pbCollection.visibility = View.GONE

                        binding.tvCurrentPage.text = "$currentPage"

                        binding.btnPrev.isClickable = currentPage != 1
                        collectionsAdapter.notifyDataSetChanged()
                    }
                    ERROR -> {
                        Snackbar.make(
                                requireActivity().findViewById(R.id.rootLayout),
                                result.message ?: "An unknown error occured.",
                                Snackbar.LENGTH_LONG
                        ).show()
                        binding.pbCollection.visibility = View.GONE
                    }
                    LOADING -> {
                        binding.pbCollection.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = collectionsAdapter
            layoutManager = GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
        }
    }
}