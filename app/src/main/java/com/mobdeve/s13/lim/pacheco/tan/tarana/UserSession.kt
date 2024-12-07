package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.util.Log

object UserSession {
    private lateinit var user: User
    private var isUserLoggedIn: Boolean = false

    fun setUser(user: User) {
        this.user = user
        isUserLoggedIn = true
        Log.e("UserSession", "User set")
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


}