package com.iflippie.mychat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SettingsFragment : Fragment() {

    private val colors = arrayListOf<ColorItem>()
    private val colorAdapter = ColorAdapter(colors) { colorItem -> onColorClick(colorItem) }
    private lateinit var viewModel: SettingsFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
           findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun initViews() {
        rvColors.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvColors.adapter = colorAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SettingsFragmentViewModel::class.java)
        viewModel.colorItems.observe(viewLifecycleOwner, Observer {
            colors.clear()
            colors.addAll(it)
            colorAdapter.notifyDataSetChanged()
        })
    }

    private fun onColorClick(colorItem: ColorItem) {
        //Snackbar.make(rvColors, "This color is: ${colorItem.name}", Snackbar.LENGTH_LONG).show()
        Toast.makeText(requireContext(), "Border changed to: ${colorItem.name}", Toast.LENGTH_SHORT).show()
    }

}
