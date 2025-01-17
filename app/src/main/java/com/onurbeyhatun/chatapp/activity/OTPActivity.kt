package com.onurbeyhatun.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.onurbeyhatun.chatapp.R
import com.onurbeyhatun.chatapp.databinding.ActivityMainBinding
import com.onurbeyhatun.chatapp.databinding.ActivityOtpactivityBinding
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOtpactivityBinding
    private lateinit var auth : FirebaseAuth
    private var verificationId:String?=null
    private lateinit var dialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpactivityBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        auth= FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)

        builder.setMessage("Lutfen Bekleyin...")
        builder.setTitle("Yukleniyor")
        builder.setCancelable(false)
        dialog=builder.create()
        dialog.show()

        val phoneNumber = "+90"+intent.getStringExtra("number")

        Log.d("FAZIL","onCreate: " + phoneNumber)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OTPActivity,"Lutfen Tekrar Deneyin!!  ${p0}",Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationId = p0
                    //Toast.makeText(this@OTPActivity, "OTP Kodu Gönderildi", Toast.LENGTH_SHORT).show()

                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        binding.button3.setOnClickListener {
            if (verificationId == null) {
                    Toast.makeText(this, "Doğrulama Kodu Alınmadı", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
            }
            if(binding.otp.text!!.isEmpty()){
                Toast.makeText(this,"Lutfen kodu girin",Toast.LENGTH_SHORT).show()
            }else{
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationId!!,binding.otp.text!!.toString())
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                dialog.dismiss()
                                    startActivity(Intent(this,ProfileActivity::class.java))
                                finish()
                            }else{
                                dialog.dismiss()
                                Toast.makeText(this,"Hata${it.exception}",Toast.LENGTH_SHORT).show()
                            }
                        }

            }
        }

    }
    }

