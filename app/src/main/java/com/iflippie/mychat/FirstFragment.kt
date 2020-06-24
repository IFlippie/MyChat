package com.iflippie.mychat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val totalRooms = mutableListOf<ChatRoom>()
    private val roomAdapter = RoomAdapter(totalRooms)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            initViews()
//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    private fun initViews() {
        rvRooms?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvRooms.adapter = roomAdapter
        //rvRooms.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        //createItemTouchHelper().attachToRecyclerView()
        //setHasOptionsMenu(true)
    }

}
