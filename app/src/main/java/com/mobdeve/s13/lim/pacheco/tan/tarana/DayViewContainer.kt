package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.CalendarDayLayoutBinding

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayLayoutTv
    val constraintLayout = CalendarDayLayoutBinding.bind(view).calendarDayLayoutCl
}