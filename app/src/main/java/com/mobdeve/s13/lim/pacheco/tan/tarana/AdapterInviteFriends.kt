package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterInviteFriends(private val items: ArrayList<InviteFriendItem>, private val context: Context) :
    RecyclerView.Adapter<AdapterInviteFriends.ViewHolder>() {

    private var filteredItems = items

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
        val item = filteredItems[position]
        holder.friendImageView.setImageResource(item.imageResId)
        holder.friendName.text = item.name
        holder.friendUsername.text = item.username
        holder.inviteCheckbox.isChecked = item.isChecked

        holder.inviteCheckbox.setOnClickListener{
            filteredItems[position].isChecked = holder.inviteCheckbox.isChecked
        }
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    fun filterItems(query: String) {
        filteredItems = ArrayList()
        for (item in items) {
            if (item.name.contains(query, ignoreCase = true) || item.username.contains(query, ignoreCase = true)) {
                filteredItems.add(item)
            }
        }
        notifyDataSetChanged()
        notifyItemRangeChanged(0, filteredItems.size)
    }

}

data class InviteFriendItem(
    val imageResId: Int,
    val name: String,
    val username: String,
    var isChecked: Boolean
)
