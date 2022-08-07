package com.thanarino.kotlinchatapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.thanarino.kotlinchatapp.Message
import com.thanarino.kotlinchatapp.R
import com.thanarino.kotlinchatapp.databinding.MsgRecvBinding
import com.thanarino.kotlinchatapp.databinding.MsgSentBinding

class MessageAdapter(val context: Activity, val messageList: ArrayList<Message>):
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  val ITEM_RECEIVE = 1
  val ITEM_SENT = 2

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    if (viewType == ITEM_RECEIVE) {
      val itemBinding = MsgRecvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return MessageAdapter.ReceiveViewHolder(itemBinding)
    } else {
      val itemBinding = MsgSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return MessageAdapter.SentViewHolder(itemBinding)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    val viewHolder: MessageViewHolder = if(holder.javaClass == SentViewHolder::class.java) {
      holder as SentViewHolder
    } else {
      holder as ReceiveViewHolder
    }

    val currentMessage = messageList[position]
    viewHolder.message.text = currentMessage.message

  }

  override fun getItemViewType(position: Int): Int {
    val currentMessage = messageList[position]
    return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
      ITEM_SENT
    } else {
      ITEM_RECEIVE
    }
  }

  override fun getItemCount(): Int {
    return messageList.size
  }

  open class MessageViewHolder(itemView: View, resId: Int) : RecyclerView.ViewHolder(itemView) {
    val message = itemView.findViewById<TextView>(resId)
  }

  class SentViewHolder(itemView: MsgSentBinding): MessageViewHolder(itemView.root, R.id.txt_sent_message) {

  }

  class ReceiveViewHolder(itemView: MsgRecvBinding): MessageViewHolder(itemView.root, R.id.txt_recv_message) {

  }
}