package com.thanarino.kotlinchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.thanarino.kotlinchatapp.adapter.MessageAdapter
import com.thanarino.kotlinchatapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

  private lateinit var msgRecyclerView: RecyclerView
  private lateinit var msgBox: EditText
  private lateinit var btnSend: ImageView
  private lateinit var messageAdapter: MessageAdapter
  private lateinit var messageList: ArrayList<Message>

  private lateinit var mDbRef: DatabaseReference

  private lateinit var binding: ActivityChatBinding

  var receiverRoom: String? = null
  var senderRoom: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityChatBinding.inflate(layoutInflater)
    val rootView = binding.root
    setContentView(rootView)

    val name = intent.getStringExtra("NAME")
    val rcvUid = intent.getStringExtra("UID")
    val senderUid = FirebaseAuth.getInstance().currentUser?.uid

    mDbRef = Firebase.database.reference

    senderRoom = rcvUid + senderUid
    receiverRoom = senderUid + rcvUid

    supportActionBar?.title = name

    msgRecyclerView = binding.rvChat
    msgBox = binding.edtChatbox
    btnSend = binding.btnSend
    messageList = ArrayList()
    messageAdapter = MessageAdapter(this, messageList)

    msgRecyclerView.layoutManager = LinearLayoutManager(this)
    msgRecyclerView.adapter = messageAdapter

    mDbRef.child("chats").child(senderRoom!!).child("messages")
      .addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          messageList.clear()
          snapshot.children.forEach { messageList.add(it.getValue(Message::class.java)!!) }
          messageAdapter.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {

        }

      })

    btnSend.setOnClickListener {
      val txtMsg = msgBox.text.toString()
      val message = Message(txtMsg, senderUid)

      mDbRef.child("chats").child(senderRoom!!).child("messages")
        .push().setValue(message).addOnSuccessListener {
          mDbRef.child("chats").child(receiverRoom!!).child("messages")
            .push().setValue(message).addOnSuccessListener {
              msgBox.text = "".toEditable()
            }
        }
    }
  }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
