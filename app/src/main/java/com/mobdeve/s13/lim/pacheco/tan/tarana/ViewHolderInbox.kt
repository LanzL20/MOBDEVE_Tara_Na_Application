package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutNotificationBinding
import kotlinx.coroutines.launch

class ViewHolderInbox(private var viewBinding: ItemLayoutNotificationBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(notification: Notification, adapter: AdapterInbox, position: Int) {
        Log.d("ProfileInboxActivity", "Binding data")
        // viewBinding.activityProfileInboxUserIv.setImageResource(notification.user.profilePicture)
        viewBinding.activityProfileInboxMessageTv.text = notification.message

        if(notification.type!=Notification.FRIEND_REQUEST_PENDING){
            viewBinding.activityProfileInboxFriendReqBtnLl.visibility = View.GONE
            //set image to default chat icon @drawable/logo_chat
            viewBinding.activityProfileInboxUserIv.setImageResource(R.drawable.logo_chat)
        }
        else{
            viewBinding.activityProfileInboxFriendReqBtnLl.visibility = View.VISIBLE
            viewBinding.activityProfileInboxUserIv.setImageResource(notification.getDrawable())
            viewBinding.acceptBtn.setOnClickListener {
                //ADD FUNCTIONALITY TO ACCEPT FRIEND REQUEST
                (viewBinding.root.context as AppCompatActivity).lifecycleScope.launch {
                    val senderUser= DBHelper.getUserbyUid(notification.sender)
                    senderUser.friendRequestsSent.remove(notification.receiver)
                    senderUser.friendsList.add(notification.receiver)
                    DBHelper.updateUser(senderUser)
                    Log.d("ViewHolderInbox", "Sender User: ${senderUser}")

                    val receiverUser= UserSession.getUser()
                    receiverUser.friendRequestsReceived.remove(notification.sender)
                    receiverUser.friendsList.add(notification.sender)
                    receiverUser.notificationList.removeAt(position)
                    DBHelper.updateUser(receiverUser)
                    Log.d("ViewHolderInbox", "Receiver User: ${receiverUser.uid}")
                    adapter.removeNotification(position)
                }
            }

            viewBinding.declineBtn.setOnClickListener {
                //ADD FUNCTIONALITY TO DECLINE FRIEND REQUEST
                (viewBinding.root.context as AppCompatActivity).lifecycleScope.launch {
                    val senderUser= DBHelper.getUserbyUid(notification.sender)
                    senderUser.friendRequestsSent.remove(notification.receiver)
                    DBHelper.updateUser(senderUser)
                    val receiverUser= UserSession.getUser()
                    receiverUser.friendRequestsReceived.remove(notification.sender)
                    receiverUser.notificationList.removeAt(position)
                    DBHelper.updateUser(receiverUser)
                    adapter.removeNotification(position)

                }
            }
        }

        viewBinding.optionsIcon.setOnClickListener { view ->
            val contextWrapper = ContextThemeWrapper(itemView.context, R.style.CustomPopupMenu)
            val popupMenu = PopupMenu(contextWrapper, view)

            popupMenu.menu.add(0, 1, 0, "Delete notification")
            popupMenu.menu.add(0, 2, 1, "Mark as read")

            popupMenu.show()
        }
    }
}