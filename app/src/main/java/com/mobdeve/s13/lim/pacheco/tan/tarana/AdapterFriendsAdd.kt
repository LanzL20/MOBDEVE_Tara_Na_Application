package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutFriendToAddBinding

class AdapterFriendsAdd(private var friendList: ArrayList<User>) : RecyclerView.Adapter<ViewHolderFriendsAdd>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFriendsAdd {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLayoutFriendToAddBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolderFriendsAdd(viewBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFriendsAdd, position: Int) {
        holder.bindData(friendList[position])
        holder.itemView.setOnClickListener(){
            val intent = Intent(holder.itemView.context, ProfileFriendActivity::class.java)
            intent.putExtra(ProfileFriendActivity.USER_KEY, friendList[position].username)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun setData(friendList: ArrayList<User>){
        this.friendList = friendList
        notifyDataSetChanged()
    }



}
