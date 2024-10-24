package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.util.Date

// abstract class Notification
abstract class Notification{
    var message: String
        private set
    var date: Date
        private set
    var isRead: Boolean
        private set
    var user: User
        private set

    // constructor
    constructor(message: String, date: Date, isRead: Boolean, user: User){
        this.message = message
        this.date = date
        this.isRead = isRead
        this.user = user
    }





}