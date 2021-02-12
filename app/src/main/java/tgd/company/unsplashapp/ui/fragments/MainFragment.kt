package tgd.company.unsplashapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.data.photo.Photo
import tgd.company.unsplashapp.databinding.FragmentMainBinding
import tgd.company.unsplashapp.other.Resource
import tgd.company.unsplashapp.other.Status
import tgd.company.unsplashapp.ui.viewmodel.PhotoViewModel
import javax.inject.Inject

class MainFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.fragment_main) {

    private lateinit var viewModel: PhotoViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoViewModel::class.java)

        subscribeToObservers()
        settingBottomSheetBehavior()

        viewModel.closeBehavior = {
            if (BottomSheetBehavior
                    .from(binding.bottomSheetBehavior)
                    .state == BottomSheetBehavior.STATE_EXPANDED) {
                hideBehavior()
                true
            } else {
                false
            }

        }

    }

    private fun openDetails() {
        Log.w("PhotoDetailsFragment_TAG", "OPEN!")
        BottomSheetBehavior
            .from(binding.bottomSheetBehavior)
            .state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun subscribeToObservers() {
        viewModel.selectedPhoto.observe(viewLifecycleOwner) {
            if (it != null) {
                openDetails()
            }
        }
    }

    private fun settingBottomSheetBehavior() {
        BottomSheetBehavior
                .from(binding.bottomSheetBehavior)
                .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    @SuppressLint("SwitchIntDef")
                    override fun onStateChanged(bottomSheet: View, newState: Int) {}
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (slideOffset > 0.1f) {
                            binding.mainFragmentRoot.setBackgroundColor(Color.parseColor("#70000000"))
                        } else {
                            binding.mainFragmentRoot.setBackgroundColor(Color.parseColor("#00000000"))
                        }
                    }
                })

        binding.closeBehaviorView.setOnClickListener {
            hideBehavior()
        }
    }

    private fun hideBehavior() {
        viewModel.setSelectedPhoto(null)
        BottomSheetBehavior
            .from(binding.bottomSheetBehavior)
            .state = BottomSheetBehavior.STATE_HIDDEN
    }
}