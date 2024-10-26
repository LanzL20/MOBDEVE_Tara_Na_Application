package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.time.LocalDate

// TODO: I don't know if this will fit into the schema...
class Event(eventId: Int, eventName: String, date: LocalDate, eventType: String) {
    companion object {
        const val EVENT_TYPE_LAKWATSA = "Lakwatsa"
        const val EVENT_TYPE_UNAVAILABLE = "Unavailable"
    }

    var eventId: Int = eventId
        private set
    var eventName: String = eventName
        private set
    var date: LocalDate = date
        private set
    var eventType: String = eventType
        private set
}