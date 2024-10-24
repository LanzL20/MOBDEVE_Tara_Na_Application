package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutNotificationBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.Notification

class AdapterInbox(private val notifications: ArrayList<Notification>): RecyclerView.Adapter<ViewHolderInbox>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInbox {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLayoutNotificationBinding.inflate(inflater, parent, false)
        val viewHolder= ViewHolderInbox(viewBinding)

        viewHolder.itemView.setOnClickListener {
            //TODO: Add a click listener for the notification

        }

        return ViewHolderInbox(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolderInbox, position: Int) {
        holder.bindData(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}