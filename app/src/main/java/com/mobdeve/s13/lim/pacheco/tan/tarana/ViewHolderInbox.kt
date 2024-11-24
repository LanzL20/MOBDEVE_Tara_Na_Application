package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutNotificationBinding

class ViewHolderInbox(private var viewBinding: ItemLayoutNotificationBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(notification: Notification) {
        // viewBinding.activityProfileInboxUserIv.setImageResource(notification.user.profilePicture)
        viewBinding.activityProfileInboxMessageTv.text = notification.message

        viewBinding.activityProfileInboxFriendReqBtnLl.visibility =
            if (notification.isFriendRequest) View.VISIBLE else View.GONE

        viewBinding.optionsIcon.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(itemView.context, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Delete notification")
            popupMenu.menu.add(0, 2, 1, "Mark as read")

            popupMenu.show()
        }
    }
}