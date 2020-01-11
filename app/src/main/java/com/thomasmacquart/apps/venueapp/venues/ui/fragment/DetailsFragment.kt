package com.thomasmacquart.apps.venueapp.venues.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.thomasmacquart.apps.venueapp.R
import com.thomasmacquart.apps.venueapp.VenueApp
import com.thomasmacquart.apps.venueapp.core.extensions.exhaustive
import com.thomasmacquart.apps.venueapp.venues.ui.uimodel.DetailsUiModel
import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.DetailsViewModel
import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.DetailsViewState
import kotlinx.android.synthetic.main.details_fragment.*
import javax.inject.Inject

class DetailsFragment : Fragment() {

    @Inject
    lateinit var factory: DetailsViewModel.Factory

    private val viewModel by viewModels<DetailsViewModel> { factory }

    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as VenueApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUiObservable().observe(this, Observer {
            when (it) {
                is DetailsViewState.OnPopulateData -> populateData(it.uiModel)
                is DetailsViewState.OnError -> showError(it.error)
                DetailsViewState.OnLoading -> TODO()
            }.exhaustive
        })

        val venueId = DetailsFragmentArgs.fromBundle(
            requireArguments()
        ).venueId

        if (savedInstanceState == null) {
            viewModel.loadVenueDetails(venueId)
        }

    }

    private fun populateData(uiModel : DetailsUiModel) {
        details_venue_name.text = uiModel.getName()
        details_venue_rating.text = uiModel.getRating(requireActivity().applicationContext)
    }

    private fun showError(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_LONG).show()
    }
}