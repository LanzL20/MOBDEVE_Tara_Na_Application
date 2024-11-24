package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.time.LocalDateTime
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
        const val ALBUM_COLOR_KEY = "albumColor"
        const val STATUS_KEY = "status"
    }

    var lakwatsaId: String
    // TODO: SHUD ONLY BE USERID
    var lakwatsaUsers: ArrayList<User>
        private set
    var location: String
        private set
    var lakwatsaTitle: String
        private set
    var date: LocalDateTime
        private set
    var pollingList: HashMap<LocalDateTime, Int>
        private set
    var album: ArrayList<String>
        private set
    var status: Int
        private set

    constructor(
        lakwatsaUsers: ArrayList<User>,
        location: String,
        lakwatsaTitle: String,
        date: LocalDateTime,
        pollingList: HashMap<LocalDateTime, Int>,
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
        date: LocalDateTime,
        pollingList: HashMap<LocalDateTime, Int>,
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

    fun setLakwatsaTitle(newTitle: String) {
        lakwatsaTitle = newTitle
    }

    fun addImage(image: String) {
        this.album.add(image)
    }

    fun removeImage(image: String) {
        this.album.remove(image)
    }

    fun addPolling(date: LocalDateTime, rating: Int) {
        this.pollingList[date] = rating
    }

    fun removePolling(date: LocalDateTime) {
        this.pollingList.remove(date)
    }

}