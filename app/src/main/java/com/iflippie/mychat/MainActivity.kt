package com.iflippie.mychat

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    class MessageViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        var messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        var messageImageView: ImageView = itemView.findViewById(R.id.messageImageView)
        var messengerTextView: TextView = itemView.findViewById(R.id.messengerTextView)
        var messengerImageView: CircleImageView = itemView.findViewById(R.id.messengerImageView) as CircleImageView
    }

    private var mUsername: String? = null
    private var mPhotoUrl: String? = null
    private var mSharedPreferences: SharedPreferences? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mSendButton: Button? = null
    private var mMessageRecyclerView: RecyclerView? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mProgressBar: ProgressBar? = null
    private var mMessageEditText: EditText? = null
    private var mAddMessageImageView: ImageView? = null
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    private val user: FirebaseUser? by lazy { auth.currentUser}
    private var mFirebaseDatabaseReference: DatabaseReference? = null
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>

    lateinit var options : FirebaseRecyclerOptions<FriendlyMessage>
    // Firebase instance variables
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // Set default username is anonymous.
        mUsername = MainActivity.Companion.ANONYMOUS
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .build()
        // Initialize ProgressBar and RecyclerView.
        mProgressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        mMessageRecyclerView = findViewById<View>(R.id.messageRecyclerView) as RecyclerView
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
        mMessageRecyclerView!!.layoutManager = mLinearLayoutManager

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        val parser =
            SnapshotParser<FriendlyMessage> { dataSnapshot ->
                val friendlyMessage = dataSnapshot.getValue(FriendlyMessage::class.java)
                friendlyMessage?.setId(dataSnapshot.key!!)
                friendlyMessage!!
            }

        val messagesRef: Query? = mFirebaseDatabaseReference?.child(MESSAGES_CHILD)
        options = FirebaseRecyclerOptions.Builder<FriendlyMessage>().setQuery(messagesRef!!, parser).build()
        mFirebaseAdapter = object: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                return MessageViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
                )
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: FriendlyMessage
            ) {
                mProgressBar!!.visibility = ProgressBar.INVISIBLE
                if (model.getText() != null) {
                    holder.messageTextView.text = model.getText()
                    holder.messageTextView.visibility = TextView.VISIBLE;
                    holder.messageImageView.visibility = ImageView.GONE;
                } else if (model.getImageUrl() != null) {
                    val imageUrl: String? = model.getImageUrl()
                    if (imageUrl!!.startsWith("gs://")) {
                        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                        storageReference.downloadUrl.addOnCompleteListener { p0 ->
                            if(p0.isSuccessful){
                                val downloadUrl: String? = p0.result.toString()
                                Glide.with(holder.messageImageView.context).load(downloadUrl).into(holder.messageImageView)
                            }else {
                                Log.w(TAG, "Getting download url was not successful.", p0.exception)
                            }
                        }
                    } else {
                        Glide.with(holder.messageImageView.context).load(model!!.getImageUrl()).into(holder.messageImageView)
                    }
                    holder.messageImageView.visibility = ImageView.VISIBLE
                    holder.messageTextView.visibility = TextView.GONE
                }

                holder.messengerTextView.text = model.getName()
                if (model.getPhotoUrl() == null) { holder.messengerImageView.setImageDrawable(
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_account_circle_black_36dp))
                } else {
                    Glide.with(this@MainActivity).load(model.getPhotoUrl()).into(holder.messengerImageView)
                }
            }
        }

        mFirebaseAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                val friendlyMessageCount: Int  = mFirebaseAdapter.itemCount
                val lastVisiblePosition: Int = mLinearLayoutManager!!.findLastCompletelyVisibleItemPosition()

                if(lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))){
                    mMessageRecyclerView!!.scrollToPosition(positionStart)
                }
            }
        })

        mMessageEditText = findViewById<View>(R.id.messageEditText) as EditText
        mMessageEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mSendButton!!.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()

            }

            override fun afterTextChanged(editable: Editable) {}
        })
        mSendButton = findViewById<View>(R.id.sendButton) as Button
        mSendButton!!.setOnClickListener {
            // Send messages on click.
        }
        mAddMessageImageView =
            findViewById<View>(R.id.addMessageImageView) as ImageView
        mAddMessageImageView!!.setOnClickListener {
            // Select image for image message on click.
        }
        if (user != null) {
        mUsername = user?.displayName
            if(user?.photoUrl != null){
                mPhotoUrl = user?.photoUrl.toString()
            }
        }else {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in.
// TODO: Add code to check if user is signed in.
    }

    public override fun onPause() {
        super.onPause()
        mFirebaseAdapter?.stopListening()
    }

    public override fun onResume() {
        super.onResume()
        mFirebaseAdapter?.startListening()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                auth.signOut()
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                mUsername = ANONYMOUS
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) { // An unresolvable error has occurred and Google APIs (including Sign-In) will not
// be available.
        Log.d(
            MainActivity.Companion.TAG,
            "onConnectionFailed:$connectionResult"
        )
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        private const val REQUEST_INVITE = 1
        private const val REQUEST_IMAGE = 2
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
        const val DEFAULT_MSG_LENGTH_LIMIT = 10
        const val ANONYMOUS = "anonymous"
        private const val MESSAGE_SENT_EVENT = "message_sent"
        private const val MESSAGE_URL = "http://friendlychat.firebase.google.com/message/"
    }
}