package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding

class AdapterSchedule(private var dataUnavailable: ArrayList<Unavailable>, private var dataLakwatsa: ArrayList<Lakwatsa>): RecyclerView.Adapter<ViewHolderSchedule>() {

    private var data: ArrayList<Event> = ArrayList()

    fun getMonth(date: String): String{
        // Assuming format is YYYY/MM/DD (delimited by /)
        val month = date.split("/")[1]

        // Translate to month name abbreviated in all caps
        when (month) {
            "01" -> return "JAN"
            "02" -> return "FEB"
            "03" -> return "MAR"
            "04" -> return "APR"
            "05" -> return "MAY"
            "06" -> return "JUN"
            "07" -> return "JUL"
            "08" -> return "AUG"
            "09" -> return "SEP"
            "10" -> return "OCT"
            "11" -> return "NOV"
            "12" -> return "DEC"
        }
        return "ERR"
    }

    fun getDay(date: String): String{
        // Assuming format is YYYY/MM/DD
        val day = date.split("/")[2]
        return day
    }

    init {
        // translate dataUnavailable to Event
        var unIndex = 0
        for (un in dataUnavailable) {
            var startMonth = getMonth(un.startDate)
            var startDay = getDay(un.startDate)
            var endMonth = getMonth(un.endDate)
            var endDay = getDay(un.endDate)

            if(startMonth != endMonth){
                startMonth = startMonth + " - " + endMonth
                startDay = startDay + " - " + endDay
            } else if (startDay != endDay){
                startDay = startDay + " - " + endDay
            }

            data.add(Event(unIndex.toString(), un.name, startMonth, startDay, Event.EVENT_TYPE_UNAVAILABLE, un.startDate, un.endDate))
            unIndex++
        }

        // translate dataLakwatsa to Event
        for (lak in dataLakwatsa) {
            var startMonth = lak.date.monthValue.toString()

            when(startMonth){
                "1" -> startMonth = "JAN"
                "2" -> startMonth = "FEB"
                "3" -> startMonth = "MAR"
                "4" -> startMonth = "APR"
                "5" -> startMonth = "MAY"
                "6" -> startMonth = "JUN"
                "7" -> startMonth = "JUL"
                "8" -> startMonth = "AUG"
                "9" -> startMonth = "SEP"
                "10" -> startMonth = "OCT"
                "11" -> startMonth = "NOV"
                "12" -> startMonth = "DEC"
            }

            var startDay = lak.date.dayOfMonth.toString()

            var startDateFormatted = lak.date.year.toString() + "/" + lak.date.monthValue.toString().padStart(2, '0') + "/" + lak.date.dayOfMonth.toString().padStart(2, '0')
            data.add(Event(lak.lakwatsaId, lak.lakwatsaTitle, startMonth, startDay, Event.EVENT_TYPE_LAKWATSA, startDateFormatted, startDateFormatted))
        }

        // sort data by date
        data.sortBy { it.startDateFull }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSchedule {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLayoutScheduleBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolderSchedule(viewBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderSchedule, position: Int) {
        holder.bindData(data[position])
    }
}