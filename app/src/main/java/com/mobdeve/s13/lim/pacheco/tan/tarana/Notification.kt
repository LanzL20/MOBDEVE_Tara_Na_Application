package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.util.Date

// abstract class Notification
class Notification{
    companion object{
        const val FRIEND_REQUEST_PENDING = 1L
        const val LAKWATSA_INVITE = 3L
        const val LAKWATSA_ON_GOING = 4L
        const val LAKWATSA_ENDED = 5L


        const val MESSAGE_KEY = "message"
        const val DATE_KEY = "date"
        const val IS_READ_KEY = "isRead"
        const val TYPE_KEY = "type"
        const val SENDER_KEY = "sender"
        const val RECEIVER_KEY = "receiver"
        const val IMAGE_KEY = "image"

        const val FRIEND_REQUEST_MESSAGE = "wants to be your friend!"
        const val LAKWATSA_INVITE_MESSAGE = "You have been added to lakwatsa: "
        const val LAKWATSA_ON_GOING_MESSAGE = "lakwatsa has started!"
        const val LAKWATSA_ENDED_MESSAGE = "lakwatsa has ended!"
    }

    var message: String
        private set
    var date: Date
        private set
    var isRead: Boolean
        private set
    var type: Long
        private set
    var sender: String
        private set
    var receiver: String
        private set
    var image:Long
        private set
    /// var user: User
        // private set

    // constructor
    constructor(message: String, date: Date, isRead: Boolean, sender: String,receiver:String, type: Long, image:Long){
        this.message = message
        this.date = date
        this.isRead = isRead
        // this.user = user
        this.sender = sender
        this.receiver = receiver
        this.type = type
        this.image = image
    }

    fun markAsRead(){
        this.isRead = true
    }

    fun getDrawable(): Int {
        if(this.image==1L){
            return R.drawable.asset_profile1
        }

        if(this.image==2L){
            return R.drawable.asset_profile2
        }

        if(this.image==3L){
            return R.drawable.asset_profile3
        }
        if(this.image==4L){
            return R.drawable.asset_profile4
        }
        return R.drawable.logo_chat
    }

}