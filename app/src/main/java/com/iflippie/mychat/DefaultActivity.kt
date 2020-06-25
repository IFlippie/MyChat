package com.iflippie.mychat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.iflippie.mychat.database.ColoursRepository
import kotlinx.android.synthetic.main.activity_default.*
import kotlinx.android.synthetic.main.dialog_query_email.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DefaultActivity : AppCompatActivity() {

    private val setColor = arrayListOf<ColorItem>()
    private lateinit var coloursRepository: ColoursRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)
        setSupportActionBar(toolbar)

        coloursRepository = ColoursRepository(this)
        //setColor.add(ColorItem("000000", "Black"))
        getRemindersFromDatabase()
        for (i in setColor){
            val thisColor = i.hex
            toolbar.setBackgroundColor(Color.parseColor("#${thisColor}"))
        }

        fab.setOnClickListener {
            createRoom()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_see_settings -> {
                //replaceFragment(SecondFragment())
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_FirstFragment_to_SecondFragment)
                true
            }
            else -> false
        }
    }

    private fun getRemindersFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val color = withContext(Dispatchers.IO) {
                coloursRepository.getAllColors()
            }
            this@DefaultActivity.setColor.clear()
            this@DefaultActivity.setColor.addAll(color)
        }
    }

    private fun createRoom(){
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.dialog_query_email)

        dialog.btnCheck.setOnClickListener {
            val fEmail = dialog.et_friend_email.text.toString()
            if(fEmail.isNotEmpty()){
                val intent = Intent(baseContext, QueryActivity::class.java)
                intent.putExtra(ADD_USER_EMAIL, fEmail)
                startActivity(intent)
            }else {
                Toast.makeText(baseContext, "email is empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.commit()
    }
    companion object {
        const val ADD_USER_EMAIL = "ADD_USER_EMAIL"
    }

}
