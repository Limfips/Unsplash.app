package tgd.company.unsplashapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.databinding.FragmentPhotoDetailsBinding
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject

//  Возможность открыть конкретную фотографию и посмотреть следующую
//   информацию по ней:
//   Ширина;
//   Высота;
//   Описание;
//   Ссылка на изображение в хорошем качестве.
class PhotoDetailsFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.fragment_photo_details) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoViewModel::class.java)

        viewModel.selectedPhoto.observe(viewLifecycleOwner) {
            it?.let {
//                binding.tvTest.text = it.toString()
            }
        }
    }
}