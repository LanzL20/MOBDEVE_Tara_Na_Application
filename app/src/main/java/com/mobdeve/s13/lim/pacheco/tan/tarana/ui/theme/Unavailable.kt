package com.mobdeve.s13.lim.pacheco.tan.tarana.ui.theme

import com.google.type.DateTime

class Unavailable {
    var name: String
    var startDate: DateTime
    var endDate: DateTime

    constructor(name: String, startDate: DateTime, endDate: DateTime) {
        this.name = name
        this.startDate = startDate
        this.endDate = endDate
    }
}