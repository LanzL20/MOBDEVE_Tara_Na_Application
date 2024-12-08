package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutNotificationBinding

class AdapterInbox(private val notifications: ArrayList<Notification>): RecyclerView.Adapter<ViewHolderInbox>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInbox {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLayoutNotificationBinding.inflate(inflater, parent, false)
        val viewHolder= ViewHolderInbox(viewBinding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolderInbox, position: Int) {
        Log.d("ProfileInboxActivity", (notifications::class.java).toString())
        Log.d("ProfileInboxActivity", "Binding data")
        Log.d("ProfileInboxActivity", notifications[position].toString())

        holder.bindData(notifications[position], this, position)
    }

    fun setNotifications(notifications: ArrayList<Notification>, position: Int){
        this.notifications.clear()
        this.notifications.addAll(notifications)
        notifyItemRangeRemoved(position, notifications.size)
    }

    fun removeNotification(position: Int){
        this.notifications.removeAt(position)
        notifyItemRangeRemoved(position, 1)
        notifyItemRangeChanged(0, notifications.size)
    }

    override fun getItemCount(): Int {
        return notifications.size

    }
}