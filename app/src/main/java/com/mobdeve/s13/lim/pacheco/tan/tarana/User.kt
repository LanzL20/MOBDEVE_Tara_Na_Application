package com.mobdeve.s13.lim.pacheco.tan.tarana

import com.google.type.DateTime
import com.mobdeve.s13.lim.pacheco.tan.tarana.ui.theme.Unavailable

class User {
    companion object {
        const val NAME_KEY = "name"
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
        const val PROFILE_PICTURE_KEY = "profilePicture"
        const val PHONE_NUMBER_KEY = "phoneNumber"
        const val FRIENDS_LIST_KEY = "friendsList"
        const val LAKWATSA_LIST_KEY = "lakwatsaList"
        const val FRIEND_REQUESTS_SENT_KEY = "friendRequestsSent"
        const val FRIEND_REQUESTS_RECEIVED_KEY = "friendRequestsReceived"
        const val UNAVAILABLE_LIST_KEY = "unavailableList"
    }

    var name: String
        private set
    var username: String
        private set
    var password: String
        private set
    var profilePicture: Int
        private set
    var phoneNumber: String
        private set
    var friendsList: ArrayList<User>
        private set
    var lakwatsaList: ArrayList<Lakwatsa>
        private set
    var friendRequestsSent: ArrayList<User>
        private set
    var friendRequestsReceived: ArrayList<User>
        private set
    var unavailableList: ArrayList<Unavailable>
        private set
    //var notificationList: ArrayList<Notification>
    //    private set

    /*
    * Constructor for a new User
     */
    constructor(name:String, username: String, password: String, profilePicture: Int, phoneNumber: String) {
        this.name = name
        this.username = username
        this.password = password
        this.phoneNumber=phoneNumber
        this.profilePicture = profilePicture
        this.friendsList = ArrayList()
        this.lakwatsaList = ArrayList()
        this.friendRequestsSent = ArrayList()
        this.friendRequestsReceived = ArrayList()
        this.unavailableList = ArrayList()
    }
    /*
    * Constructor for a user from the database
     */
    constructor(name:String, username: String, password: String, phoneNumber: String, profilePicture: Int, friendsList: ArrayList<User>, lakwatsaList: ArrayList<Lakwatsa>, friendRequestsSent: ArrayList<User>, friendRequestsReceived: ArrayList<User>, unavailableList: ArrayList<Unavailable>) {
        this.name = name
        this.username = username
        this.password = password
        this.phoneNumber=phoneNumber
        this.profilePicture = profilePicture
        this.friendsList = friendsList
        this.lakwatsaList = lakwatsaList
        this.friendRequestsSent = friendRequestsSent
        this.friendRequestsReceived = friendRequestsReceived
        this.unavailableList = unavailableList
    }

    fun addFriend(user: User) {
        this.friendsList.add(user)
    }

    fun removeFriend(user: User) {
        this.friendsList.remove(user)
    }

    fun addLakwatsa(lakwatsa: Lakwatsa) {
        this.lakwatsaList.add(lakwatsa)
    }

    fun removeLakwatsa(lakwatsa: Lakwatsa) {
        this.lakwatsaList.remove(lakwatsa)
    }

    fun sendFriendRequest(user: User) {
        this.friendRequestsSent.add(user)
        user.friendRequestsReceived.add(this)
    }

    fun acceptFriendRequest(user: User) {
        this.friendRequestsReceived.remove(user)
        user.friendRequestsSent.remove(this)
        this.friendsList.add(user)
        user.friendsList.add(this)
    }

    fun rejectFriendRequest(user: User) {
        this.friendRequestsReceived.remove(user)
        user.friendRequestsSent.remove(this)
    }

    fun cancelFriendRequest(user: User) {
        this.friendRequestsSent.remove(user)
        user.friendRequestsReceived.remove(this)
    }

    fun isFriend(user: User): Boolean {
        return this.friendsList.contains(user)
    }

    fun addUnavailable(unavailable: Unavailable) {
        this.unavailableList.add(unavailable)
    }

    fun removeUnavailable(unavailable: Unavailable) {
        this.unavailableList.remove(unavailable)
    }

    fun updateUnavailableListAtIndex(index: Int, unavailable: Unavailable) {
        this.unavailableList[index] = unavailable
    }

}