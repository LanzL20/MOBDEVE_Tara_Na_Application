package com.mobdeve.s13.lim.pacheco.tan.tarana

object UserSession {
    private var user: User? = null

    fun setUser(user: User) {
        this.user = user
    }

    fun getUser(): User? {
        return this.user
    }

    fun clearUser() {
        this.user = null
    }

    fun isLoggedIn(): Boolean {
        return this.user != null
    }

    fun getUsername(): String {
        return this.user!!.username
    }

    fun getName(): String {
        return this.user!!.name
    }

    fun getProfilePicture(): Int {
        return this.user!!.profilePicture
    }

    fun getPhoneNumber(): String {
        return this.user!!.phoneNumber
    }

    fun getFriendsList(): ArrayList<User> {
        return this.user!!.friendsList
    }

    fun getLakwatsaList(): ArrayList<Lakwatsa> {
        return this.user!!.lakwatsaList
    }

    fun getFriendRequestsSent(): ArrayList<User> {
        return this.user!!.friendRequestsSent
    }

    fun getFriendRequestsReceived(): ArrayList<User> {
        return this.user!!.friendRequestsReceived
    }

    fun getUnavailableList(): ArrayList<Unavailable> {
        return this.user!!.unavailableList
    }

    fun addFriend(user: User) {
        this.user!!.friendsList.add(user)
    }

    fun addLakwatsa(lakwatsa: Lakwatsa) {
        this.user!!.lakwatsaList.add(lakwatsa)
    }

    fun sendFriendRequest(user: User) {
        this.user!!.friendRequestsSent.add(user)
        user.friendRequestsReceived.add(this.user!!)
    }

    fun acceptFriendRequest(user: User) {
        this.user!!.friendRequestsReceived.remove(user)
        user.friendRequestsSent.remove(this.user!!)
        this.user!!.friendsList.add(user)
        user.friendsList.add(this.user!!)
    }

    fun removeFriend(user: User) {
        this.user!!.friendsList.remove(user)
    }

    fun removeLakwatsa(lakwatsa: Lakwatsa) {
        this.user!!.lakwatsaList.remove(lakwatsa)
    }

    fun removeFriendRequest(user: User) {
        this.user!!.friendRequestsSent.remove(user)
        user.friendRequestsReceived.remove(this.user!!)
    }

    fun removeUnavailable(unavailable: Unavailable) {
        this.user!!.unavailableList.remove(unavailable)
    }

    fun isFriend(user: User): Boolean {
        return this.user!!.friendsList.contains(user)
    }

    fun addUnavailable(unavailable: Unavailable) {
        this.user!!.unavailableList.add(unavailable)
    }

    fun rejectFriendRequest(user: User) {
        this.user!!.friendRequestsReceived.remove(user)
        user.friendRequestsSent.remove(this.user!!)
    }

    fun cancelFriendRequest(user: User) {
        this.user!!.friendRequestsSent.remove(user)
        user.friendRequestsReceived.remove(this.user!!)
    }


}