package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.util.Date

class Lakwatsa {
    companion object {
        const val LAKWATSA_COMPLETED = 1
        const val LAKWATSA_UPCOMING = 0
        const val LAKWATSA_CANCELLED = -1
        const val ID_KEY = "lakwatsaId"
        const val USERS_KEY = "lakwatsaUsers"
        const val LOCATION_KEY = "location"
        const val TITLE_KEY = "lakwatsaTitle"
        const val DATE_KEY = "date"
        const val POLLING_LIST_KEY = "pollingList"
        const val ALBUM_KEY = "album"
        const val STATUS_KEY = "status"
    }

    var lakwatsaId: String
    var lakwatsaUsers: ArrayList<User>
        private set
    var location: String
        private set
    var lakwatsaTitle: String
        private set
    var date: Date
        private set
    var pollingList: HashMap<Date, Int>
        private set
    var album: ArrayList<String>
        private set
    var status: Int
        private set

    constructor(
        lakwatsaUsers: ArrayList<User>,
        location: String,
        lakwatsaTitle: String,
        date: Date,
        pollingList: HashMap<Date, Int>,
        album: ArrayList<String>
    ) {
        this.lakwatsaId = "-1"
        this.lakwatsaUsers = lakwatsaUsers
        this.location = location
        this.lakwatsaTitle = lakwatsaTitle
        this.date = date
        this.pollingList = pollingList
        this.album = album
        this.status = LAKWATSA_UPCOMING
    }

    constructor(
        lakwatsaId: String,
        lakwatsaUsers: ArrayList<User>,
        location: String,
        lakwatsaTitle: String,
        date: Date,
        pollingList: HashMap<Date, Int>,
        album: ArrayList<String>
    ) {
        this.lakwatsaId = lakwatsaId
        this.lakwatsaUsers = lakwatsaUsers
        this.location = location
        this.lakwatsaTitle = lakwatsaTitle
        this.date = date
        this.pollingList = pollingList
        this.album = album
        this.status = LAKWATSA_UPCOMING
    }

    fun addImage(image: String) {
        this.album.add(image)
    }

    fun removeImage(image: String) {
        this.album.remove(image)
    }

    fun addPolling(date: Date, rating: Int) {
        this.pollingList[date] = rating
    }

    fun removePolling(date: Date) {
        this.pollingList.remove(date)
    }

}