package com.iflippie.mychat.old
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.*
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.firebase.ui.database.FirebaseRecyclerAdapter
//import com.firebase.ui.database.FirebaseRecyclerOptions
//import com.firebase.ui.database.SnapshotParser
//import com.google.android.gms.auth.api.Auth
//import com.google.android.gms.common.ConnectionResult
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.Query
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//import com.iflippie.mychat.R
//import de.hdodenhof.circleimageview.CircleImageView
//import kotlinx.android.synthetic.main.activity_main.*
//
//
//class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
//
//    class MessageViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
//        var messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
//        var messageImageView: ImageView = itemView.findViewById(R.id.messageImageView)
//        var messengerTextView: TextView = itemView.findViewById(R.id.messengerTextView)
//        var messengerImageView: CircleImageView = itemView.findViewById(R.id.messengerImageView) as CircleImageView
//    }
//
//    private lateinit var mUsername: String
//    private lateinit var mPhotoUrl: String
//    private lateinit var mGoogleApiClient: GoogleApiClient
//    private var mLinearLayoutManager: LinearLayoutManager? = null
//    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
//    private val user: FirebaseUser? by lazy { auth.currentUser}
//
//    private lateinit var mFirebaseDatabaseReference: DatabaseReference
//    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
//    private lateinit var options : FirebaseRecyclerOptions<FriendlyMessage>
//    // Firebase instance variables
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        mUsername = ANONYMOUS
//        fixButton.setOnClickListener {
//            val intent = Intent(this, SecondActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        mGoogleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API).build()
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
//        val parser = SnapshotParser { dataSnapshot ->
//                val friendlyMessage = dataSnapshot.getValue(FriendlyMessage::class.java)
//                friendlyMessage?.setId(dataSnapshot.key!!)
//                friendlyMessage!!
//            }
//
//        val messagesRef: Query? = mFirebaseDatabaseReference.child(MESSAGES_CHILD)
//        options = FirebaseRecyclerOptions.Builder<FriendlyMessage>().setQuery(messagesRef!!, parser).build()
//        mFirebaseAdapter = object: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(options){
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
//                return MessageViewHolder(
//                    LayoutInflater.from(parent.context).inflate(
//                        R.layout.item_message,
//                        parent,
//                        false
//                    )
//                )
//            }
//
//            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: FriendlyMessage
//            ) {
//                ProgressBar.INVISIBLE
//                if (model.getText() != null) {
//                    holder.messageTextView.text = model.getText()
//                    holder.messageTextView.visibility = TextView.VISIBLE;
//                    holder.messageImageView.visibility = ImageView.GONE;
//                } else if (model.getImageUrl() != null) {
//                    val imageUrl: String? = model.getImageUrl()
//                    if (imageUrl!!.startsWith("gs://")) {
//                        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
//                        storageReference.downloadUrl.addOnCompleteListener { p0 ->
//                            if(p0.isSuccessful){
//                                val downloadUrl: String? = p0.result.toString()
//                                Glide.with(holder.messageImageView.context).load(downloadUrl).into(holder.messageImageView)
//                            }else {
//                                Log.w(TAG, "Getting download url was not successful.", p0.exception)
//                            }
//                        }
//                    } else {
//                        Glide.with(holder.messageImageView.context).load(model!!.getImageUrl()).into(holder.messageImageView)
//                    }
//                    holder.messageImageView.visibility = ImageView.VISIBLE
//                    holder.messageTextView.visibility = TextView.GONE
//                }
//
//                holder.messengerTextView.text = model.getName()
//                if (model.getPhotoUrl() == null) { holder.messengerImageView.setImageDrawable(
//                        ContextCompat.getDrawable(this@MainActivity,
//                            R.drawable.ic_account_circle_black_36dp
//                        ))
//                } else {
//                    Glide.with(this@MainActivity).load(model.getPhotoUrl()).into(holder.messengerImageView)
//                }
//            }
//        }
//
//        mFirebaseAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
//            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
//                super.onItemRangeChanged(positionStart, itemCount, payload)
//                val friendlyMessageCount: Int  = mFirebaseAdapter.itemCount
//                val lastVisiblePosition: Int = mLinearLayoutManager!!.findLastCompletelyVisibleItemPosition()
//
//                if(lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))){
//                    messageRecyclerView.scrollToPosition(positionStart)
//                }
//            }
//        })
//
//
//        messageEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                sendButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
//
//            }
//
//            override fun afterTextChanged(editable: Editable) {}
//        })
//
//        sendButton.setOnClickListener {
//            var friendlyMess = FriendlyMessage(
//                messageEditText.text.toString(),
//                mUsername,
//                mPhotoUrl,
//                null
//            )
//            mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(friendlyMess)
//            messageEditText.setText("")
//        }
//        addMessageImageView.setOnClickListener {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "image/*"
//            startActivityForResult(intent,
//                REQUEST_IMAGE
//            )
//        }
//        if (user != null) {
//        mUsername = user?.displayName.toString()
//            if(user?.photoUrl != null){
//                mPhotoUrl = user?.photoUrl.toString()
//            }
//        }else {
//            val intent = Intent(this, SignInActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in.
//    }
//
//    public override fun onPause() {
//        super.onPause()
//        mFirebaseAdapter.stopListening()
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        mFirebaseAdapter.startListening()
//    }
//
//    public override fun onDestroy() {
//        super.onDestroy()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.sign_out_menu -> {
//                auth.signOut()
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
//                mUsername =
//                    ANONYMOUS
//                val intent = Intent(this, SignInActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> false
//        }
//    }
//
//    override fun onConnectionFailed(connectionResult: ConnectionResult) { // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
//        Log.d(TAG, "onConnectionFailed:$connectionResult")
//        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
//
//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == RESULT_OK) {
//            if (data != null) {
//               val uri: Uri = data.data
//               Log.d(TAG, "Uri: $uri");
//
//              val tempMessage = FriendlyMessage(
//                  null,
//                  mUsername,
//                  mPhotoUrl,
//                  LOADING_IMAGE_URL
//              )
//               mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(tempMessage) { databaseError, databaseReference ->
//                   if (databaseError == null) {
//                       val key: String? = databaseReference.key
//                     val storageReference: StorageReference = FirebaseStorage.getInstance().getReference(user!!.uid).child(key!!).child(uri.lastPathSegment)
//                       putImageInStorage(storageReference, uri, key)
//                   } else {
//                       Log.w(TAG, "Unable to write message to database.", databaseError.toException())
//                   }
//               }
//                 }
//             }
//        }
//    }
//
//    private fun putImageInStorage( storageReference:StorageReference, uri: Uri , key: String ) {
//        storageReference.putFile(uri).addOnCompleteListener(this@MainActivity) { task ->
//            if (task.isSuccessful) {
//                task.result?.metadata?.reference?.downloadUrl?.addOnCompleteListener(this@MainActivity) { task ->
//                    if (task.isSuccessful) {
//                        val friendlyMessage =
//                            FriendlyMessage(
//                                null,
//                                mUsername,
//                                mPhotoUrl,
//                                task.result.toString()
//                            )
//                        mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key).setValue(friendlyMessage)
//                    }
//                }
//            } else {
//                Log.w(TAG, "Image upload task was not successful.", task.exception)
//            }
//        }
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//        const val MESSAGES_CHILD = "messages"
//        private const val REQUEST_INVITE = 1
//        private const val REQUEST_IMAGE = 2
//        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
//        const val DEFAULT_MSG_LENGTH_LIMIT = 10
//        const val ANONYMOUS = "anonymous"
//        private const val MESSAGE_SENT_EVENT = "message_sent"
//        private const val MESSAGE_URL = "http://friendlychat.firebase.google.com/message/"
//    }
//}