package com.mobdeve.s13.lim.pacheco.tan.tarana

object UserSession {
    private lateinit var user: User
    private var isUserLoggedIn: Boolean = false

    fun setUser(user: User) {
        this.user = user
        isUserLoggedIn = true
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