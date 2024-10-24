package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.util.Date

class Lakwatsa {
   companion object{
       const val LAKWATSA_COMPLETED = 1
       const val LAKWATSA_UPCOMING = 0
       const val LAKWATSA_CANCELLED = -1
   }



    var lakwatsaId: Int
        private set
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
    var spendatureList: HashMap<User, Float>
        private set
    var album: ArrayList<Image>
        private set
    var status: Int
        private set

    constructor(lakwatsaId: Int, lakwatsaUsers: ArrayList<User>, location: String, lakwatsaTitle: String, date: Date, pollingList: HashMap<Date, Int>, spendatureList: HashMap<User, Float>, album: ArrayList<Image>) {
        this.lakwatsaId = lakwatsaId
        this.lakwatsaUsers = lakwatsaUsers
        this.location = location
        this.lakwatsaTitle = lakwatsaTitle
        this.date = date
        this.pollingList = pollingList
        this.spendatureList = spendatureList
        this.album = album
        this.status = LAKWATSA_UPCOMING
    }

    fun addImage(image: Image) {
        this.album.add(image)
    }

    fun removeImage(image: Image) {
        this.album.remove(image)
    }

    fun addPolling(date: Date, rating: Int) {
        this.pollingList[date] = rating
    }

    fun removePolling(date: Date) {
        this.pollingList.remove(date)
    }




}