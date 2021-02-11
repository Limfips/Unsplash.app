package tgd.company.unsplashapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.databinding.FragmentCollectionDetailsBinding
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject

//возможность открыть коллекцию и
//посмотреть фотографии в ней;
class CollectionDetailsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.fragment_collection_details) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentCollectionDetailsBinding? = null
    private val binding get() = _binding!!

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
    }
}