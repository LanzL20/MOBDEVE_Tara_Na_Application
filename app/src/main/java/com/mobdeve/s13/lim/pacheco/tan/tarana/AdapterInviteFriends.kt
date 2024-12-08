package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterInviteFriends(private val items: List<InviteFriendItem>, private val context: Context) :
    RecyclerView.Adapter<AdapterInviteFriends.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friendImageView: ImageView = view.findViewById(R.id.item_layout_friend_to_invite_iv)
        val friendName: TextView = view.findViewById(R.id.item_layout_friend_to_invite_name)
        val friendUsername: TextView = view.findViewById(R.id.item_layout_friend_to_invite_username)
        val inviteCheckbox: CheckBox = view.findViewById(R.id.invite_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_invite_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.friendImageView.setImageResource(item.imageResId)
        holder.friendName.text = item.name
        holder.friendUsername.text = item.username
        holder.inviteCheckbox.isChecked = item.isChecked
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

data class InviteFriendItem(
    val imageResId: Int,
    val name: String,
    val username: String,
    val isChecked: Boolean
)
