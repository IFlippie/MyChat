package com.iflippie.mychat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val totalRooms = mutableListOf<ChatRoom>()
    private val roomAdapter = RoomAdapter(totalRooms)
    private var totalStrings = mutableListOf<String>()

    private val ref: FirebaseDatabase by lazy { FirebaseDatabase.getInstance()}
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private val userUid: String? by lazy {auth.currentUser?.uid}
    private val friendsQuery: Query by lazy { ref.getReference("Users/${userUid}/rooms") }

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
//        friendsQuery.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    totalRooms.clear()
//                    println("hello1")
//                    for (i in dataSnapshot.children) {
//                        totalStrings.add(i.toString())
//                        println("hello2")
//                        if (totalStrings.isNotEmpty()) {
                            ref.getReference("ChatRooms/").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (i in dataSnapshot.children){
                                        val room = i.getValue(ChatRoom::class.java)

                                        room?.let {
                                            totalRooms.add(it)
                                            roomAdapter.notifyDataSetChanged()
                                        }
                                    }

                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    println(databaseError)
                                }
                            })

//                        }
//                    }
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                println(databaseError)
//            }
//        })
    }

    private fun initViews() {
        rvRooms?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvRooms.adapter = roomAdapter
        //rvRooms.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        //createItemTouchHelper().attachToRecyclerView()
        //setHasOptionsMenu(true)
    }

}
