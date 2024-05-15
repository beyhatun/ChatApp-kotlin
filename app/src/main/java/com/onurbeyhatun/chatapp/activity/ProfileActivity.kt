package com.onurbeyhatun.chatapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.onurbeyhatun.chatapp.MainActivity
import com.onurbeyhatun.chatapp.R
import com.onurbeyhatun.chatapp.databinding.ActivityMainBinding
import com.onurbeyhatun.chatapp.databinding.ActivityProfileBinding
import com.onurbeyhatun.chatapp.model.UserModel
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dialog = AlertDialog.Builder(this).setMessage("Profil Yukleniyor...").setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        binding.continueBtn.setOnClickListener {
            if (binding.userName.text!!.isEmpty()) {
                Toast.makeText(this, "Lutfen adinizi giriniz..", Toast.LENGTH_SHORT).show()
            } else if (selectedImg == null) {
                Toast.makeText(this, "Lutfen Fotografinizi giriniz...", Toast.LENGTH_SHORT).show()
            } else uploadData()
        }

    }

    private fun uploadData() {
        val referance = storage.reference.child("Profile").child(Date().time.toString())
        referance.putFile(selectedImg).addOnCompleteListener {
            if (it.isSuccessful) {
                referance.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }

    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(
            auth.uid.toString(),
            binding.userName.text.toString(),
            auth.currentUser!!.phoneNumber.toString(),
            imgUrl
        )

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
    }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (data != null) {
                if (data.data != null) {
                    selectedImg = data.data!!

                    binding.userImage.setImageURI(selectedImg)
                }
            }
        }
    }
