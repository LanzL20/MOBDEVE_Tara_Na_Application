package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.util.Log

object UserSession {
    private lateinit var user: User
    private var isUserLoggedIn: Boolean = false
    private var hasUnread: Boolean = false
    private var oldNotificationList: ArrayList<Notification> = arrayListOf()
    fun setUser(user: User) {
        oldNotificationList = user.notificationList
        this.user = user
        isUserLoggedIn = true
        Log.e("UserSession", "User set")
        if(user.notificationList.size > oldNotificationList.size){
            hasUnread = true
        }
        // check if notification list has unread notifications
        for (notification in user.notificationList) {
            if (!notification.isRead) {
                hasUnread = true
                break
            }
        }

    }

    fun getUser(): User {
        return user
    }

    fun clearUser() {
        isUserLoggedIn = false
    }

    fun isLoggedIn(): Boolean {
        return isUserLoggedIn
    }

    fun setUnreadNotifications(hasUnread: Boolean) {
        this.hasUnread = hasUnread
    }

    fun hasUnreadNotifications(): Boolean {
        return hasUnread
    }


}