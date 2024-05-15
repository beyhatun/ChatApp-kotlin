package com.onurbeyhatun.chatapp.activity

import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.onurbeyhatun.chatapp.R
import com.onurbeyhatun.chatapp.adapter.MessageAdapter
import com.onurbeyhatun.chatapp.databinding.ActivityChatBinding
import com.onurbeyhatun.chatapp.model.MessageModel
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChatBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var senderUid:String
    private lateinit var receiverUid:String
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String

    private lateinit var list:ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        senderUid=FirebaseAuth.getInstance().uid.toString()
        receiverUid=intent.getStringExtra("uid")!!

        list=ArrayList()

        senderRoom=senderUid+receiverUid
        receiverRoom=receiverUid+senderUid

        database=FirebaseDatabase.getInstance()

        binding.imageView2.setOnClickListener {
            if(binding.messageBox.text.isEmpty()){
                Toast.makeText(this,"Lutfen mesajinizi girin...",Toast.LENGTH_SHORT).show()
            }else{
                        val message= MessageModel(binding.messageBox.text.toString(),senderUid,Date().time)

                val randomKey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {

                    database.reference.child("chats")
                        .child(receiverRoom).child("message").child(randomKey!!).setValue(message).addOnSuccessListener {

                            binding.messageBox.text=null
                            Toast.makeText(this,"Mesaj gonder!!",Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    binding.recyclerView.adapter=MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"Hata: ${error.message}",Toast.LENGTH_SHORT).show()
                }


            })
    }
}