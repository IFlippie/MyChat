package com.iflippie.mychat

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    class MessageViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        var messageTextView: TextView
        var messageImageView: ImageView
        var messengerTextView: TextView
        var messengerImageView: CircleImageView

        init {
            messageTextView = itemView.findViewById(R.id.messageTextView)
            messageImageView =
                itemView.findViewById(R.id.messageImageView)
            messengerTextView = itemView.findViewById(R.id.messengerTextView)
            messengerImageView = itemView.findViewById(R.id.messengerImageView) as CircleImageView
        }
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
        mMessageRecyclerView =
            findViewById<View>(R.id.messageRecyclerView) as RecyclerView
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
        mMessageRecyclerView!!.layoutManager = mLinearLayoutManager
        mProgressBar!!.visibility = ProgressBar.INVISIBLE
        mMessageEditText = findViewById<View>(R.id.messageEditText) as EditText
        mMessageEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
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
    }

    public override fun onResume() {
        super.onResume()
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