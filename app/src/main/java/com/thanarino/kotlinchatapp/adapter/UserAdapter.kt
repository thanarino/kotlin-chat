package com.thanarino.kotlinchatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.thanarino.kotlinchatapp.ChatActivity
import com.thanarino.kotlinchatapp.databinding.UserLayoutBinding
import com.thanarino.kotlinchatapp.objects.User

class UserAdapter(val context: Context, val userList: ArrayList<User>):
  RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
    val itemBinding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return UserViewHolder(itemBinding, context)
  }

  override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
    val userBean: User = userList[position]
    holder.bind(userBean)
  }

  override fun getItemCount(): Int = userList.size

  class UserViewHolder(private val itemBinding: UserLayoutBinding, val context: Context) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(userBean: User) {
      itemBinding.tvName.text = userBean.username
      itemBinding.root.setOnClickListener {
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("NAME", userBean.username)
        intent.putExtra("UID", userBean.uid)
        context.startActivity(intent)
      }
    }
  }

}