package com.mobdeve.s13.lim.pacheco.tan.tarana

import java.time.LocalDate

// This is an internal type that generalizes Lakwatsa and Unavailable events for the ViewHolderSchedule class.
class Event(associatedId: String, eventName: String, dateMonths: String, dateDays: String, eventType: String, startDateFull: String, endDateFull: String) {
    companion object {
        const val EVENT_TYPE_LAKWATSA = "Lakwatsa"
        const val EVENT_TYPE_UNAVAILABLE = "Unavailable"
    }
    // Will be used for deleting...
    var associatedId: String = associatedId
        private set
    var eventName: String = eventName
        private set
    var dateMonths: String = dateMonths
        private set
    var dateDays: String = dateDays
        private set
    var eventType: String = eventType
        private set
    // Will be used for sorting...
    var startDateFull: String = startDateFull
        private set
    var endDateFull: String = endDateFull
        private set
}