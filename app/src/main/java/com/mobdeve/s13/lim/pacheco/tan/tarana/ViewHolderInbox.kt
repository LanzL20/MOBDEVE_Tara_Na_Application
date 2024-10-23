package com.mobdeve.s13.lim.pacheco.tan.tarana


import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutNotificationBinding
import com.mobdeve.s13.lim.pacheco.tan.tarana.Notification
class ViewHolderInbox(private var viewBinding: ItemLayoutNotificationBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    //TODO: coordinate with the Notification class
    //TODO: maybe add a date field to the Notification class
    fun bindData(notification: Notification) {
        viewBinding.activityProfileInboxUser1Iv.setImageResource(notification.user.profilePicture)
        viewBinding.activityProfileInboxMessageTv.text = notification.message
    }


}