package com.mobdeve.s13.lim.pacheco.tan.tarana

import com.google.type.LatLng
import java.time.LocalDateTime
import java.util.Date

class Lakwatsa {
    companion object {
        const val LAKWATSA_COMPLETED = 2
        const val LAKWATSA_ONGOING = 1
        const val LAKWATSA_UPCOMING = 0
        const val ID_KEY = "lakwatsaId"
        const val USERS_KEY = "lakwatsaUsers"
        const val LATITUDE_KEY = "locationLatitude"
        const val LONGITUDE_KEY = "locationLongitude"
        const val TITLE_KEY = "lakwatsaTitle"
        const val DATE_KEY = "date"
        const val POLLING_LIST_KEY = "pollingList"
        const val ALBUM_KEY = "album"
        const val STATUS_KEY = "status"
        const val TIME_KEY = "time"
        const val ADMIN_KEY = "lakwatsaAdmin"
        const val LOCATION_NAME_KEY = "locationName"
    }

    var lakwatsaId: String
        private set
    var lakwatsaUsers: ArrayList<String>
        private set
    var lakwatsaAdmin: String
        private set
    var locationLatitude: Double
        private set
    var locationLongitude: Double
        private set
    var locationName: String
        private set
    var lakwatsaTitle: String
        private set
    var date: String
        private set
    var time: String = ""
        private set
    var pollingList: HashMap<String, ArrayList<String>>
        private set
    var album: ArrayList<String>
        private set
    var status: Int
        private set

    // Only for initial creation...
    constructor(
        lakwatsaUsers: ArrayList<String>,
        lakwatsaTitle: String,
        date: String,
        lakwatsaAdmin: String
    ) {
        this.lakwatsaId = "0"
        this.lakwatsaUsers = lakwatsaUsers
        this.lakwatsaTitle = lakwatsaTitle
        this.date = date
        this.pollingList = HashMap()
        this.album = ArrayList()
        this.status = LAKWATSA_UPCOMING
        this.lakwatsaAdmin = lakwatsaAdmin
        this.locationLatitude = 0.0
        this.locationLongitude = 0.0
        this.locationName = ""
    }

    constructor(
        lakwatsaId: String,
        lakwatsaUsers: ArrayList<String>,
        locationLatitude: Double,
        locationLongitude: Double,
        lakwatsaTitle: String,
        date: String,
        time: String,
        pollingList: HashMap<String, ArrayList<String>>,
        album: ArrayList<String>,
        status: Int,
        lakwatsaAdmin: String,
        locationName: String
    ) {
        this.lakwatsaId = lakwatsaId
        this.lakwatsaUsers = lakwatsaUsers
        this.locationLatitude = locationLatitude
        this.locationLongitude = locationLongitude
        this.lakwatsaTitle = lakwatsaTitle
        this.date = date
        this.time = time
        this.pollingList = pollingList
        this.album = album
        this.status = status
        this.lakwatsaAdmin = lakwatsaAdmin
        this.locationName = locationName
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

    fun addPollingOption(date: String) {
        this.pollingList[date] = ArrayList()
    }

    fun addPolling(date: String, user: String) {
        this.pollingList[date]?.add(user)
    }

    fun removePolling(
        date: String,
        user: String
    ) {
        this.pollingList[date]?.remove(user)
    }

    fun setLocationName(locationName: String) {
        this.locationName = locationName
    }

}