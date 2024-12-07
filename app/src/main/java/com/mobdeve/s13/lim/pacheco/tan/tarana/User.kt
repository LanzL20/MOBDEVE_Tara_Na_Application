package com.mobdeve.s13.lim.pacheco.tan.tarana

import com.google.type.LatLng

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
        const val UID_KEY = "uid"
        const val LOCATION_KEY = "location"
        const val SALT_KEY = "salt"
    }

    override fun toString(): String {
        return "User(username='$username')"
    }

    override fun hashCode(): Int {
        return username.hashCode()
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
    var friendsList: ArrayList<String>
        private set
    var lakwatsaList: ArrayList<String>
        private set
    var friendRequestsSent: ArrayList<String>
        private set
    var friendRequestsReceived: ArrayList<String>
        private set
    var unavailableList: ArrayList<Unavailable>
        private set
    var uid: String
        private set
    var location: LatLng
        private set
    var salt: String
        private set
    //var notificationList: ArrayList<Notification>
    //    private set

    /*
    * Constructor for a new User
     */
    constructor(
        name: String,
        username: String,
        password: String,
        profilePicture: Int,
        phoneNumber: String,
        salt: String
    ) {
        this.name = name
        this.username = username
        this.password = password
        this.phoneNumber = phoneNumber
        this.profilePicture = profilePicture
        this.friendsList = ArrayList()
        this.lakwatsaList = ArrayList()
        this.friendRequestsSent = ArrayList()
        this.friendRequestsReceived = ArrayList()
        this.unavailableList = ArrayList()
        this.uid = username
        this.location = LatLng.newBuilder().setLatitude(0.0).setLongitude(0.0).build()
        this.salt = salt
    }

    /*
    * Constructor for a user from the database
     */
    constructor(
        name: String,
        username: String,
        password: String,
        phoneNumber: String,
        profilePicture: Int,
        friendsList: ArrayList<String>,
        lakwatsaList: ArrayList<String>,
        friendRequestsSent: ArrayList<String>,
        friendRequestsReceived: ArrayList<String>,
        unavailableList: ArrayList<Unavailable>,
        uid: String,
        location: LatLng,
        salt: String
    ) {
        this.name = name
        this.username = username
        this.password = password
        this.phoneNumber = phoneNumber
        this.profilePicture = profilePicture
        this.friendsList = friendsList
        this.lakwatsaList = lakwatsaList
        this.friendRequestsSent = friendRequestsSent
        this.friendRequestsReceived = friendRequestsReceived
        this.unavailableList = unavailableList
        this.uid = uid
        this.location = location
        this.salt = salt
    }

    fun addFriend(userId: String) {
        this.friendsList.add(userId)
    }

    fun removeFriend(userId: String) {
        this.friendsList.remove(userId)
    }

    fun addLakwatsa(lakwatsaId: String) {
        this.lakwatsaList.add(lakwatsaId)
    }

    fun removeLakwatsa(lakwatsaId: String) {
        this.lakwatsaList.remove(lakwatsaId)
    }

    fun sendFriendRequest(userId: String) {
        this.friendRequestsSent.add(userId)
    }

    fun receiveFriendRequest(userId: String) {
        this.friendRequestsReceived.add(userId)
    }

    fun acceptFriendRequest(userId: String) {
        this.friendRequestsReceived.remove(userId)
        this.friendsList.add(userId)
    }

    fun friendRequestAccepted(userId: String) {
        this.friendRequestsSent.remove(userId)
        this.friendsList.add(userId)
    }

    fun rejectFriendRequest(userId: String) {
        this.friendRequestsReceived.remove(userId)
    }

    fun friendRequestRejected(userId: String) {
        this.friendRequestsSent.remove(userId)
    }

    fun cancelFriendRequest(userId: String) {
        this.friendRequestsSent.remove(userId)
    }

    fun friendRequestCancelled(userId: String) {
        this.friendRequestsReceived.remove(userId)
    }

    fun isFriend(userId: String): Boolean {
        return this.friendsList.contains(userId)
    }

    fun addUnavailable(unavailable: Unavailable) {
        this.unavailableList.add(unavailable)
    }

    fun removeUnavailable(unavailable: Unavailable) {
        this.unavailableList.remove(unavailable)
    }

    fun removeUnavailableAtIndex(index: Int) {
        this.unavailableList.removeAt(index)
    }

    fun updateUnavailableListAtIndex(index: Int, unavailable: Unavailable) {
        this.unavailableList[index] = unavailable
    }

    fun setSalt(salt: String) {
        this.salt = salt
    }

    fun getSalt(): String {
        return this.salt
    }

    fun setLocation(location: LatLng) {
        this.location = location
    }

    fun getLocation(): LatLng {
        return this.location
    }

}