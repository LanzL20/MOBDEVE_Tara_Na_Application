package com.mobdeve.s13.lim.pacheco.tan.tarana

import com.google.type.DateTime
import java.time.LocalDate
import java.time.LocalDateTime

class Unavailable {
    var name: String
    var startDate: String
    var endDate: String

    constructor(name: String, startDate: String, endDate: String) {
        this.name = name
        this.startDate = startDate
        this.endDate = endDate
    }
}