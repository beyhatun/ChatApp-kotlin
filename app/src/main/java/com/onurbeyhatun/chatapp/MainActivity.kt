package com.onurbeyhatun.chatapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.onurbeyhatun.chatapp.activity.NumberActivity
import com.onurbeyhatun.chatapp.adapter.ViewPagerAdapter
import com.onurbeyhatun.chatapp.databinding.ActivityMainBinding
import com.onurbeyhatun.chatapp.ui.CallFragment
import com.onurbeyhatun.chatapp.ui.ChatFragment
import com.onurbeyhatun.chatapp.ui.StatusFragment

class MainActivity : AppCompatActivity() {

  var binding: ActivityMainBinding?=null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view =binding!!.root
        setContentView(view)

        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, NumberActivity::class.java))
            finish()
        }

        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding!!.viewPAger.adapter=adapter

        binding!!.tabs.setupWithViewPager(binding!!.viewPAger)


    }
}