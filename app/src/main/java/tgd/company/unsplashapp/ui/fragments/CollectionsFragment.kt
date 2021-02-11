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
import tgd.company.unsplashapp.databinding.FragmentCollectionsBinding
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject

// •Отображение списка коллекций фотографий
class CollectionsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.fragment_collections) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!

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
    }
}