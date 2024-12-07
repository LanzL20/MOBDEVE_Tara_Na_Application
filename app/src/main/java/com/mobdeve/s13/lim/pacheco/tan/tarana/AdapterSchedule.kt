package com.mobdeve.s13.lim.pacheco.tan.tarana

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.lim.pacheco.tan.tarana.databinding.ItemLayoutScheduleBinding

class AdapterSchedule(private var dataUnavailable: ArrayList<Unavailable>, private var dataLakwatsa: ArrayList<Lakwatsa>, private var hide: Boolean): RecyclerView.Adapter<ViewHolderSchedule>() {

    private var data: ArrayList<Event> = ArrayList()

    fun getYear(date: String): String{
        // Assuming format is YYYY/MM/DD
        val year = date.split("/")[0]
        return year
    }


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
            var startMonth = getMonth(lak.date)
            var startDay = getDay(lak.date)

            data.add(Event(lak.lakwatsaId, lak.lakwatsaTitle, startMonth, startDay, Event.EVENT_TYPE_LAKWATSA, lak.date, lak.date))
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
        holder.bindData(data[position], hide)
    }
}