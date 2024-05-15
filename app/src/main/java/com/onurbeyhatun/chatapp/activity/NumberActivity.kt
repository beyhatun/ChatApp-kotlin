package com.onurbeyhatun.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.onurbeyhatun.chatapp.MainActivity
import com.onurbeyhatun.chatapp.R
import com.onurbeyhatun.chatapp.databinding.ActivityMainBinding
import com.onurbeyhatun.chatapp.databinding.ActivityNumberBinding

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.button3.setOnClickListener {
            if (binding.phoneNumber.text!!.isEmpty()) {
                Toast.makeText(this, "Lutfen telefon numarasi girin!!", Toast.LENGTH_LONG).show()
            } else {
                var intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("number",binding.phoneNumber.text!!.toString())
                startActivity(intent)


            }


        }
    }
}