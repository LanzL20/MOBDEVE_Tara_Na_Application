package com.mobdeve.s13.lim.pacheco.tan.tarana

class User {
    var username: String
        private set
    var password: String
        private set
    var email: String
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
    //var notificationList: ArrayList<Notification>
    //    private set

    /*
    * Constructor for a new User
     */
    constructor(username: String, password: String, email: String, profilePicture: Int, phoneNumber: String) {
        this.username = username
        this.password = password
        this.email = email
        this.phoneNumber=phoneNumber
        this.profilePicture = profilePicture
        this.friendsList = ArrayList()
        this.lakwatsaList = ArrayList()
        this.friendRequestsSent = ArrayList()
        this.friendRequestsReceived = ArrayList()
    }
    /*
    * Constructor for a user from the database
     */
    constructor(username: String, password: String, email: String, profilePicture: Int, phoneNumber: String, friendsList: ArrayList<User>, lakwatsaList: ArrayList<Lakwatsa>, friendRequestsSent: ArrayList<User>, friendRequestsReceived: ArrayList<User>) {
        this.username = username
        this.password = password
        this.email = email
        this.phoneNumber=phoneNumber
        this.profilePicture = profilePicture
        this.friendsList = friendsList
        this.lakwatsaList = lakwatsaList
        this.friendRequestsSent = friendRequestsSent
        this.friendRequestsReceived = friendRequestsReceived
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



}